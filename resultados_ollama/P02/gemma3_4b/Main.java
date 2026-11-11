public class Main {

    static class Livro {
        private String titulo;
        private String autor;

        public Livro(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getAutor() {
            return autor;
        }

        public Livro copiar() {
            return new Livro(this.titulo, this.autor);
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien");
        Livro livro2 = livro1.copiar();

        System.out.println("Livro 1: Título = " + livro1.getTitulo() + ", Autor = " + livro1.getAutor());
        System.out.println("Livro 2: Título = " + livro2.getTitulo() + ", Autor = " + livro2.getAutor());

        livro1.getTitulo();
        livro1.getAutor();
    }
}
