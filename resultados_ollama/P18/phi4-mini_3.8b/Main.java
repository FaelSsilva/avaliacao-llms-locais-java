public class Main {
    interface Notificador {
        void enviar(String mensagem);
    }

    static class NotificadorEmail implements Notificador {
        @Override
        public void enviar(String mensagem) {
            System.out.println("Email enviado: " + mensagem);
        }
    }

    static class NotificadorConsole implements Notificador {
        @Override
        public void enviar(String mensagem) {
            System.out.println("Console: " + mensagem);
        }
    }

    public static void main(String[] args) {
        Notificador[] notificadores = { new NotificadorEmail(), new NotificadorConsole() };
        for (Notificador notificador : notificadores) {
            notificador.enviar("Olá, Mundo!");
        }
    }
}
