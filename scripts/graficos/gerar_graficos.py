"""Gera gráficos vetoriais e um resumo por modelo a partir de metricas.csv.

Uso: python scripts/graficos/gerar_graficos.py

Assertividade entra no gráfico quando a coluna assertividade_0_100 da planilha
de métricas estiver preenchida com notas calculadas pelos mesmos testes.
"""

import csv
import html
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
CSV_PATH = ROOT / "resultados_modelos" / "metricas.csv"
OUT = ROOT / "graficos"
MODELS = ["phi4-mini:3.8b", "gemma3:4b", "mistral:7b-instruct"]
COLORS = {
    "phi4-mini:3.8b": "#3267a8",
    "gemma3:4b": "#cf7b22",
    "mistral:7b-instruct": "#287a58",
}


def numeric(values):
    result = []
    for value in values:
        try:
            if value not in (None, ""):
                result.append(float(value))
        except (TypeError, ValueError):
            pass
    return result


def average(values):
    vals = numeric(values)
    return sum(vals) / len(vals) if vals else None


def svg_bar_chart(filename, title, subtitle, values, unit="", max_value=None, percent=False):
    width, height = 960, 560
    left, right, top, bottom = 100, 48, 92, 104
    plot_w, plot_h = width - left - right, height - top - bottom
    available = [v for v in values.values() if v is not None]
    if not available:
        return
    upper = max_value if max_value is not None else max(available) * 1.18
    upper = upper or 1
    # Round chart limits to a readable interval.
    if max_value is None:
        magnitude = 10 ** max(0, len(str(int(upper))) - 1)
        upper = ((upper + magnitude - 1) // magnitude) * magnitude
    rows = [
        f'<svg xmlns="http://www.w3.org/2000/svg" width="{width}" height="{height}" viewBox="0 0 {width} {height}" role="img" aria-labelledby="title desc">',
        f'<title id="title">{html.escape(title)}</title>',
        f'<desc id="desc">Gráfico de barras comparando {html.escape(subtitle)} para Phi-4 Mini, Gemma 3 e Mistral. Os valores estão escritos sobre as barras.</desc>',
        '<rect width="100%" height="100%" fill="#ffffff"/>',
        f'<text x="{left}" y="38" font-family="Arial, sans-serif" font-size="25" font-weight="700" fill="#18212b">{html.escape(title)}</text>',
        f'<text x="{left}" y="66" font-family="Arial, sans-serif" font-size="15" fill="#52606d">{html.escape(subtitle)}</text>',
    ]
    ticks = 4
    for i in range(ticks + 1):
        val = upper * i / ticks
        y = top + plot_h - plot_h * i / ticks
        label = f"{val:.0f}%" if percent else f"{val:.0f}"
        rows.append(f'<line x1="{left}" y1="{y:.1f}" x2="{width-right}" y2="{y:.1f}" stroke="#d8dee5" stroke-width="1"/>')
        rows.append(f'<text x="{left-12}" y="{y+5:.1f}" text-anchor="end" font-family="Arial, sans-serif" font-size="13" fill="#46515c">{label}</text>')
    names = {"phi4-mini:3.8b": "Phi-4 Mini", "gemma3:4b": "Gemma 3", "mistral:7b-instruct": "Mistral 7B Instruct"}
    group_w = plot_w / len(MODELS)
    bar_w = min(112, group_w * 0.48)
    for i, model in enumerate(MODELS):
        value = values.get(model)
        center = left + group_w * (i + 0.5)
        if value is not None:
            bar_h = plot_h * max(0, value) / upper
            x, y = center - bar_w / 2, top + plot_h - bar_h
            rows.append(f'<rect x="{x:.1f}" y="{y:.1f}" width="{bar_w:.1f}" height="{bar_h:.1f}" rx="3" fill="{COLORS[model]}"/>')
            shown = f"{value:.1f}%" if percent else f"{value:.1f}{unit}"
            rows.append(f'<text x="{center:.1f}" y="{max(top+16,y-10):.1f}" text-anchor="middle" font-family="Arial, sans-serif" font-size="15" font-weight="700" fill="#18212b">{shown}</text>')
        rows.append(f'<text x="{center:.1f}" y="{height-bottom+28}" text-anchor="middle" font-family="Arial, sans-serif" font-size="14" fill="#18212b">{html.escape(names[model])}</text>')
    rows.append(f'<line x1="{left}" y1="{top+plot_h}" x2="{width-right}" y2="{top+plot_h}" stroke="#46515c" stroke-width="1.5"/>')
    rows.append('</svg>')
    (OUT / filename).write_text("\n".join(rows), encoding="utf-8")


def png_bar_chart(svg_filename, title, subtitle, values, unit="", max_value=None, percent=False):
    """Also emit a raster copy for direct inclusion with LaTeX graphicx."""
    try:
        from PIL import Image, ImageDraw, ImageFont
    except ImportError:
        return
    width, height = 1920, 1120
    scale = 2
    left, right, top, bottom = 200, 96, 184, 208
    plot_w, plot_h = width - left - right, height - top - bottom
    available = [v for v in values.values() if v is not None]
    if not available:
        return
    upper = max_value if max_value is not None else max(available) * 1.18
    upper = upper or 1
    if max_value is None:
        magnitude = 10 ** max(0, len(str(int(upper))) - 1)
        upper = ((upper + magnitude - 1) // magnitude) * magnitude
    image = Image.new("RGB", (width, height), "white")
    draw = ImageDraw.Draw(image)
    font_path = "C:/Windows/Fonts/arial.ttf"
    def font(size):
        try:
            return ImageFont.truetype(font_path, size)
        except OSError:
            return ImageFont.load_default()
    title_font, body_font, small_font, value_font = font(50), font(32), font(28), font(30)
    draw.text((left, 38), title, font=title_font, fill="#18212b")
    draw.text((left, 104), subtitle, font=small_font, fill="#52606d")
    for i in range(5):
        val = upper * i / 4
        y = top + plot_h - plot_h * i / 4
        label = f"{val:.0f}%" if percent else f"{val:.0f}"
        draw.line((left, y, width-right, y), fill="#d8dee5", width=2)
        box = draw.textbbox((0, 0), label, font=small_font)
        draw.text((left - 20 - (box[2]-box[0]), y - 18), label, font=small_font, fill="#46515c")
    names = {"phi4-mini:3.8b": "Phi-4 Mini", "gemma3:4b": "Gemma 3", "mistral:7b-instruct": "Mistral 7B Instruct"}
    group_w = plot_w / len(MODELS)
    bar_w = min(224, group_w * 0.48)
    for i, model in enumerate(MODELS):
        value = values.get(model)
        center = left + group_w * (i + 0.5)
        if value is not None:
            bar_h = plot_h * max(0, value) / upper
            x, y = center - bar_w / 2, top + plot_h - bar_h
            draw.rounded_rectangle((x, y, x + bar_w, top + plot_h), radius=8, fill=COLORS[model])
            shown = f"{value:.1f}%" if percent else f"{value:.1f}{unit}"
            box = draw.textbbox((0, 0), shown, font=value_font)
            draw.text((center-(box[2]-box[0])/2, max(top+4, y-42)), shown, font=value_font, fill="#18212b")
        label = names[model]
        box = draw.textbbox((0, 0), label, font=body_font)
        draw.text((center-(box[2]-box[0])/2, height-bottom+50), label, font=body_font, fill="#18212b")
    draw.line((left, top+plot_h, width-right, top+plot_h), fill="#46515c", width=3)
    image.save(OUT / svg_filename.replace(".svg", ".png"), dpi=(300, 300))


def main():
    if not CSV_PATH.exists():
        raise SystemExit(f"Não encontrei {CSV_PATH}")
    with CSV_PATH.open(encoding="utf-8-sig", newline="") as f:
        rows = list(csv.DictReader(f))
    if not rows:
        raise SystemExit("O CSV de métricas está vazio.")
    groups = {model: [r for r in rows if r.get("modelo") == model] for model in MODELS}
    OUT.mkdir(parents=True, exist_ok=True)

    metrics = {
        "tokens_total_medio": {m: average(r.get("tokens_total") for r in groups[m]) for m in MODELS},
        "tempo_segundos_medio": {m: average(r.get("tempo_total_s") for r in groups[m]) for m in MODELS},
        "linhas_codigo_media": {m: average(r.get("linhas_codigo") for r in groups[m]) for m in MODELS},
        "complexidade_ciclomatica_estimada_media": {m: average(r.get("complexidade_ciclomatica_estimada") for r in groups[m]) for m in MODELS},
        "compilacao_pct": {m: 100 * sum(r.get("compilou") == "sim" for r in groups[m]) / len(groups[m]) if groups[m] else None for m in MODELS},
        "execucao_pct": {m: 100 * sum(r.get("executou") == "sim" for r in groups[m]) / len(groups[m]) if groups[m] else None for m in MODELS},
        "assertividade_pct_media": {m: average(r.get("assertividade_0_100") for r in groups[m]) for m in MODELS},
    }

    charts = [
        ("tokens-medios.svg", "Tokens médios por resposta", "Entrada + saída; média de 30 especificações", metrics["tokens_total_medio"], " tokens", None, False),
        ("tempo-medio.svg", "Tempo médio de resposta", "Segundos por geração; média de 30 especificações", metrics["tempo_segundos_medio"], " s", None, False),
        ("linhas-medias.svg", "Linhas de código", "Média de linhas por resposta", metrics["linhas_codigo_media"], " linhas", None, False),
        ("complexidade-estimada.svg", "Complexidade ciclomática estimada", "Média; contagem heurística por código", metrics["complexidade_ciclomatica_estimada_media"], "", None, False),
        ("taxa-compilacao.svg", "Taxa de compilação", "Respostas que compilaram; percentual das 30", metrics["compilacao_pct"], "", 100, True),
        ("taxa-execucao.svg", "Taxa de execução", "Respostas que executaram sem erro; percentual das 30", metrics["execucao_pct"], "", 100, True),
    ]
    if any(value is not None for value in metrics["assertividade_pct_media"].values()):
        charts.append(("assertividade.svg", "Assertividade funcional", "Média de critérios aprovados; percentual por modelo", metrics["assertividade_pct_media"], "", 100, True))
    for filename, title, subtitle, values, unit, maximum, percent in charts:
        svg_bar_chart(filename, title, subtitle, values, unit, max_value=maximum, percent=percent)
        png_bar_chart(filename, title, subtitle, values, unit, max_value=maximum, percent=percent)

    fields = ["modelo", *metrics.keys(), "respostas"]
    with (OUT / "resumo_por_modelo.csv").open("w", encoding="utf-8-sig", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fields)
        writer.writeheader()
        for model in MODELS:
            outrow = {"modelo": model, "respostas": len(groups[model])}
            for key in metrics:
                value = metrics[key][model]
                outrow[key] = "" if value is None else round(value, 3)
            writer.writerow(outrow)
    print(f"Gráficos e resumo salvos em: {OUT}")
    if not any(value is not None for value in metrics["assertividade_pct_media"].values()):
        print("Assertividade ainda não preenchida; o gráfico correspondente será gerado depois dos testes funcionais.")


if __name__ == "__main__":
    main()
