public class Main {
    public static class Livro {
        private String genero;

        public Livro(String genero) {
            this.genero = genero;
        }

        public String getGenero() {
            return genero;
        }
    }

    public static class Catalogo {
        private List<Livro> livros = new ArrayList<>();

        public void adicionarLivro(Livro livro) {
            livros.add(livro);
        }

        public int calcularQuantidadeTotal() {
            return livros.size();
        }

        public int calcularQuantidadePorGenero(String genero) {
            int quantidade = 0;
            for (Livro livro : livros) {
                if (livro.getGenero().equalsIgnoreCase(genero)) {
                    quantidade++;
                }
            }
            return quantidade;
        }
    }

    public static void main(String[] args) {
        Catalogo catalogo = new Catalogo();
        catalogo.adicionarLivro(new Livro("Ficção"));
        catalogo.adicionarLivro(new Livro("Ficção"));
        catalogo.adicionarLivro(new Livro("Não-Ficção"));

        System.out.println("Quantidade total de livros: " + catalogo.calcularQuantidadeTotal());
        System.out.println("Quantidade de livros de Ficção: " + catalogo.calcularQuantidadePorGenero("Ficção"));
        System.out.println("Quantidade de livros de Não-Ficção: " + catalogo.calcularQuantidadePorGenero("Não-Ficção"));
    }
}
