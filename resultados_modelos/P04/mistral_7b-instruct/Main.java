public class Main {

    static class Livro {
        private String titulo;
        private boolean disponivel;

        public Livro(String titulo) {
            this.titulo = titulo;
            this.disponivel = true;
        }

        public void emprestar() {
            if (!disponivel) {
                throw new IllegalStateException("Livro indisponível para empréstimo.");
            }
            disponivel = false;
        }

        public void devolver() {
            if (disponivel) {
                throw new IllegalStateException("Livro já está disponível.");
            }
            disponivel = true;
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("Livro 1");
        Livro livro2 = new Livro("Livro 2");

        try {
            livro1.emprestar();
            System.out.println("Livro 1 emprestado com sucesso.");
            livro1.devolver();
            System.out.println("Livro 1 devolvido com sucesso.");
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }

        try {
            livro2.emprestar();
            System.out.println("Livro 2 emprestado com sucesso."); // Não será impresso
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }
}
