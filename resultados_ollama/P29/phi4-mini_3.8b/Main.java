import java.time.LocalDate;

public class Main {
    public static class RegraDevolucao {
        public int calcularDiasDeAtraso(LocalDate dataPresenca, LocalDate dataPresencaPrazo) {
            if (dataPresenca.isAfter(dataPresencaPrazo)) {
                return 0;
            }
            return (int) ChronoUnit.DAYS.between(dataPresencaPrazo, dataPresenca);
        }
    }

    public static void main(String[] args) {
        RegraDevolucao regra = new RegraDevolucao();
        LocalDate dataPresencaPrazo = LocalDate.of(2023, 4, 15);
        LocalDate dataPresenca = LocalDate.of(2023, 4, 20);

        int diasDeAtraso = regra.calcularDiasDeAtraso(dataPresenca, dataPresencaPrazo);
        System.out.println("Dias de atraso: " + diasDeAtraso); // Saída: Dias de atraso: 5

        dataPresenca = LocalDate.of(2023, 4, 10);
        diasDeAtraso = regra.calcularDiasDeAtraso(dataPresenca, dataPresencaPrazo);
        System.out.println("Dias de atraso: " + diasDeAtraso); // Saída: Dias de atraso: 0
    }
}
