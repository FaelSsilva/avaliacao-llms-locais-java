public class Main {
    static class FilaReservas<T> {
        private LinkedList<T> fila = new LinkedList<>();

        public void reservar(T item) {
            fila.addLast(item);
        }

        public T proximo() {
            if (estaVazia()) {
                throw new NoSuchElementException("Fila vazia");
            }
            return fila.pollFirst();
        }

        public boolean estaVazia() {
            return fila.isEmpty();
        }
    }

    public static void main(String[] args) {
        FilaReservas<String> reservas = new FilaReservas<>();

        reservas.reservar("Cliente 1");
        reservas.reservar("Cliente 2");

        System.out.println("Proximo: " + reservas.proximo()); // Cliente 1
        System.out.println("Proximo: " + reservas.proximo()); // Cliente 2
        try {
            System.out.println("Proximo: " + reservas.proximo()); // Exceção
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
        }
    }
}
