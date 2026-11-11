<<<<<<< HEAD
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
=======
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
>>>>>>> 824e5563d1353ed0c27db8282105b939af927bd5
