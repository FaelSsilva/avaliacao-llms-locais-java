public class Main {

    static class Livro {
        private String titulo;
        private String autor;
        private static int totalLivros = 0;

        public Livro(String titulo, String autor) {
            this.titulo = titulo;
            this.autor = autor;
            totalLivros++;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getAutor() {
            return autor;
        }

        public static int getTotalLivros() {
            return totalLivros;
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien");
        Livro livro2 = new Livro("1984", "George Orwell");
        Livro livro3 = new Livro("Dom Quixote", "Miguel de Cervantes");

        System.out.println("Total de livros: " + Livro.getTotalLivros());
    }
}
