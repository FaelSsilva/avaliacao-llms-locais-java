public class Main {

    interface Validador<T> {
        boolean validar(T isbn);
    }

    static class ValidadorIsbnSimples implements Validador<String> {
        @Override
        public boolean validar(String isbn) {
            return isbn != null && isbn.matches("\\d{13}");
        }
    }

    public static void main(String[] args) {
        Validador<String> validador = new ValidadorIsbnSimples();

        System.out.println("ISBN válido: 9781234567890123");
        System.out.println("ISBN inválido: 978123456789012X");
        System.out.println("ISBN inválido: 9781234567890123X");
        System.out.println("ISBN inválido: 978123456789012345");
        System.out.println("ISBN inválido: null");
        System.out.println("ISBN inválido: ");
    }
}
