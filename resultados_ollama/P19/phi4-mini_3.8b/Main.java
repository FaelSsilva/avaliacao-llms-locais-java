public class Main {
    static class RegraMulta {
        abstract int calcular(int diasAtraso);
    }

    static class RegraMultaR$2PorDia extends RegraMulta {
        @Override
        int calcular(int diasAtraso) {
            if (diasAtraso < 0) {
                throw new IllegalArgumentException("Dias de atraso não podem ser negativos");
            }
            return diasAtraso * 2;
        }
    }

    static class RegraMultaComTeto extends RegraMulta {
        @Override
        int calcular(int diasAtraso) {
            if (diasAtraso < 0) {
                throw new IllegalArgumentException("Dias de atraso não podem ser negativos");
            }
            int multa = diasAtraso * 20;
            return Math.min(multa, 20);
        }
    }

    public static void main(String[] args) {
        RegraMultaR$2PorDia regraR$2PorDia = new RegraMultaR$2PorDia();
        System.out.println("Multa R$ 2 por dia: " + regraR$2PorDia.calcular(5)); // 10

        RegraMultaComTeto regraComTeto = new RegraMultaComTeto();
        System.out.println("Multa com teto: " + regraComTeto.calcular(1)); // 20
        System.out.println("Multa com teto: " + regraComTeto.calcular(2)); // 20
    }
}
