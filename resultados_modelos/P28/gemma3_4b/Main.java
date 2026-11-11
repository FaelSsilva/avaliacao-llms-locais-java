public class Main {

    static class Livro {
        private String genero;

        public Livro(String genero) {
            this.genero = genero;
        }

        public String getGenero() {
            return genero;
        }
    }

    static class Catalogo {
        private java.util.List<Livro> livros;

        public Catalogo() {
            this.livros = new java.util.ArrayList<>();
        }

        public void adicionarLivro(Livro livro) {
            this.livros.add(livro);
        }

        public int calcularQuantidadeTotal() {
            return this.livros.size();
        }

        public int calcularQuantidadePorGenero(String genero) {
            int count = 0;
            for (Livro livro : this.livros) {
                if (livro.getGenero().equals(genero)) {
                    count++;
                }
            }
            return count;
        }
    }

    public static void main(String[] args) {
        Catalogo catalogo = new Catalogo();
        Livro livro1 = new Livro("Fantasia");
        Livro livro2 = new Livro("Ficção Científica");
        Livro livro3 = new Livro("Fantasia");
        Livro livro4 = new Livro("Romance");

        catalogo.adicionarLivro(livro1);
        catalogo.adicionarLivro(livro2);
        catalogo.adicionarLivro(livro3);
        catalogo.adicionarLivro(livro4);

        System.out.println("Quantidade total de livros: " + catalogo.calcularQuantidadeTotal());
        System.out.println("Quantidade de livros de Fantasia: " + catalogo.calcularQuantidadePorGenero("Fantasia"));
        System.out.println("Quantidade de livros de Ficção Científica: " + catalogo.calcularQuantidadePorGenero("Ficção Científica"));
        System.out.println("Quantidade de livros de Romance: " + catalogo.calcularQuantidadePorGenero("Romance"));
    }
}
