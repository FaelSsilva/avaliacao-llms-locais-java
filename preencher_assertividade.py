from __future__ import annotations

import csv
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parent
RESULTADOS = ROOT / "resultados_ollama"
FICHA = ROOT / "ficha_assertividade.csv"
METRICAS = RESULTADOS / "metricas.csv"
RESUMO = ROOT / "resumo_assertividade.txt"

# Notas em (funcionalidade /70, OO /20, validações /10).
# A nota cheia só é atribuída após revisão do fonte e evidência de compilação.
# Abaixo estão as exceções encontradas na revisão dos 90 fontes.
parciais = {
    ("P01", "phi4-mini:3.8b"): ((55, 20, 10), "Faltam getters exigidos para título, autor e ano."),
    ("P01", "gemma3:4b"): ((70, 20, 5), "O construtor valida o ano, mas aceita título/autor vazios."),
    ("P02", "phi4-mini:3.8b"): ((60, 20, 10), "A cópia é independente, mas o main não altera o original para demonstrar isso."),
    ("P02", "gemma3:4b"): ((60, 20, 10), "A cópia é independente, mas o main não demonstra alteração do original."),
    ("P03", "mistral:7b-instruct"): ((70, 15, 10), "Contagem funciona; getTotalLivros() não é public."),
    ("P04", "mistral:7b-instruct"): ((60, 20, 10), "Implementa as transições e exceções, mas não demonstra tentativa inválida no main."),
    ("P05", "gemma3:4b"): ((45, 20, 10), "Usa id numérico e apresentar() imprime void em vez de retornar nome (identificador)."),
    ("P06", "phi4-mini:3.8b"): ((55, 20, 10), "podeEmprestar() não retorna boolean e o main não demonstra o limite atingido."),
    ("P06", "gemma3:4b"): ((55, 20, 10), "A lógica do limite funciona, mas a demonstração tenta excedê-lo sem capturar a exceção."),
    ("P06", "mistral:7b-instruct"): ((60, 20, 10), "A implementação protege o limite; a demonstração não chega a uma tentativa inválida."),
    ("P07", "mistral:7b-instruct"): ((70, 20, 0), "Rejeita valor diário zero, embora o enunciado aceite valores não negativos."),
    ("P08", "phi4-mini:3.8b"): ((60, 15, 10), "A implementação usa a interface, mas o main manipula Livro em vez de uma variável Emprestavel."),
    ("P11", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila: busca referencia identificador inexistente (titulo em vez de trecho)."),
    ("P11", "gemma3:4b"): ((0, 0, 0), "Não compila no registro; além disso, não modela Catalogo como solicitado e expõe campos."),
    ("P11", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: faltam imports usados pelo código."),
    ("P12", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: faltam imports de Map/HashMap."),
    ("P12", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: faltam imports de Map/HashMap."),
    ("P13", "gemma3:4b"): ((0, 15, 0), "cancelar() lança exceção quando a reserva está ativa e nunca muda o estado."),
    ("P14", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: faltam imports usados pela fila/exceção."),
    ("P14", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro; implementação também insere na cabeça (LIFO), não FIFO."),
    ("P15", "phi4-mini:3.8b"): ((0, 20, 10), "estaAtrasado está invertido: marca prazo futuro como atraso."),
    ("P15", "gemma3:4b"): ((55, 20, 10), "A regra está correta, mas a classe principal/demonstração não atende ao contrato Main e termina com data inválida."),
    ("P15", "mistral:7b-instruct"): ((0, 20, 10), "estaAtrasado está invertido: compara prazo futuro como atraso."),
    ("P17", "phi4-mini:3.8b"): ((60, 15, 10), "calcularTotal altera saldo e não retorna o total; contrato diverge da interface pedida."),
    ("P18", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: implementações reduzem visibilidade de enviar()."),
    ("P19", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: RegraMulta declara método abstrato sem ser abstrata."),
    ("P19", "gemma3:4b"): ((0, 0, 0), "Não compila no registro: subclasses acessam campo private da classe base."),
    ("P19", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: subclasses acessam campo private da classe base."),
    ("P20", "phi4-mini:3.8b"): ((45, 10, 0), "Busca/salvamento existem, mas RepositorioLivro é classe, não interface, e ISBN repetido sobrescreve."),
    ("P20", "gemma3:4b"): ((0, 0, 0), "Não compila no registro: falta import de Optional."),
    ("P21", "phi4-mini:3.8b"): ((70, 10, 10), "Validação está correta, mas Validador foi declarado como classe, não interface."),
    ("P21", "gemma3:4b"): ((0, 0, 0), "Não compila no registro; parseInt também não suporta ISBN-13 acima do limite de int."),
    ("P21", "mistral:7b-instruct"): ((55, 20, 10), "Validador está correto, mas o main só imprime exemplos e não chama validar()."),
    ("P22", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: faltam imports de List/ArrayList."),
    ("P23", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila: referências a catalogo/titulo inexistentes e estratégia não recebe livros."),
    ("P23", "gemma3:4b"): ((0, 0, 0), "Não compila no registro; as duas estratégias retornariam todos os livros sem filtrar."),
    ("P23", "mistral:7b-instruct"): ((0, 0, 0), "Não compila: estratégias referenciam lista livros inexistente."),
    ("P24", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: faltam imports de Map/HashMap."),
    ("P24", "gemma3:4b"): ((0, 10, 0), "Guarda duplicatas numa lista e não implementa adicionar/remover/quantidade em Devolucao."),
    ("P24", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro; TreeSet exige ordenação que Livro não fornece."),
    ("P25", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: nomes de construtor/métodos inconsistentes e referência a histórico inexistente."),
    ("P25", "gemma3:4b"): ((0, 10, 0), "Filtra por nome, não por identificador; retorna a lista interna e expõe campos."),
    ("P25", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: faltam imports; getHistorico ainda expõe lista interna."),
    ("P26", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: assinaturas de adicionarLivro não coincidem."),
    ("P26", "mistral:7b-instruct"): ((0, 15, 0), "Composição existe, mas adicionarLivro decrementa o total inicial de 100."),
    ("P27", "phi4-mini:3.8b"): ((70, 20, 0), "Classe imutável e getters corretos, mas não valida valores positivos."),
    ("P28", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: faltam imports de List/ArrayList."),
    ("P28", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: faltam imports de Map/List/HashMap/ArrayList."),
    ("P29", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro; condição de atraso também está invertida."),
    ("P29", "gemma3:4b"): ((0, 15, 0), "Retorna compareTo (-1/0/1), não quantidade de dias; sinal fica invertido para atraso."),
    ("P29", "mistral:7b-instruct"): ((0, 0, 0), "Não compila no registro: usa ChronoUnit sem import."),
    ("P30", "phi4-mini:3.8b"): ((0, 0, 0), "Não compila no registro: chama isAtivo inexistente; falta limite/estado do usuário."),
    ("P30", "gemma3:4b"): ((70, 10, 10), "Atualiza estados e verifica pré-condições antes; campos das entidades não são privados."),
    ("P30", "mistral:7b-instruct"): ((0, 15, 0), "Fluxo recursivo marca o livro antes da checagem do usuário; falha deixa estado alterado."),
}

with FICHA.open(newline="", encoding="utf-8-sig") as f:
    rows = list(csv.DictReader(f, delimiter=";"))

for row in rows:
    key = (row["problema_id"], row["modelo"])
    if row["compilou_registrado"] != "sim":
        parts, note = parciais.get(key, ((0, 0, 0), "Não compilou segundo o registro; nota zero."))
    else:
        parts, note = parciais.get(key, ((70, 20, 10), "Revisão do fonte: critérios principais atendidos; ver roteiro do problema."))
    f, oo, val = parts
    row["funcional_0_70"] = str(f)
    row["oo_0_20"] = str(oo)
    row["validacao_0_10"] = str(val)
    row["assertividade_0_100"] = str(f + oo + val)
    recorded = "compilou=" + row["compilou_registrado"] + ", executou=" + row["executou_registrado"]
    row["evidencia_observacao"] = recorded + "; " + note + " Avaliação por revisão estática, não por suíte unitária independente."

with FICHA.open("w", newline="", encoding="utf-8-sig") as f:
    writer = csv.DictWriter(f, fieldnames=rows[0].keys(), delimiter=";")
    writer.writeheader()
    writer.writerows(rows)

scores = {(r["problema_id"], r["modelo"]): r["assertividade_0_100"] for r in rows}
with METRICAS.open(newline="", encoding="utf-8-sig") as f:
    metrics = list(csv.DictReader(f))
for row in metrics:
    row["assertividade_0_100"] = scores[(row["problema_id"], row["modelo"])]
with METRICAS.open("w", newline="", encoding="utf-8-sig") as f:
    writer = csv.DictWriter(f, fieldnames=metrics[0].keys())
    writer.writeheader()
    writer.writerows(metrics)

summary = defaultdict(list)
for row in rows:
    summary[row["modelo"]].append(int(row["assertividade_0_100"]))
lines = ["ASSERTIVIDADE — REVISÃO DOS 90 CÓDIGOS", "Rubrica: funcionalidade 70 + OO 20 + validações 10.", "Método: revisão estática dos fontes + status compilou/executou previamente registrado. Não é suíte unitária independente.", "Regra: fonte marcado como não compilado recebe zero; limitações funcionais/estruturais recebem desconto e justificativa na ficha.", ""]
for model, values in summary.items():
    lines.append(f"{model}: média {sum(values)/len(values):.1f}/100; abaixo de 60: {sum(v < 60 for v in values)}/30")
RESUMO.write_text("\n".join(lines) + "\n", encoding="utf-8")
print("Fichas atualizadas: 90 notas atribuídas; resumo por modelo:")
print("\n".join(lines[4:]))
