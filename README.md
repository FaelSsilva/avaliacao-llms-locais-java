# Avaliação de modelos locais na geração de código Java orientado a objetos

Este projeto reúne os programas, prompts, dados e resultados de um experimento acadêmico que compara três modelos de linguagem executados localmente ao resolver exercícios pequenos e independentes de orientação a objetos (OO) em Java.

Cada um dos 30 problemas foi enviado aos três modelos, uma vez por modelo, totalizando 90 respostas. Os mesmos enunciados e as mesmas configurações de geração foram usados nas chamadas. O problema é a unidade pareada da análise estatística.

## Motivo e objetivo do trabalho

Modelos de linguagem podem ajudar a produzir código, mas a utilidade de uma resposta depende de ela atender ao que foi pedido, compilar e funcionar. Por isso, este trabalho compara, em condições iguais, três modelos locais na resolução de exercícios introdutórios de orientação a objetos em Java. A execução local também permite observar o desempenho dos modelos em um computador de recursos limitados, sem GPU dedicada.

O objetivo é identificar como os modelos se comportam em relação à aderência aos requisitos, compilação e execução, além de registrar custo de geração (tokens e tempo), tamanho do código e uma estimativa de complexidade. Os resultados servem como evidência para a atividade acadêmica; não pretendem estabelecer qual modelo é melhor para todo tipo de programação.

O estudo foi desenvolvido no contexto da disciplina de Desenvolvimento Orientado a Objetos (DOO), ministrada pelo professor [@filipefernandesphd](https://github.com/filipefernandesphd).

## Modelos avaliados

- `phi4-mini:3.8b`
- `gemma3:4b`
- `mistral:7b-instruct`

A seleção considerou a execução local viável no computador disponível durante o estudo (AMD Ryzen 7 5800U, 16 GB de RAM, sem GPU dedicada) e a disponibilidade no Ollama. Trata-se de uma amostra por conveniência; os resultados não representam todos os modelos disponíveis e não permitem separar o efeito do tamanho do modelo do efeito de sua família.

## Estrutura do repositório

| Arquivo ou pasta | Conteúdo |
|---|---|
| `prompts_30_biblioteca.md` | Prompt-base de role-playing e as 30 especificações utilizadas. |
| `medir_ollama.py` | Envia os prompts à API local do Ollama, salva as respostas e registra métricas. |
| `resultados_ollama/metricas.csv` | Registro das 90 chamadas, com tokens, duração, linhas, complexidade estimada e resultados de compilação, execução e assertividade. |
| `resultados_ollama/P01` a `P30/` | Códigos Java gerados e respostas completas e brutas de cada modelo para cada problema. |
| `analise_estatistica_pareada.py` | Script da análise estatística pareada. |
| `resultados_ollama/analise_estatistica_pareada.json` e `.md` | Resultados completos e resumo legível da análise estatística. |
| `gerar_graficos.py` | Script para gerar gráficos a partir dos dados. |
| `ficha_assertividade.csv` | Notas e justificativas da revisão dos códigos. |
| `roteiro_testes_assertividade.csv` e `Assertividade.txt` | Critérios e síntese da avaliação de assertividade. |
| `Relatorio-overleaf.zip` | Projeto completo do relatório para o Overleaf. O arquivo contém os fontes LaTeX, referências, dados complementares e figuras. |
| `MANIFEST.json` | Lista de arquivos e hashes SHA-256 para conferência de integridade. |

O projeto do relatório e as figuras estão dentro de `Relatorio-overleaf.zip`; eles não aparecem como pastas separadas na raiz deste repositório.

## Ambiente registrado

- Windows; AMD Ryzen 7 5800U; 16 GB de RAM; sem GPU dedicada.
- Python 3.14.10.
- JDK 21.0.12.1, usado para compilar e executar os códigos Java.
- Cliente Ollama 0.40.16.
- Modelos locais conforme as tags acima.

A versão do servidor Ollama durante a coleta não foi registrada. Por esse motivo, uma execução posterior pode não reproduzir exatamente os mesmos tempos ou saídas.

## Reproduzir a coleta

É necessário ter Python, JDK e Ollama instalados, iniciar o serviço Ollama e baixar os três modelos. No PowerShell, na pasta raiz do projeto:

```powershell
ollama pull phi4-mini:3.8b
ollama pull gemma3:4b
ollama pull mistral:7b-instruct
python medir_ollama.py
```

A execução completa faz 90 chamadas locais e pode demorar. Para limitar a coleta ao primeiro problema:

```powershell
python medir_ollama.py --limit 1
```

Para gerar um resumo dos registros já coletados:

```powershell
python medir_ollama.py --report
```

O script preserva as respostas e registra falhas. A métrica de compilação informa se `javac` aceitou o fonte; execução sem erro também não prova que todos os requisitos foram atendidos.

## Reproduzir análise e gráficos

Com o CSV de métricas preenchido:

```powershell
python analise_estatistica_pareada.py resultados_ollama/metricas.csv
python gerar_graficos.py
```

A análise trata cada problema como um bloco com três respostas relacionadas. Usa Friedman por permutação para medidas numéricas, Q de Cochran para resultados binários, testes pareados pós-hoc e correção de Holm. O script estatístico usa somente a biblioteca padrão do Python. Os arquivos JSON e Markdown resultantes são gravados junto ao CSV de entrada.

## Como interpretar as métricas

- **Assertividade (0–100):** nota de aderência atribuída por revisão estática segundo a rubrica registrada em `roteiro_testes_assertividade.csv` e `ficha_assertividade.csv`. Não é uma taxa de aprovação em suíte independente de testes unitários.
- **Compilação e execução:** resultados técnicos medidos pelo script; não equivalem à correção completa do comportamento pedido.
- **Complexidade ciclomática:** estimativa heurística por contagem textual, não cálculo de AST.
- **Tokens e tempo:** medidas da execução local e dependentes da máquina, versões do runtime e carga do sistema.

## Procedência dos problemas e dos prompts

Os enunciados foram elaborados especificamente para este estudo, com apoio de IA generativa na redação inicial; não foram transcritos de um benchmark público. O domínio de biblioteca mantém o contexto geral constante, enquanto os conceitos e comportamentos variam entre os problemas.

A matriz `artefatos/rastreabilidade_especificacoes.csv`, incluída no projeto do relatório, relaciona os IDs aos conceitos de OO e ao comportamento esperado. O arquivo `artefatos/revisao_e_procedencia.md`, também incluído no ZIP do Overleaf, descreve as decisões, o processo de revisão de consistência e as ambiguidades registradas.

## Relatório e citação

Repositório: https://github.com/FaelSsilva/avaliacao-llms-locais-java

O projeto-fonte do relatório está em `Relatorio-overleaf.zip`. Para utilizá-lo no Overleaf, importe esse ZIP e compile o documento. O relatório cita este repositório como local de acesso aos artefatos.

Referência sugerida:

> Silva, Rafael P. (2026). *Avaliação de modelos de linguagem locais na geração de código Java orientado a objetos* [software e artefatos de pesquisa]. GitHub.
