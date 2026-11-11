public class Main {

    static class Multa {
        private int diasAtraso;
        private double valorDiario;

        public Multa(int diasAtraso, double valorDiario) {
            if (diasAtraso < 0) {
                throw new IllegalArgumentException("Dias de atraso não podem ser negativos.");
            }
            if (valorDiario < 0) {
                throw new IllegalArgumentException("Valor diário não pode ser negativo.");
            }
            this.diasAtraso = diasAtraso;
            this.valorDiario = valorDiario;
        }

        public double calcularValor() {
            return diasAtraso * valorDiario;
        }
    }

    public static void main(String[] args) {
        Multa multa1 = new Multa(0, 50.0);
        System.out.println("Multa com atraso zero: " + multa1.calcularValor());

        Multa multa2 = new Multa(10, 20.0);
        System.out.println("Multa com atraso de 10 dias: " + multa2.calcularValor());
    }
}
