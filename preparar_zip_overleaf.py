#!/usr/bin/env python3
"""Empacota a pasta overleaf_upload_pronto sem alterar suas fontes."""

from pathlib import Path
import zipfile

ROOT = Path(__file__).resolve().parent
PROJECT = ROOT / "overleaf_upload_pronto"
ARCHIVE = ROOT / "relatorio_sbc_completo_overleaf.zip"
REQUIRED = {
    "main.tex",
    "sbc-template.sty",
    "sbc.bst",
    "referencias.bib",
    "figuras/assertividade.pdf",
    "artefatos/metricas.csv",
    "artefatos/prompts_30_biblioteca.md",
}


def main() -> None:
    if not PROJECT.is_dir():
        raise SystemExit(f"Pasta do Overleaf não encontrada: {PROJECT}")
    with zipfile.ZipFile(ARCHIVE, "w", compression=zipfile.ZIP_DEFLATED) as archive:
        for path in sorted(PROJECT.rglob("*")):
            if path.is_file():
                archive.write(path, path.relative_to(PROJECT).as_posix())
    with zipfile.ZipFile(ARCHIVE) as archive:
        broken = archive.testzip()
        missing = REQUIRED - set(archive.namelist())
    if broken or missing:
        raise SystemExit(f"ZIP inválido: arquivo corrompido={broken}; faltando={sorted(missing)}")
    print(f"ZIP validado: {ARCHIVE} ({len(archive.namelist())} arquivos)")


if __name__ == "__main__":
    main()
