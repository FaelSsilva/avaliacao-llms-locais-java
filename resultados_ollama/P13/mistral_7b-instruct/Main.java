public class Main {

    static class Livro {
        private final String titulo;

        public Livro(String titulo) {
            this.titulo = titulo;
        }

        @Override
        public String toString() {
            return titulo;
        }
    }

    static class Reserva {
        private final Usuario usuario;
        private final Livro livro;
        private boolean ativa;

        public Reserva(Usuario usuario, Livro livro) {
            this.usuario = usuario;
            this.livro = livro;
            this.ativa = true;
        }

        public void cancelar() {
            if (!ativa) {
                throw new IllegalStateException("A reserva já foi cancelada.");
            }
            ativa = false;
        }

        @Override
        public String toString() {
            return "Reserva de " + livro + " pelo " + usuario;
        }
    }

    static class Usuario {
        private final String nome;

        public Usuario(String nome) {
            this.nome = nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    public static void main(String[] args) {
        Usuario joao = new Usuario("João");
        Livro livro1 = new Livro("Livro 1");
        Reserva reserva1 = new Reserva(joao, livro1);
        System.out.println(reserva1);
        reserva1.cancelar();
        System.out.println(reserva1);
    }
}
