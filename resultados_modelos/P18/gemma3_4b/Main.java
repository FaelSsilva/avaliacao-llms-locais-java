package com.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    interface Notificador {
        void enviar(String mensagem);
    }

    static class NotificadorEmail implements Notificador {
        @Override
        public void enviar(String mensagem) {
            System.out.println("[EMAIL] Mensagem: " + mensagem);
        }
    }

    static class NotificadorConsole implements Notificador {
        @Override
        public void enviar(String mensagem) {
            System.out.println("[CONSOLE] Mensagem: " + mensagem);
        }
    }

    public static void main(String[] args) {
        List<Notificador> notificadores = new ArrayList<>();
        notificadores.add(new NotificadorEmail());
        notificadores.add(new NotificadorConsole());

        for (Notificador notificador : notificadores) {
            notificador.enviar("Esta é uma notificação de teste.");
        }
    }
}
