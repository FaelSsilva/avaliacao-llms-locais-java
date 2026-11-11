public class Main {

    static class Livro {
        private final String titulo;
        private final String autor;

        public Livro(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
        }

        public Livro copiar() {
            return new Livro(this.titulo, this.autor);
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien");
        Livro livro2 = livro1.copiar();

        livro1.titulo = "Novo Título";
        System.out.println("Livro 1: " + livro1.titulo);
        System.out.println("Livro 2: " + livro2.titulo);
    }
}
