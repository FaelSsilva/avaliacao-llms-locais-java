#!/usr/bin/env python3
"""Executa os 30 prompts nos modelos Ollama e registra métricas locais.

Uso:
  python medir_ollama.py                 # roda todos os 90 pares
  python medir_ollama.py --limit 1       # teste rápido com o primeiro problema
  python medir_ollama.py --report        # resume resultados já gravados

Assertividade deve ser avaliada com testes do problema e preenchida manualmente
na coluna assertividade_0_100 do CSV. Compilar/executar não prova correção.
"""

from __future__ import annotations

import argparse
import csv
import json
import re
import shutil
import subprocess
import sys
import time
import urllib.error
import urllib.request
from datetime import datetime, timezone
from pathlib import Path

ROOT = Path(__file__).resolve().parent
PROMPTS_FILE = ROOT / "prompts_30_biblioteca.md"
OUT_DIR = ROOT / "resultados_ollama"
CSV_FILE = OUT_DIR / "metricas.csv"
DEFAULT_MODELS = ["phi4-mini:3.8b", "gemma3:4b", "mistral:7b-instruct"]
OLLAMA_URL = "http://localhost:11434/api/chat"
SYSTEM_MARKER = "## Prompt-base (usar como mensagem de sistema em todos os casos)"
PROBLEMS_MARKER = "## Especificações (usar uma por vez como mensagem do usuário)"

FIELDS = [
    "problema_id", "problema_titulo", "modelo", "data_utc", "status",
    "tokens_entrada", "tokens_saida", "tokens_total", "tempo_total_s",
    "tempo_prompt_s", "tempo_geracao_s", "linhas_codigo",
    "complexidade_ciclomatica_estimada", "compilou", "executou",
    "assertividade_0_100", "arquivo_codigo", "erro",
]


def read_prompt_bank() -> tuple[str, list[tuple[str, str, str]]]:
    text = PROMPTS_FILE.read_text(encoding="utf-8")
    system = text.split(SYSTEM_MARKER, 1)[1].split(PROBLEMS_MARKER, 1)[0]
    # Remove the Markdown quote prefix from the common role-playing prompt.
    system = "\n".join(re.sub(r"^> ?", "", line) for line in system.strip().splitlines())
    section = text.split(PROBLEMS_MARKER, 1)[1].split("## Registro de avaliação", 1)[0]
    matches = list(re.finditer(r"(?m)^### (P\d{2}) — (.+)$", section))
    problems = []
    for i, match in enumerate(matches):
        end = matches[i + 1].start() if i + 1 < len(matches) else len(section)
        body = section[match.end():end].strip()
        problems.append((match.group(1), match.group(2).strip(), body))
    return system, problems


def extract_code(response: str) -> str:
    blocks = re.findall(r"```(?:java)?\s*\n(.*?)```", response, flags=re.I | re.S)
    if blocks:
        return blocks[0].strip() + "\n"
    # If a model ignores the code-fence instruction, discard any prose before
    # the Java entry class, then stop at its matching closing brace.
    match = re.search(r"(?m)^\s*public\s+class\s+Main\b", response)
    if not match:
        return response.strip() + "\n"
    start = match.start()
    depth = 0
    state = "code"
    escaped = False
    i = response.find("{", match.end())
    if i < 0:
        return response[start:].strip() + "\n"
    while i < len(response):
        ch = response[i]
        nxt = response[i + 1] if i + 1 < len(response) else ""
        if state == "line_comment":
            if ch == "\n": state = "code"
        elif state == "block_comment":
            if ch == "*" and nxt == "/": state = "code"; i += 1
        elif state in ("string", "char"):
            if escaped: escaped = False
            elif ch == "\\": escaped = True
            elif (state == "string" and ch == '"') or (state == "char" and ch == "'"):
                state = "code"
        else:
            if ch == "/" and nxt == "/": state = "line_comment"; i += 1
            elif ch == "/" and nxt == "*": state = "block_comment"; i += 1
            elif ch == '"': state = "string"
            elif ch == "'": state = "char"
            elif ch == "{": depth += 1
            elif ch == "}":
                depth -= 1
                if depth == 0:
                    return response[start:i + 1].strip() + "\n"
        i += 1
    return response[start:].strip() + "\n"


def estimate_cyclomatic(code: str) -> int:
    """Heuristic estimate: 1 + control-flow branches + boolean operators."""
    clean = re.sub(r'//[^\n]*|/\*.*?\*/|"(?:\\.|[^"\\])*"|\'(?:\\.|[^\'\\])*\'', " ", code, flags=re.S)
    branches = len(re.findall(r"\b(?:if|for|while|case|catch)\b|\?(?!\?)", clean))
    boolean_ops = len(re.findall(r"&&|\|\|", clean))
    return 1 + branches + boolean_ops


def find_entry_class(code: str) -> str:
    """Find the class containing main(), allowing models to name it differently."""
    package = re.search(r"(?m)^\s*package\s+([A-Za-z_$][\w$]*(?:\.[A-Za-z_$][\w$]*)*)\s*;", code)
    prefix = f"{package.group(1)}." if package else ""
    for match in re.finditer(r"\bclass\s+([A-Za-z_$][\w$]*)\b", code):
        opening = code.find("{", match.end())
        if opening < 0:
            continue
        depth, state, escaped, i = 0, "code", False, opening
        while i < len(code):
            ch = code[i]
            nxt = code[i + 1] if i + 1 < len(code) else ""
            if state == "line_comment":
                if ch == "\n": state = "code"
            elif state == "block_comment":
                if ch == "*" and nxt == "/": state = "code"; i += 1
            elif state in ("string", "char"):
                if escaped: escaped = False
                elif ch == "\\": escaped = True
                elif (state == "string" and ch == '"') or (state == "char" and ch == "'"):
                    state = "code"
            else:
                if ch == "/" and nxt == "/": state = "line_comment"; i += 1
                elif ch == "/" and nxt == "*": state = "block_comment"; i += 1
                elif ch == '"': state = "string"
                elif ch == "'": state = "char"
                elif ch == "{": depth += 1
                elif ch == "}":
                    depth -= 1
                    if depth == 0:
                        body = code[opening:i + 1]
                        if re.search(r"\bstatic\s+void\s+main\s*\(", body):
                            return prefix + match.group(1)
                        break
            i += 1
    return prefix + "Main"


def compile_and_run(code: str, folder: Path) -> tuple[str, str, str]:
    javac, java = shutil.which("javac"), shutil.which("java")
    if not javac or not java:
        return "indisponivel", "indisponivel", "JDK não encontrado no PATH"
    folder.mkdir(parents=True, exist_ok=True)
    source = folder / "Main.java"
    source.write_text(code, encoding="utf-8")
    compiled = subprocess.run([javac, "-d", str(folder), str(source)], capture_output=True, text=True, timeout=60)
    if compiled.returncode:
        return "nao", "nao", (compiled.stderr or compiled.stdout).strip()[:1500]
    entry_class = find_entry_class(code)
    ran = subprocess.run([java, "-cp", str(folder), entry_class], capture_output=True, text=True, timeout=30)
    if ran.returncode:
        return "sim", "nao", (ran.stderr or ran.stdout).strip()[:1500]
    return "sim", "sim", ""


def call_ollama(model: str, system: str, prompt: str) -> tuple[dict, float]:
    payload = {
        "model": model,
        "messages": [{"role": "system", "content": system}, {"role": "user", "content": prompt}],
        "stream": False,
        # Disable chain-of-thought mode uniformly; otherwise Qwen3 may spend its
        # output budget on hidden reasoning and return no Java source.
        "think": False,
        "options": {"temperature": 0, "seed": 42, "num_predict": 2048},
    }
    request = urllib.request.Request(
        OLLAMA_URL,
        data=json.dumps(payload).encode("utf-8"),
        headers={"Content-Type": "application/json"},
        method="POST",
    )
    start = time.perf_counter()
    with urllib.request.urlopen(request, timeout=900) as response:
        result = json.loads(response.read().decode("utf-8"))
    wall_s = time.perf_counter() - start
    return result, wall_s


def write_row(row: dict) -> None:
    OUT_DIR.mkdir(parents=True, exist_ok=True)
    exists = CSV_FILE.exists()
    with CSV_FILE.open("a", newline="", encoding="utf-8-sig") as f:
        writer = csv.DictWriter(f, fieldnames=FIELDS)
        if not exists:
            writer.writeheader()
        writer.writerow(row)


def report() -> None:
    if not CSV_FILE.exists():
        print("Ainda não há resultados.")
        return
    with CSV_FILE.open(encoding="utf-8-sig", newline="") as f:
        rows = list(csv.DictReader(f))
    print(f"Registros: {len(rows)} | arquivo: {CSV_FILE}")
    for model in sorted({r["modelo"] for r in rows}):
        group = [r for r in rows if r["modelo"] == model and r["status"] == "ok"]
        if not group:
            print(f"{model}: sem gerações concluídas")
            continue
        nums = lambda key: [float(r[key]) for r in group if r.get(key)]
        scores = nums("assertividade_0_100")
        print(
            f"{model}: {len(group)} respostas; média tokens={sum(nums('tokens_total'))/len(group):.1f}; "
            f"tempo={sum(nums('tempo_total_s'))/len(group):.1f}s; "
            f"linhas={sum(nums('linhas_codigo'))/len(group):.1f}; "
            f"complexidade estimada={sum(nums('complexidade_ciclomatica_estimada'))/len(group):.1f}; "
            f"assertividade={sum(scores)/len(scores):.1f}% ({len(scores)} avaliações)" if scores else
            f"{model}: {len(group)} respostas; média tokens={sum(nums('tokens_total'))/len(group):.1f}; "
            f"tempo={sum(nums('tempo_total_s'))/len(group):.1f}s; "
            f"linhas={sum(nums('linhas_codigo'))/len(group):.1f}; "
            f"complexidade estimada={sum(nums('complexidade_ciclomatica_estimada'))/len(group):.1f}; "
            "assertividade ainda não preenchida"
        )


def recheck_saved_codes() -> None:
    """Recompile and rerun saved successful generations with current Java runner."""
    if not CSV_FILE.exists():
        print("Ainda não há resultados para revalidar.")
        return
    with CSV_FILE.open(encoding="utf-8-sig", newline="") as f:
        rows = list(csv.DictReader(f))
    checked = 0
    for row in rows:
        if row.get("compilou") != "sim" or not row.get("arquivo_codigo"):
            continue
        source = ROOT / row["arquivo_codigo"]
        if not source.exists():
            continue
        folder = source.parent / "build_recheck"
        compiled, ran, err = compile_and_run(source.read_text(encoding="utf-8"), folder)
        row["compilou"] = compiled
        row["executou"] = ran
        if err:
            row["erro"] = err
        elif compiled == "sim" and ran == "sim":
            row["erro"] = ""
        checked += 1
        print(f"[{row['problema_id']}] {row['modelo']}: compilou={compiled}, executou={ran}")
    temp_file = CSV_FILE.with_suffix(".tmp")
    with temp_file.open("w", encoding="utf-8-sig", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=FIELDS)
        writer.writeheader()
        writer.writerows(rows)
    temp_file.replace(CSV_FILE)
    print(f"Revalidados {checked} códigos salvos.")


def main() -> int:
    global OLLAMA_URL
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--models", nargs="+", default=DEFAULT_MODELS, help="nomes/tags exatas no Ollama")
    parser.add_argument("--limit", type=int, help="limita o número de problemas (teste piloto)")
    parser.add_argument("--report", action="store_true", help="resume o CSV existente")
    parser.add_argument("--recheck", action="store_true", help="recompila e reexecuta códigos já salvos")
    parser.add_argument("--url", default=OLLAMA_URL, help="endpoint local do Ollama")
    args = parser.parse_args()
    OLLAMA_URL = args.url
    if args.report:
        report()
        return 0
    if args.recheck:
        recheck_saved_codes()
        return 0
    system, problems = read_prompt_bank()
    if args.limit:
        problems = problems[:args.limit]
    print(f"Executando {len(problems)} problemas × {len(args.models)} modelos = {len(problems)*len(args.models)} chamadas")
    print("Para interromper com segurança, pressione Ctrl+C; registros concluídos ficam salvos no CSV.")
    for pid, title, prompt in problems:
        for model in args.models:
            print(f"[{pid}] {model} ...", flush=True)
            row = {key: "" for key in FIELDS}
            row.update({"problema_id": pid, "problema_titulo": title, "modelo": model,
                        "data_utc": datetime.now(timezone.utc).isoformat(), "status": "erro"})
            try:
                result, wall_s = call_ollama(model, system, prompt)
                response_text = result.get("message", {}).get("content", "")
                code = extract_code(response_text)
                if not response_text.strip() or not code.strip():
                    row["erro"] = "O modelo retornou conteúdo vazio; resposta não contabilizada como geração válida."
                    print(f"  erro: {row['erro']}")
                    write_row(row)
                    continue
                model_dir = OUT_DIR / pid / re.sub(r"[^A-Za-z0-9_.-]", "_", model)
                model_dir.mkdir(parents=True, exist_ok=True)
                code_file = model_dir / "Main.java"
                code_file.write_text(code, encoding="utf-8")
                (model_dir / "resposta_completa.txt").write_text(response_text, encoding="utf-8")
                (model_dir / "resposta_bruta.json").write_text(
                    json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8"
                )
                compiled, ran, err = compile_and_run(code, model_dir / "build")
                row.update({
                    "status": "ok", "tokens_entrada": result.get("prompt_eval_count", ""),
                    "tokens_saida": result.get("eval_count", ""),
                    "tokens_total": (result.get("prompt_eval_count", 0) or 0) + (result.get("eval_count", 0) or 0),
                    "tempo_total_s": round(wall_s, 3),
                    "tempo_prompt_s": round((result.get("prompt_eval_duration", 0) or 0) / 1e9, 3),
                    "tempo_geracao_s": round((result.get("eval_duration", 0) or 0) / 1e9, 3),
                    "linhas_codigo": len(code.splitlines()),
                    "complexidade_ciclomatica_estimada": estimate_cyclomatic(code),
                    "compilou": compiled, "executou": ran,
                    "arquivo_codigo": str(code_file.relative_to(ROOT)), "erro": err,
                })
                print(f"  ok | {row['tokens_total']} tokens | {wall_s:.1f}s | compilou={compiled}, executou={ran}")
            except (urllib.error.URLError, TimeoutError, OSError, json.JSONDecodeError, subprocess.TimeoutExpired) as exc:
                row["erro"] = str(exc)[:1500]
                print(f"  erro: {row['erro']}")
            except KeyboardInterrupt:
                print("\nInterrompido pelo usuário. Os registros anteriores foram mantidos.")
                return 130
            write_row(row)
    print(f"Concluído. Resultados: {OUT_DIR}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
