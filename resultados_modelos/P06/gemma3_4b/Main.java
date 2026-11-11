public class Main {

    static class Usuario {
        private String nome;
        private int limiteMaximo;
        private int quantidadeEmprestimos;
        private static int limiteEmprestimosMaximo = 3;

        public Usuario(String nome, int limiteMaximo) {
            this.nome = nome;
            this.limiteMaximo = limiteMaximo;
            this.quantidadeEmprestimos = 0;
        }

        public boolean podeEmprestar() {
            return quantidadeEmprestimos < limiteMaximo;
        }

        public void registrarEmprestimo() {
            if (quantidadeEmprestimos < limiteMaximo) {
                quantidadeEmprestimos++;
            } else {
                throw new IllegalStateException("Limite de empréstimos atingido.");
            }
        }

        public void registrarDevolucao() {
            if (quantidadeEmprestimos > 0) {
                quantidadeEmprestimos--;
            } else {
                throw new IllegalStateException("Não há empréstimos para devolver.");
            }
        }
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario("João", 2);
        System.out.println("Usuário: " + usuario.nome);
        System.out.println("Pode emprestar: " + usuario.podeEmprestar());

        usuario.registrarEmprestimo();
        System.out.println("Emprestimo registrado. Pode emprestar: " + usuario.podeEmprestar());
        System.out.println("Quantidade de empréstimos: " + usuario.quantidadeEmprestimos);

        usuario.registrarEmprestimo();
        usuario.registrarEmprestimo();
        System.out.println("Quantidade de empréstimos: " + usuario.quantidadeEmprestimos);

        try {
            usuario.registrarEmprestimo();
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }

        usuario.registrarDevolucao();
        System.out.println("Quantidade de empréstimos: " + usuario.quantidadeEmprestimos);
    }
}
