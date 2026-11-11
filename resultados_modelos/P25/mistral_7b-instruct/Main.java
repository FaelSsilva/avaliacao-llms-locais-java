public class Main {

    static class Emprestimo {
        private final String usuario;
        private final String isbn;
        private final LocalDate data;

        public Emprestimo(String usuario, String isbn, LocalDate data) {
            this.usuario = Objects.requireNonNull(usuario);
            this.isbn = Objects.requireNonNull(isbn);
            this.data = Objects.requireNonNull(data);
        }
    }

    static class Historico {
        private final Map<String, List<Emprestimo>> emprestimosPorUsuario = new HashMap<>();

        public void adicionar(Emprestimo emprestimo) {
            String usuario = emprestimo.usuario;
            if (!emprestimosPorUsuario.containsKey(usuario)) {
                emprestimosPorUsuario.put(usuario, new ArrayList<>());
            }
            emprestimosPorUsuario.get(usuario).add(emprestimo);
        }

        public List<Emprestimo> getHistorico(String usuario) {
            return emprestimosPorUsuario.getOrDefault(usuario, new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        Historico historico = new Historico();
        historico.adicionar(new Emprestimo("João", "123-4567", LocalDate.of(2022, 1, 1)));
        historico.adicionar(new Emprestimo("Maria", "234-8901", LocalDate.of(2022, 2, 2)));
        historico.adicionar(new Emprestimo("João", "345-7892", LocalDate.of(2022, 3, 3)));

        System.out.println(historico.getHistorico("João"));
        System.out.println(historico.getHistorico("Maria"));
    }
}
