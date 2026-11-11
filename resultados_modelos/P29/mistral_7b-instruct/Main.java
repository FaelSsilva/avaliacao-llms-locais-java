import java.time.LocalDate;

public class Main {
    interface RegraDevolucao {
        int calcularDiasAtraso(LocalDate dataPrevista, LocalDate dataReal);
    }

    static class RegraSimples implements RegraDevolucao {
        @Override
        public int calcularDiasAtraso(LocalDate dataPrevista, LocalDate dataReal) {
            return dataReal.isAfter(dataPrevista) ? (int) ChronoUnit.DAYS.between(dataPrevista, dataReal) : 0;
        }
    }

    public static void main(String[] args) {
        RegraDevolucao regra = new RegraSimples();
        LocalDate dataPrevista = LocalDate.of(2022, 1, 1);
        LocalDate dataReal = LocalDate.of(2022, 1, 5);
        System.out.println("Data de devolução: " + dataReal + ", dias de atraso: " + regra.calcularDiasAtraso(dataPrevista, dataReal));

        LocalDate dataPrevista2 = LocalDate.of(2022, 1, 5);
        LocalDate dataReal2 = LocalDate.of(2022, 1, 1);
        System.out.println("Data de devolução: " + dataReal2 + ", dias de atraso: " + regra.calcularDiasAtraso(dataPrevista2, dataReal2));
    }
}
