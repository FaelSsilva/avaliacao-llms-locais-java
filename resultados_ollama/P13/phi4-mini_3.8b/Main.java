public class Main {
    static class Livro {
        private String titulo;

        public Livro(String titulo) {
            this.titulo = titulo;
        }

        public String getTitulo() {
            return titulo;
        }
    }

    static class Reserva {
        private Livro livro;
        private String usuario;
        private boolean ativo;

        public Reserva(Livro livro, String usuario) {
            this.livro = livro;
            this.usuario = usuario;
            this.ativo = true;
        }

        public void cancelar() {
            if (!ativo) {
                throw new IllegalStateException("A reserva já está cancelada.");
            }
            ativo = false;
        }

        public boolean isAtivo() {
            return ativo;
        }
    }

    public static void main(String[] args) {
        Livro livro = new Livro("Algoritmos");
        Reserva reserva = new Reserva(livro, "João");

        System.out.println("Reserva ativa: " + reserva.isAtivo());

        reserva.cancelar();
        System.out.println("Reserva ativa após cancelamento: " + reserva.isAtivo());

        try {
            reserva.cancelar();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
