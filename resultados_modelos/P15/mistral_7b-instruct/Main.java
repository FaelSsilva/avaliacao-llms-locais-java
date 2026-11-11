import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    static class Emprestimo {
        private LocalDate dataRetirada;
        private LocalDate dataPrazo;

        public Emprestimo(LocalDate dataRetirada, LocalDate dataPrazo) {
            this.dataRetirada = dataRetirada;
            this.dataPrazo = dataPrazo;
            if (dataPrazo.isBefore(dataRetirada)) {
                throw new IllegalArgumentException("O prazo não pode anteceder a data de retirada.");
            }
        }

        public boolean estaAtrasado(LocalDate dataAtual) {
            return ChronoUnit.DAYS.between(dataAtual, dataPrazo) > 0;
        }
    }

    public static void main(String[] args) {
        Emprestimo emprestimo = new Emprestimo(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 2, 1));
        System.out.println("Emprestimo no prazo: " + !emprestimo.estaAtrasado(LocalDate.of(2022, 1, 15)));
        System.out.println("Emprestimo atrasado: " + emprestimo.estaAtrasado(LocalDate.of(2022, 3, 1)));
    }
}
