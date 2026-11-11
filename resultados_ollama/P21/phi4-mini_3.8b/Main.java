public class Main {
    static class Validador<T> {
        public boolean validar(T entrada) {
            throw new UnsupportedOperationException("Método não implementado");
        }
    }

    static class ValidadorIsbnSimples extends Validador<String> {
        @Override
        public boolean validar(String isbn) {
            return isbn != null && isbn.matches("\\d{13}");
        }
    }

    public static void main(String[] args) {
        ValidadorIsbnSimples validator = new ValidadorIsbnSimples();
        System.out.println("Validação de '9780134190440': " + validator.validar("9780134190440")); // Válido
        System.out.println("Validação de '978013419044': '978013419044' é inválido: " + validator.validar("978013419044")); // Invalido
        System.out.println("Validação de '123': '123' é inválido: " + validator.validar("123")); // Invalido
        System.out.println("Validação de '1234567890123': '1234567890123' é inválido: " + validator.validar("1234567890123")); // Invalido
    }
}
