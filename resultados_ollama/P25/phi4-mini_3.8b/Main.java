import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static class Emprestimo {
        private String identificador;
        private String usuario;
        private String isbn;
        private LocalDate dataEmpréstimo;

        public Empresteo(String identificador, String usuario, String isbn, LocalDate dataEmpréstimo) {
            this.identificador = identificador;
            this.usuario = usuario;
            this.isbn = isbn;
            this.dataEmpréstimo = dataEmpréstimo;
        }

        public String getUsuario() {
            return usuario;
        }

        public String getHistorico() {
            return historico();
        }

        private String historico() {
            List<Emprestimo> emprestimos = historico().getEmprestimos();
            return emprestimos.stream()
                    .map(Emprestimo::getUsuario)
                    .map(String::toUpperCase)
                    .collect(Collectors.joining(", "));
        }

        public static List<Emprestimo> historico(String identificador) {
            List<Emprestimo> emprestimos = new ArrayList<>();
            emprestimos.add(new Empresteo("ID001", "João", "1234567890", LocalDate.now()));
            emprestimos.add(new Empresteo("ID002", "Maria", "0987654321", LocalDate.now()));
            return emprestimos.stream()
                    .filter(e -> e.getIdentificador().equals(identificador))
                    .collect(Collectors.toList());
        }
    }

    public static void main(String[] args) {
        List<Empresteo> emprestimos = new ArrayList<>();
        emprestimos.add(new Empresteo("ID001", "João", "1234567890", LocalDate.now()));
        emprestimos.add(new Empresteo("ID002", "Maria", "0987654321", LocalDate.now()));

        String historicoJoao = emprestimos.get(0).getHistorico();
        System.out.println(historicoJoao); // DEPOIS DE EMPRÉSTIMO: JOÃ, DEPOIS DE EMPRÉSTIMO: JOÃ
    }
}
