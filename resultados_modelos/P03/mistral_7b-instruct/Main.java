public class Main {

    static class Livro {
        private static int total = 0;
        private final String titulo;
        private final String autor;

        Livro(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
            total++;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getAutor() {
            return autor;
        }

        static int getTotalLivros() {
            return total;
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien");
        Livro livro2 = new Livro("Fundação", "Isaac Asimov");
        Livro livro3 = new Livro("Duna", "Frank Herbert");

        System.out.println("Total de livros criados: " + Livro.getTotalLivros());
    }
}
