public class Main {
    interface Notificador {
        void enviar(String mensagem);
    }

    static class NotificadorEmail implements Notificador {
        void enviar(String mensagem) {
            System.out.println("Email: " + mensagem);
        }
    }

    static class NotificadorConsole implements Notificador {
        void enviar(String mensagem) {
            System.out.println("Console: " + mensagem);
        }
    }

    public static void main(String[] args) {
        List<Notificador> notificadores = new ArrayList<>();
        notificadores.add(new NotificadorEmail());
        notificadores.add(new NotificadorConsole());

        for (Notificador notificador : notificadores) {
            notificador.enviar("Teste de notificação");
        }
    }
}
