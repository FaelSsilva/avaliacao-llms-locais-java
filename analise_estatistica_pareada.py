import csv
import itertools
import json
import math
import random
import statistics
import sys
from collections import defaultdict
from pathlib import Path


MODELS = ["phi4-mini:3.8b", "gemma3:4b", "mistral:7b-instruct"]
NUMERIC = {
    "assertividade_0_100": "Assertividade",
    "tokens_total": "Tokens",
    "tempo_total_s": "Tempo (s)",
    "linhas_codigo": "Linhas",
    "complexidade_ciclomatica_estimada": "Complexidade estimada",
}


def chi2_df2_p(value):
    return math.exp(-value / 2.0)


def average_ranks(values):
    order = sorted(range(len(values)), key=lambda i: values[i])
    ranks = [0.0] * len(values)
    ties = []
    pos = 0
    while pos < len(order):
        end = pos + 1
        while end < len(order) and values[order[end]] == values[order[pos]]:
            end += 1
        rank = (pos + 1 + end) / 2.0
        for j in order[pos:end]:
            ranks[j] = rank
        if end - pos > 1:
            ties.append(end - pos)
        pos = end
    return ranks, ties


def holm(items):
    ordered = sorted(items, key=lambda x: x["p"])
    running = 0.0
    m = len(ordered)
    for index, item in enumerate(ordered):
        running = max(running, min(1.0, (m - index) * item["p"]))
        item["p_holm"] = running


def friedman(columns, permutations=20000):
    n, k = len(columns[0]), len(columns)
    rank_sums = [0.0] * k
    tie_sum = 0
    for row in zip(*columns):
        ranks, ties = average_ranks(row)
        rank_sums = [a + b for a, b in zip(rank_sums, ranks)]
        tie_sum += sum(t**3 - t for t in ties)
    raw = 12.0 / (n * k * (k + 1)) * sum(r * r for r in rank_sums) - 3 * n * (k + 1)
    correction = 1.0 - tie_sum / (n * k * (k * k - 1))
    statistic = raw / correction if correction > 0 else 0.0
    # Monte Carlo randomization: permute model labels within each problem block.
    # It avoids relying only on the chi-square approximation with three models.
    rng = random.Random(20261111)
    rank_rows = [average_ranks(row)[0] for row in zip(*columns)]
    exceed = 0
    for _ in range(permutations):
        sums = [0.0] * k
        for rank_row in rank_rows:
            permuted = rank_row[:]
            rng.shuffle(permuted)
            for j, rank in enumerate(permuted):
                sums[j] += rank
        q = 12.0 / (n * k * (k + 1)) * sum(r * r for r in sums) - 3 * n * (k + 1)
        q = q / correction if correction > 0 else 0.0
        if q >= statistic - 1e-12:
            exceed += 1
    p_perm = (exceed + 1) / (permutations + 1)
    return {"n": n, "statistic": statistic, "df": k - 1,
            "p": p_perm, "p_chi2_approx": chi2_df2_p(statistic), "p_permutation": p_perm,
            "permutations": permutations, "seed": 20261111,
            "kendall_w": statistic / (n * (k - 1)),
            "rank_sums": rank_sums}


def wilcoxon(x, y):
    diffs = [a - b for a, b in zip(x, y) if a != b]
    if not diffs:
        return {"n_nonzero": 0, "w_plus": 0.0, "effect_rank_biserial": 0.0, "p": 1.0}
    ranks, _ = average_ranks([abs(d) for d in diffs])
    w_plus = sum(r for r, d in zip(ranks, diffs) if d > 0)
    total = sum(ranks)
    variance = sum(r * r for r in ranks) / 4.0
    z = max(0.0, abs(w_plus - total / 2.0) - 0.5) / math.sqrt(variance)
    p = math.erfc(z / math.sqrt(2.0))
    return {"n_nonzero": len(diffs), "w_plus": w_plus,
            "effect_rank_biserial": (2 * w_plus - total) / total, "p": p}


def cochran_q(columns):
    rows = list(zip(*columns))
    k = len(columns)
    totals = [sum(col) for col in columns]
    row_squares = sum(sum(row) ** 2 for row in rows)
    total = sum(totals)
    denom = k * total - row_squares
    q = (k - 1) * (k * sum(c * c for c in totals) - total * total) / denom if denom else 0.0
    return {"n": len(rows), "statistic": q, "df": k - 1, "p": chi2_df2_p(q), "successes": totals}


def mcnemar_exact(x, y):
    b = sum(a == 1 and c == 0 for a, c in zip(x, y))
    c = sum(a == 0 and d == 1 for a, d in zip(x, y))
    discordant = b + c
    tail = sum(math.comb(discordant, i) for i in range(min(b, c) + 1)) / (2 ** discordant) if discordant else 1.0
    return {"b": b, "c": c, "p": min(1.0, 2 * tail)}


def quantiles(values):
    vals = sorted(values)
    return {"mean": statistics.mean(vals), "median": statistics.median(vals),
            "q1": statistics.quantiles(vals, n=4, method="inclusive")[0],
            "q3": statistics.quantiles(vals, n=4, method="inclusive")[2]}


def main():
    if len(sys.argv) > 1:
        csv_path = Path(sys.argv[1])
    else:
        candidates = [Path("resultados_ollama/metricas.csv"), Path(__file__).with_name("metricas.csv"),
                      Path(__file__).parent / "artefatos" / "metricas.csv"]
        csv_path = next((candidate for candidate in candidates if candidate.exists()), candidates[0])
    rows = list(csv.DictReader(csv_path.open(encoding="utf-8-sig", newline="")))
    data = defaultdict(dict)
    for row in rows:
        data[row["problema_id"]][row["modelo"]] = row
    ids = sorted(data)
    if len(ids) != 30 or any(set(data[i]) != set(MODELS) for i in ids):
        raise SystemExit("Esperados 30 problemas, cada um com exatamente uma resposta dos três modelos.")
    result = {"n_problems": len(ids), "models": MODELS, "numeric": {}, "pairwise_numeric": [], "binary": {}, "pairwise_binary": []}
    numeric_arrays = {}
    for field, label in NUMERIC.items():
        cols = [[float(data[i][m][field]) for i in ids] for m in MODELS]
        numeric_arrays[field] = cols
        result["numeric"][field] = {"label": label, "friedman": friedman(cols),
            "descriptive": {m: quantiles(col) for m, col in zip(MODELS, cols)}}
        for a, b in itertools.combinations(range(3), 2):
            test = wilcoxon(cols[a], cols[b])
            result["pairwise_numeric"].append({"metric": field, "a": MODELS[a], "b": MODELS[b], **test})
    for field in ("compilou", "executou"):
        cols = [[int(data[i][m][field] == "sim") for i in ids] for m in MODELS]
        result["binary"][field] = {"cochran_q": cochran_q(cols),
            "rates": {m: {"successes": sum(col), "n": len(col), "rate": sum(col) / len(col)} for m, col in zip(MODELS, cols)}}
        for a, b in itertools.combinations(range(3), 2):
            test = mcnemar_exact(cols[a], cols[b])
            result["pairwise_binary"].append({"outcome": field, "model_a": MODELS[a], "model_b": MODELS[b],
                                              **test})
    holm([v["friedman"] for v in result["numeric"].values()])
    holm(result["pairwise_numeric"])
    holm([v["cochran_q"] for v in result["binary"].values()])
    holm(result["pairwise_binary"])
    out = csv_path.parent / "analise_estatistica_pareada.json"
    out.write_text(json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8")
    lines = ["# Análise estatística pareada", "", f"Problemas pareados: {len(ids)}. Nível α=0,05.",
             "Friedman por permutação (20.000; semente 20261111); p de Friedman ajustados por Holm entre cinco medidas.", "",
             "## Testes globais numéricos", "", "| Medida | Friedman χ²(2) | p permutação | p Holm | Kendall W |", "|---|---:|---:|---:|---:|"]
    for field, item in result["numeric"].items():
        f = item["friedman"]
        lines.append(f"| {item['label']} | {f['statistic']:.3f} | {f['p']:.5g} | {f['p_holm']:.5g} | {f['kendall_w']:.3f} |")
    lines += ["", "## Desfechos binários", "", "| Desfecho | Cochran Q(2) | p | p Holm |", "|---|---:|---:|---:|"]
    for field, item in result["binary"].items():
        q = item["cochran_q"]
        lines.append(f"| {field} | {q['statistic']:.3f} | {q['p']:.5g} | {q['p_holm']:.5g} |")
    lines += ["", "## Comparações numéricas pós-hoc", "", "p de Wilcoxon com aproximação normal e correção de continuidade; ajuste de Holm global nas 15 comparações.",
              "", "| Métrica | Modelo A | Modelo B | n não-zero | p | p Holm | Efeito rank-biserial |", "|---|---|---|---:|---:|---:|---:|"]
    for item in result["pairwise_numeric"]:
        lines.append(f"| {NUMERIC[item['metric']]} | {item['a']} | {item['b']} | {item['n_nonzero']} | {item['p']:.5g} | {item['p_holm']:.5g} | {item['effect_rank_biserial']:.3f} |")
    lines += ["", "## Comparações binárias pós-hoc", "", "McNemar exato; ajuste de Holm global nas seis comparações.",
              "", "| Desfecho | Modelo A | Modelo B | Discordâncias A→B | Discordâncias B→A | p | p Holm |", "|---|---|---|---:|---:|---:|---:|"]
    for item in result["pairwise_binary"]:
        lines.append(f"| {item['outcome']} | {item['model_a']} | {item['model_b']} | {item['b']} | {item['c']} | {item['p']:.5g} | {item['p_holm']:.5g} |")
    (csv_path.parent / "analise_estatistica_pareada.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(json.dumps(result, ensure_ascii=False, indent=2))
    print(f"\nSalvo em: {out}")


if __name__ == "__main__":
    main()
