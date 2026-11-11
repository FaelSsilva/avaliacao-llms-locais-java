public class Main {

    static class Livro {
        private String titulo;
        private String autor;
        private int ano;

        public Livro(String titulo, String autor, int ano) {
            this.titulo = titulo;
            this.autor = autor;
            if (ano < 1) {
                throw new IllegalArgumentException("O ano deve ser maior ou igual a 1.");
            }
            this.ano = ano;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getAutor() {
            return autor;
        }

        public int getAno() {
            return ano;
        }

        public String resumo() {
            return titulo + " — " + autor + " (" + ano + ")";
        }
    }

    public static void main(String[] args) {
        Livro livro = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien", 1954);
        System.out.println(livro.resumo());
    }
}
