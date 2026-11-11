# Análise estatística pareada

Problemas pareados: 30. Nível α=0,05.
Friedman por permutação (20.000; semente 20261111); p de Friedman ajustados por Holm entre cinco medidas.

## Testes globais numéricos

| Medida | Friedman χ²(2) | p permutação | p Holm | Kendall W |
|---|---:|---:|---:|---:|
| Assertividade | 7.435 | 0.023249 | 0.023249 | 0.124 |
| Tokens | 45.867 | 4.9998e-05 | 0.00024999 | 0.764 |
| Tempo (s) | 52.267 | 4.9998e-05 | 0.00024999 | 0.871 |
| Linhas | 16.957 | 0.00024999 | 0.00074996 | 0.283 |
| Complexidade estimada | 12.274 | 0.0020499 | 0.0040998 | 0.205 |

## Desfechos binários

| Desfecho | Cochran Q(2) | p | p Holm |
|---|---:|---:|---:|
| compilou | 5.375 | 0.068051 | 0.1361 |
| executou | 2.333 | 0.3114 | 0.3114 |

## Comparações numéricas pós-hoc

p de Wilcoxon com aproximação normal e correção de continuidade; ajuste de Holm global nas 15 comparações.

| Métrica | Modelo A | Modelo B | n não-zero | p | p Holm | Efeito rank-biserial |
|---|---|---|---:|---:|---:|---:|
| Assertividade | phi4-mini:3.8b | gemma3:4b | 21 | 0.047838 | 0.22396 | -0.494 |
| Assertividade | phi4-mini:3.8b | mistral:7b-instruct | 17 | 0.55264 | 1 | -0.170 |
| Assertividade | gemma3:4b | mistral:7b-instruct | 21 | 0.044791 | 0.22396 | 0.502 |
| Tokens | phi4-mini:3.8b | gemma3:4b | 30 | 3.3425e-06 | 4.3452e-05 | -0.974 |
| Tokens | phi4-mini:3.8b | mistral:7b-instruct | 30 | 1.8242e-06 | 2.7363e-05 | -1.000 |
| Tokens | gemma3:4b | mistral:7b-instruct | 30 | 0.00015397 | 0.0015397 | -0.794 |
| Tempo (s) | phi4-mini:3.8b | gemma3:4b | 30 | 3.6943e-06 | 4.4332e-05 | -0.970 |
| Tempo (s) | phi4-mini:3.8b | mistral:7b-instruct | 30 | 1.8254e-06 | 2.7363e-05 | -1.000 |
| Tempo (s) | gemma3:4b | mistral:7b-instruct | 30 | 3.6943e-06 | 4.4332e-05 | -0.970 |
| Linhas | phi4-mini:3.8b | gemma3:4b | 29 | 0.0022834 | 0.020551 | -0.651 |
| Linhas | phi4-mini:3.8b | mistral:7b-instruct | 28 | 0.4659 | 1 | -0.160 |
| Linhas | gemma3:4b | mistral:7b-instruct | 28 | 0.0027338 | 0.02187 | 0.650 |
| Complexidade estimada | phi4-mini:3.8b | gemma3:4b | 16 | 0.016434 | 0.098602 | -0.684 |
| Complexidade estimada | phi4-mini:3.8b | mistral:7b-instruct | 16 | 0.37454 | 1 | -0.250 |
| Complexidade estimada | gemma3:4b | mistral:7b-instruct | 20 | 0.0096392 | 0.067475 | 0.638 |

## Comparações binárias pós-hoc

McNemar exato; ajuste de Holm global nas seis comparações.

| Desfecho | Modelo A | Modelo B | Discordâncias A→B | Discordâncias B→A | p | p Holm |
|---|---|---|---:|---:|---:|---:|
| compilou | phi4-mini:3.8b | gemma3:4b | 3 | 10 | 0.092285 | 0.55371 |
| compilou | phi4-mini:3.8b | mistral:7b-instruct | 4 | 5 | 1 | 1 |
| compilou | gemma3:4b | mistral:7b-instruct | 8 | 2 | 0.10938 | 0.55371 |
| executou | phi4-mini:3.8b | gemma3:4b | 5 | 10 | 0.30176 | 1 |
| executou | phi4-mini:3.8b | mistral:7b-instruct | 4 | 5 | 1 | 1 |
| executou | gemma3:4b | mistral:7b-instruct | 8 | 4 | 0.3877 | 1 |
