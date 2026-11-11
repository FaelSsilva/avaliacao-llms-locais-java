# Experimento: geração de códigos OO para biblioteca

## Configuração comum

- Tema geral: sistema de biblioteca.
- Linguagem: Java, um único arquivo por problema.
- Modelos: `phi4-mini:3.8b`, `gemma3:4b`, `mistral:7b-instruct`.
- Técnica de prompt: role-playing.
- Cada problema é independente; não compartilhe classes nem contexto entre problemas.
- Gere uma resposta por modelo para cada problema (30 × 3 = 90 respostas).

## Prompt-base (usar como mensagem de sistema em todos os casos)

> Você é um engenheiro de software Java experiente e professor de Orientação a Objetos. Resolva o problema específico como um exercício pequeno, independente e autocontido. Use Java padrão, aplique encapsulamento e escolha abstrações simples que atendam ao enunciado. Não invente requisitos. Inclua validações e exceções somente quando solicitadas ou necessárias para impedir estados inválidos. Responda em português. Sua resposta deve conter somente o código-fonte Java em um único bloco de código, sem explicações antes ou depois. Não apresente raciocínio, análise ou paráfrase do enunciado. O arquivo deve compilar sozinho: declare exatamente uma classe pública de nível superior chamada `Main`; declare as classes auxiliares como classes aninhadas `static` dentro dela. Inclua um `main` curto com demonstrações dos comportamentos solicitados. Não use bibliotecas externas, entrada interativa, rede, banco de dados nem arquivos. Não inclua comentários extensos.

## Especificações (usar uma por vez como mensagem do usuário)

### P01 — Livro e encapsulamento
Crie `Main` com uma classe `Livro` que guarde título, autor e ano de publicação em campos privados. O construtor recebe os três valores. Não permita título ou autor vazios e ano menor que 1. Forneça getters e um método `resumo()` que retorne `Título — Autor (Ano)`. No `main`, crie e exiba um livro.

### P02 — Cópia de livro
Crie `Main` com a classe `Livro` (título e autor privados) e método `copiar()`, que devolve um novo objeto com os mesmos dados. Alterar o objeto original não deve alterar a cópia. Demonstre a independência no `main`.

### P03 — Contador de livros
Crie `Main` com a classe `Livro`. Conte quantos objetos foram criados usando um campo estático e exponha `getTotalLivros()`. O `main` deve criar três livros e exibir o total.

### P04 — Estado de disponibilidade
Crie `Main` com `Livro`, com título e estado de disponibilidade privados. Implemente `emprestar()` e `devolver()`. Não empreste um livro indisponível nem devolva um livro que já esteja disponível; nesses casos lance `IllegalStateException`. Demonstre operação válida e tratamento de uma tentativa inválida no `main`.

### P05 — Usuário da biblioteca
Crie `Main` com classe `Usuario` contendo nome e identificador privados. Valide valores não vazios no construtor, implemente getters e `apresentar()` que retorne `nome (identificador)`. Demonstre no `main`.

### P06 — Limite de empréstimos
Crie `Main` com `Usuario` que tenha nome, limite máximo e quantidade atual de empréstimos. Implemente `podeEmprestar()`, `registrarEmprestimo()` e `registrarDevolucao()`. Não exceda o limite nem permita quantidade negativa; use `IllegalStateException` para operação inválida. Demonstre o limite no `main`.

### P07 — Multa por atraso
Crie `Main` com classe `Multa` que receba dias de atraso e valor diário. Valide dias não negativos e valor diário não negativo. `calcularValor()` deve devolver o produto. Demonstre atraso zero e atraso positivo.

### P08 — Item emprestável por interface
Crie `Main` com interface `Emprestavel` (`emprestar`, `devolver`, `isDisponivel`) e classe `Livro` que a implemente. A classe deve impedir empréstimo duplicado e devolução indevida com `IllegalStateException`. Demonstre usando uma variável do tipo interface.

### P09 — Herança de publicação
Crie `Main` com classe base `Publicacao` (título e ano) e subclasses `Livro` (autor) e `Revista` (edição). Campos privados, getters, construtores e método sobrescrito `descricao()`. Demonstre polimorfismo com uma lista de publicações.

### P10 — Pessoa e usuário
Crie `Main` com classe base abstrata `Pessoa` (nome) e subclasses `Usuario` (identificador) e `Bibliotecario` (matrícula). Cada subclasse deve implementar `tipo()`. Demonstre chamada polimórfica.

### P11 — Catálogo pesquisável
Crie `Main` com `Livro` (título e autor) e `Catalogo` que armazene livros numa lista. Implemente busca por trecho do título sem diferenciar maiúsculas/minúsculas. Retorne uma nova lista com os resultados e demonstre busca com dois livros.

### P12 — Remoção do catálogo
Crie `Main` com `Livro` identificado por ISBN e título, e `Catalogo` com métodos para adicionar e remover pelo ISBN. Não permita ISBN duplicado; remoção de ISBN inexistente retorna `false`. Demonstre adição, duplicidade e remoção.

### P13 — Reserva de livro
Crie `Main` com `Livro` e `Reserva`. Uma reserva associa usuário e livro e tem estado ativa/cancelada. Implemente `cancelar()` e impeça cancelar duas vezes com `IllegalStateException`. Demonstre criação e cancelamento.

### P14 — Fila de reservas
Crie `Main` com `FilaReservas` que mantenha nomes de usuários em ordem FIFO. Implemente `reservar`, `proximo` e `estaVazia`. `proximo` deve lançar `NoSuchElementException` quando vazia. Demonstre duas reservas e a ordem de atendimento.

### P15 — Empréstimo e datas
Crie `Main` com classe `Emprestimo` usando `LocalDate`, com data de retirada e prazo de devolução. Valide que o prazo não anteceda a retirada. Implemente `estaAtrasado(dataAtual)` e demonstre empréstimo no prazo e atrasado.

### P16 — Exceção de livro indisponível
Crie `Main` com exceção própria `LivroIndisponivelException` e classe `Livro` com operação `emprestar()`. A operação deve lançar a exceção própria se o livro já estiver emprestado. Demonstre o tratamento da exceção.

### P17 — Pagamento de multa por interface
Crie `Main` com interface `Pagavel` (`calcularTotal`, `pagar`) e classe `Multa` que a implemente. Após paga, a multa não pode ser paga de novo; lance `IllegalStateException`. Demonstre através da interface.

### P18 — Formas de notificação
Crie `Main` com interface `Notificador` contendo `enviar(String mensagem)`. Implemente `NotificadorEmail` e `NotificadorConsole`, cada um produzindo uma representação textual diferente da notificação. Demonstre ambos numa lista polimórfica, sem enviar mensagens reais.

### P19 — Cálculo de multa polimórfico
Crie `Main` com classe abstrata `RegraMulta` e método abstrato `calcular(int diasAtraso)`. Implemente regra fixa de R$ 2 por dia e regra com teto de R$ 20. Valide atraso não negativo e demonstre ambas via referência da classe base.

### P20 — Repositório em memória
Crie `Main` com interface `RepositorioLivro` (`salvar`, `buscarPorIsbn`) e classe `RepositorioLivroMemoria` baseada em `Map`. ISBN deve ser único; retorne `Optional<Livro>` na busca. Demonstre inclusão e consulta.

### P21 — Validador de ISBN
Crie `Main` com interface `Validador<String>` e implementação `ValidadorIsbnSimples`. Para este exercício, considere válido apenas ISBN-13 formado por exatamente 13 algarismos. Implemente `validar` e demonstre entradas válidas e inválidas. Não implemente o dígito verificador.

### P22 — Ordenação de livros
Crie `Main` com `Livro` (título e autor) que implemente `Comparable<Livro>` ordenando pelo título sem diferenciar maiúsculas/minúsculas. Demonstre ordenação de uma lista.

### P23 — Busca com estratégia
Crie `Main` com interface `EstrategiaBusca` e duas implementações: busca exata por título e busca por trecho do título, ambas sem diferenciar maiúsculas/minúsculas. Um `Catalogo` recebe a estratégia e busca numa lista de livros. Demonstre as duas estratégias.

### P24 — Carrinho de devoluções
Crie `Main` com classe `Devolucao` que armazene livros devolvidos numa coleção sem duplicatas. Implemente adicionar, remover e quantidade. Demonstre que adicionar o mesmo título/ISBN duas vezes mantém um exemplar. Use ISBN como identidade.

### P25 — Histórico de empréstimos
Crie `Main` com `Emprestimo` contendo usuário, ISBN e data. Classe `Historico` deve adicionar empréstimos e retornar histórico de um usuário em nova lista. Demonstre filtragem por identificador.

### P26 — Biblioteca agregadora
Crie `Main` com `Biblioteca` que possua nome e um `Catalogo`. Demonstre composição: criar biblioteca, adicionar livros por meio do catálogo e consultar a quantidade. Mantenha campos encapsulados.

### P27 — Configuração imutável
Crie `Main` com classe imutável `PoliticaEmprestimo` contendo prazo em dias e limite por usuário. Campos finais privados, validação positiva, getters e nenhum setter. Demonstre leitura dos valores.

### P28 — Estatísticas do catálogo
Crie `Main` com `Livro` que tenha gênero e `Catalogo` que calcule quantidade total e quantidade por gênero. Evite expor diretamente a coleção interna. Demonstre com livros de dois gêneros.

### P29 — Processador de devolução
Crie `Main` com interface `RegraDevolucao` e implementação que recebe data prevista e data real, retornando dias de atraso (zero se devolvido no prazo). Use `LocalDate` e demonstre os dois casos.

### P30 — Serviço de empréstimo
Crie `Main` com classes `Livro`, `Usuario` e `ServicoEmprestimo`. O serviço deve emprestar livro disponível ao usuário abaixo do limite e atualizar ambos os estados; se qualquer condição falhar, não altere nenhum estado e lance `IllegalStateException`. Demonstre sucesso e falha.

## Registro de avaliação da assertividade

O script grava resultados técnicos automaticamente, mas isso não prova que o comportamento está correto. Para cada resposta, execute os casos do enunciado e registre `assertividade` de 0 a 100 no CSV de resultados. Use a mesma rubrica em todas: correção funcional (70 pontos), requisitos de OO (20), tratamento/validações exigidos (10). Se usar testes automatizados, registre também aprovado/total e mantenha os testes junto dos artefatos.
