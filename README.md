
# Avaliação de modelos locais na geração de código Java orientado a objetos

Este repositório reúne os prompts, os códigos gerados, os dados de coleta, os scripts de análise e os gráficos de um experimento acadêmico sobre geração de código Java para problemas introdutórios de orientação a objetos (OO).

Foram elaboradas 30 especificações independentes. Cada uma foi enviada uma vez a três modelos locais, totalizando 90 respostas. O problema é a unidade pareada da análise estatística.

## Motivo e objetivo

Modelos de linguagem podem ajudar a produzir código, mas uma resposta precisa atender aos requisitos, compilar e funcionar para ser útil. Este estudo compara três modelos em condições iguais e registra aderência aos enunciados, compilação, execução, tokens, tempo, linhas de código e complexidade estimada. A execução local também permite observar os modelos em um computador sem GPU dedicada.

O trabalho foi desenvolvido no contexto da disciplina de Desenvolvimento Orientado a Objetos (DOO), ministrada pelo professor [@filipefernandesphd](https://github.com/filipefernandesphd).

## Modelos avaliados

- `phi4-mini:3.8b`
- `gemma3:4b`
- `mistral:7b-instruct`

A seleção considerou a disponibilidade no Ollama e a viabilidade de execução no computador utilizado (AMD Ryzen 7 5800U, 16 GB de RAM e sem GPU dedicada). É uma amostra por conveniência: os resultados não representam todos os modelos disponíveis e não permitem separar o efeito da família do efeito do tamanho.

## Organização do repositório

| Pasta ou arquivo | Conteúdo |
|---|---|
| `prompts/` | Prompt-base de role-playing e as 30 especificações utilizadas. |
| `scripts/medicao/` | Script que envia os prompts ao Ollama, guarda as respostas e registra as métricas. |
| `scripts/analise/` | Análise estatística pareada e scripts de avaliação de assertividade. |
| `scripts/graficos/` | Scripts que geram os gráficos a partir dos resultados. |
| `resultados_modelos/P01` a `P30/` | Os 90 códigos Java, as respostas completas e as respostas brutas dos três modelos. |
| `resultados_modelos/metricas.csv` | 90 registros com tokens, tempos, linhas, complexidade estimada, compilação, execução e assertividade. |
| `resultados_modelos/analise_estatistica_pareada.json` e `.md` | Resultados completos e resumo legível da análise estatística. |
| `avaliacao/` | Ficha de assertividade, roteiro de avaliação e síntese das notas. |
| `graficos/` | Gráficos e tabela-resumo produzidos a partir dos dados. |
| `relatorio/Projeto_Overleaf.zip` | Projeto completo do relatório para importar no Overleaf, incluindo fontes, referências, dados complementares e figuras. |
| `MANIFEST.json` | Arquivos do repositório com tamanho e hash SHA-256 para conferência de integridade. |

## Ambiente registrado

- Windows; AMD Ryzen 7 5800U; 16 GB de RAM; sem GPU dedicada.
- Python 3.14.10.
- JDK 21.0.12.1, utilizado para compilar e executar os códigos Java.
- Cliente Ollama 0.40.16.
- Modelos conforme as tags listadas acima.

A versão do servidor Ollama não foi registrada durante a coleta. Uma execução posterior pode, por isso, apresentar tempos ou respostas diferentes.

## Reproduzir a coleta

Instale Python, JDK e Ollama, inicie o Ollama e baixe os modelos. Execute os comandos no PowerShell, a partir da raiz do repositório:

```powershell
ollama pull phi4-mini:3.8b
ollama pull gemma3:4b
ollama pull mistral:7b-instruct
python scripts/medicao/medir_ollama.py
```

A coleta completa faz 90 chamadas e pode demorar. Para executar somente o primeiro problema:

```powershell
python scripts/medicao/medir_ollama.py --limit 1
```

Para resumir os registros já existentes:

```powershell
python scripts/medicao/medir_ollama.py --report
```

O script conserva as respostas e registra falhas. Compilar ou executar sem erro não comprova que todos os requisitos foram atendidos.

## Reproduzir análise e gráficos

Para recalcular a análise estatística e os gráficos:

```powershell
python scripts/analise/analise_estatistica_pareada.py resultados_modelos/metricas.csv
python scripts/graficos/gerar_graficos.py
python scripts/graficos/gerar_grafico_assertividade_pdf.py
```

A análise trata cada problema como um bloco com três respostas relacionadas. Usa Friedman por permutação para medidas numéricas, Q de Cochran para resultados binários, testes pareados pós-hoc e correção de Holm. Os arquivos JSON e Markdown são gravados em `resultados_modelos/`; os gráficos são gravados em `graficos/`.

## Como interpretar as métricas

- **Assertividade (0–100):** nota de aderência atribuída por revisão estática, segundo a rubrica em `avaliacao/`. Não é uma taxa de aprovação em uma suíte independente de testes unitários.
- **Compilação e execução:** resultados técnicos registrados pela coleta; não garantem que todos os requisitos comportamentais foram atendidos.
- **Complexidade ciclomática:** estimativa heurística por contagem textual, não calculada sobre uma AST completa.
- **Tokens, tempo e linhas:** medidas da execução local, dependentes da máquina, das versões do runtime e da carga do sistema.

## Procedência dos problemas e dos prompts

As especificações foram elaboradas para este estudo, com apoio de IA generativa na redação inicial; não foram copiadas de um benchmark público. O domínio de biblioteca foi mantido para reduzir variações de vocabulário e contexto, enquanto os conceitos e comportamentos solicitados variam entre os problemas. O arquivo `prompts/prompts_30_biblioteca.md` preserva o prompt-base e os enunciados utilizados.

O projeto-fonte do relatório, dentro de `relatorio/Projeto_Overleaf.zip`, registra a justificativa da linguagem Java, dos modelos e do role-playing, além do processo de revisão dos prompts e da rastreabilidade das especificações.

## Relatório e citação

Repositório: <https://github.com/FaelSsilva/avaliacao-llms-locais-java>

Para consultar ou editar o relatório no Overleaf, importe `relatorio/Projeto_Overleaf.zip`. O relatório inclui a referência a este repositório como local de acesso aos artefatos.

Referência sugerida:

> Silva, Rafael P. (2026). *Avaliação de modelos de linguagem locais na geração de código Java orientado a objetos* [software e artefatos de pesquisa]. GitHub.
