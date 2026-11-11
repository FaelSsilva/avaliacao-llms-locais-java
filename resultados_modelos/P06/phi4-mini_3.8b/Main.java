public class Main {
    public static class Usuario {
        private String nome;
        private int limiteMaximo;
        private int quantidadeAtual;

        public Usuario(String nome, int limiteMaximo) {
            this.nome = nome;
            this.limiteMaximo = limiteMaximo;
            this.quantidadeAtual = 0;
        }

        public String getNome() {
            return nome;
        }

        public void podeEmprestar() {
            if (quantidadeAtual >= limiteMaximo) {
                throw new IllegalStateException("O limite máximo de empréstimos já foi atingido.");
            }
        }

        public void registrarEmprestimo() {
            podeEmprestar();
            quantidadeAtual++;
        }

        public void registrarDevolucao() {
            if (quantidadeAtual <= 0) {
                throw new IllegalStateException("Não há empréstimos para devolver.");
            }
            quantidadeAtual--;
        }

        public int getQuantidadeAtual() {
            return quantidadeAtual;
        }
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario("João", 3);
        usuario.podeEmprestar();
        usuario.registrarEmprestimo();
        usuario.registrarEmprestimo();
        System.out.println("Quantas empréstimos? " + usuario.getQuantidadeAtual());
        usuario.registrarDevolucao();
        System.out.println("Quantas empréstimos após devolução? " + usuario.getQuantidadeAtual());
        // O seguinte linha deve causar uma exceção devido ao limite de empréstimos já atingido.
        // usuario.registrarEmprestimo();
    }
}
