public class Main {
    static class Biblioteca {
        private String nome;
        private Catalogo catalogo;

        public Biblioteca(String nome) {
            this.nome = nome;
            this.catalogo = new Catalogo();
        }

        public void adicionarLivro(String titulo, String autor) {
            catalogo.adicionarLivro(titulo, autor);
        }

        public int obterQuantidadeLivros() {
            return catalogo.obterQuantidadeLivros();
        }
    }

    static class Catalogo {
        private java.util.ArrayList<Livro> livros = new java.util.ArrayList<>();

        public void adicionarLivro(String titulo, String autor) {
            Livro livro = new Livro(titulo, autor);
            livros.add(livro);
        }

        public int obterQuantidadeLivros() {
            return livros.size();
        }
    }

    static class Livro {
        private String titulo;
        private String autor;

        public Livro(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
        }
    }

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Central");
        biblioteca.adicionarLivro("O Senhor dos Anéis", "J.R.R. Tolkien");
        biblioteca.adicionarLivro("Dom Quixote", "Miguel de Cervantes");
        System.out.println("Quantidade de livros na biblioteca: " + biblioteca.obterQuantidadeLivros());
    }
}
