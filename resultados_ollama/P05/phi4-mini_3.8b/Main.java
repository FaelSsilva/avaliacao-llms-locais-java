public class Main {
    static class Usuario {
        private String nome;
        private String identificador;

        public Usuario(String nome, String identificador) {
            setNome(nome);
            setIdentificador(identificador);
        }

        public String getNome() {
            return nome;
        }

        public String getIdentificador() {
            return identificador;
        }

        private void setNome(String nome) {
            if (nome == null || nome.isEmpty()) {
                throw new IllegalArgumentException("Nome não pode ser vazio");
            }
            this.nome = nome;
        }

        private void setIdentificador(String identificador) {
            if (identificador == null || identificador.isEmpty()) {
                throw new IllegalArgumentException("Identificador não pode ser vazio");
            }
            this.identificador = identificador;
        }

        public String apresentar() {
            return nome + " (" + identificador + ")";
        }
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario("John Doe", "JD123");
        System.out.println(usuario.apresentar());
    }
}
