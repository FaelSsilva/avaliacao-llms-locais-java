package com.example;

public class Main {

    interface Pagavel {
        void pagar();
        double calcularTotal();
    }

    static class Multa implements Pagavel {
        private double valor;
        private boolean paga;

        public Multa(double valor) {
            this.valor = valor;
            this.paga = false;
        }

        @Override
        public void pagar() {
            if (paga) {
                throw new IllegalStateException("Multa já foi paga.");
            }
            this.paga = true;
        }

        @Override
        public double calcularTotal() {
            return valor;
        }
    }

    public static void main(String[] args) {
        Multa multa = new Multa(100.0);
        System.out.println("Valor da multa: " + multa.calcularTotal());

        try {
            multa.pagar();
            System.out.println("Multa paga com sucesso.");
            System.out.println("Valor da multa: " + multa.calcularTotal());
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        try {
            multa.pagar();
        } catch (IllegalStateException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
