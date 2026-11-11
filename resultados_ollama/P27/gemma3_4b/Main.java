public class Main {

    static class PoliticaEmprestimo {
        private final int prazo;
        private final int limitePorUsuario;

        PoliticaEmprestimo(int prazo, int limitePorUsuario) {
            if (prazo <= 0) {
                throw new IllegalArgumentException("O prazo deve ser maior que zero.");
            }
            if (limitePorUsuario <= 0) {
                throw new IllegalArgumentException("O limite por usuário deve ser maior que zero.");
            }
            this.prazo = prazo;
            this.limitePorUsuario = limitePorUsuario;
        }

        public int getPrazo() {
            return prazo;
        }

        public int getLimitePorUsuario() {
            return limitePorUsuario;
        }
    }

    public static void main(String[] args) {
        PoliticaEmprestimo politica = new PoliticaEmprestimo(30, 5);
        System.out.println("Prazo: " + politica.getPrazo() + " dias");
        System.out.println("Limite por usuário: " + politica.getLimitePorUsuario());

        try {
            PoliticaEmprestimo politicaInvalida = new PoliticaEmprestimo(-10, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
