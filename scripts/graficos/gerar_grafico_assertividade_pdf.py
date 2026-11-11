"""Gera um gráfico vetorial PDF sem dependências externas para o relatório SBC."""
import csv
from pathlib import Path

ROOT = Path(__file__).resolve().parents[2]
CSV = ROOT / "resultados_modelos" / "metricas.csv"
OUT_DIR = ROOT / "graficos"
OUT_DIR.mkdir(exist_ok=True)

models = ["phi4-mini:3.8b", "gemma3:4b", "mistral:7b-instruct"]
labels = ["Phi-4 Mini", "Gemma 3", "Mistral 7B Instruct"]
colors = [(0.196, 0.404, 0.659), (0.812, 0.482, 0.133), (0.157, 0.478, 0.345)]
values = {m: [] for m in models}
with CSV.open(encoding="utf-8-sig", newline="") as f:
    for row in csv.DictReader(f):
        if row["modelo"] in values:
            values[row["modelo"]].append(float(row["assertividade_0_100"]))
means = [sum(values[m]) / len(values[m]) for m in models]

commands = ["1 1 1 rg", "BT /F1 24 Tf 70 515 Td (Assertividade media por modelo) Tj ET",
            "BT /F1 12 Tf 70 492 Td (Pontuacao media em 30 respostas, escala de 0 a 100) Tj ET"]
left, bottom, plot_w, plot_h, maxval = 120, 125, 760, 300, 100
for tick in [0, 25, 50, 75, 100]:
    y = bottom + plot_h * tick / maxval
    commands += ["0.84 0.87 0.90 RG 0.7 w", f"{left} {y:.1f} m {left+plot_w} {y:.1f} l S",
                 f"0.20 0.24 0.28 rg BT /F1 11 Tf 82 {y-4:.1f} Td ({tick}) Tj ET"]
bar_w, gap = 125, 118
for i, (label, value, color) in enumerate(zip(labels, means, colors)):
    x = left + 72 + i * (bar_w + gap)
    h = plot_h * value / maxval
    commands.append("%.3f %.3f %.3f rg %d %d %d %.1f re f" % (*color, x, bottom, bar_w, h))
    commands.append(f"0.10 0.13 0.17 rg BT /F1 13 Tf {x+24} {bottom+h+10:.1f} Td ({value:.1f}%) Tj ET")
    commands.append(f"0.10 0.13 0.17 rg BT /F1 12 Tf {x+14} 98 Td ({label}) Tj ET")
commands += ["0.20 0.24 0.28 RG 1 w", f"{left} {bottom} m {left+plot_w} {bottom} l S"]
stream = ("\n".join(commands) + "\n").encode("ascii")
objs = [
    b"<< /Type /Catalog /Pages 2 0 R >>",
    b"<< /Type /Pages /Kids [3 0 R] /Count 1 >>",
    b"<< /Type /Page /Parent 2 0 R /MediaBox [0 0 960 560] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>",
    b"<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>",
    b"<< /Length " + str(len(stream)).encode() + b" >>\nstream\n" + stream + b"endstream",
]
pdf = bytearray(b"%PDF-1.4\n%\xe2\xe3\xcf\xd3\n")
offsets = [0]
for i, obj in enumerate(objs, 1):
    offsets.append(len(pdf))
    pdf += f"{i} 0 obj\n".encode() + obj + b"\nendobj\n"
xref = len(pdf)
pdf += f"xref\n0 {len(objs)+1}\n0000000000 65535 f \n".encode()
for offset in offsets[1:]:
    pdf += f"{offset:010d} 00000 n \n".encode()
pdf += f"trailer\n<< /Size {len(objs)+1} /Root 1 0 R >>\nstartxref\n{xref}\n%%EOF\n".encode()
path = OUT_DIR / "assertividade.pdf"
path.write_bytes(pdf)
print(path)
