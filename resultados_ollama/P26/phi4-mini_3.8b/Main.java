public class Main {
    public static class Biblioteca {
        private String nome;
        private Catalogo catalogo;

        public Biblioteca(String nome) {
            this.nome = nome;
            this.catalogo = new Catalogo();
        }

        public void adicionarLivro(String livro) {
            catalogo.adicionarLivro(livro);
        }

        public int consultarQuantidadeLivros() {
            return catalogo.consultarQuantidadeLivros();
        }

        public String getNome() {
            return nome;
        }
    }

    public static class Catalogo {
        private int quantidadeLivros = 0;

        public void adicionarLivro() {
            quantidadeLivros++;
        }

        public int consultarQuantidadeLivros() {
            return quantidadeLivros;
        }
    }

    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
        biblioteca.adicionarLivro();
        biblioteca.adicionarLivro();
        System.out.println("Quantidade de livros na " + biblioteca.getNome() + ": " + biblioteca.consultarQuantidadeLivros());
    }
}
