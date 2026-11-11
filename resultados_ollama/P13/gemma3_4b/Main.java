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
    }

    static class Reserva {
        private String usuario;
        private Livro livro;
        private boolean ativa;

        public Reserva(String usuario, Livro livro) {
            this.usuario = usuario;
            this.livro = livro;
            this.ativa = true;
        }

        public String getUsuario() {
            return usuario;
        }

        public Livro getLivro() {
            return livro;
        }

        public boolean isAtiva() {
            return ativa;
        }

        public void cancelar() {
            if (isAtiva()) {
                throw new IllegalStateException("Reserva já foi cancelada.");
            }
        }

        public void ativar() {
            this.ativa = true;
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("O Senhor dos Anéis", "J.R.R. Tolkien");
        Reserva reserva1 = new Reserva("João", livro1);
        System.out.println("Usuário: " + reserva1.getUsuario());
        System.out.println("Livro: " + reserva1.getLivro().getTitulo());
        System.out.println("Ativa: " + reserva1.isAtiva());

        try {
            reserva1.cancelar();
            System.out.println("Reserva cancelada.");
            System.out.println("Ativa: " + reserva1.isAtiva());
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
