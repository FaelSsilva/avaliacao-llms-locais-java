package com.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    static class Devolucao {
        private String isbn;
        private String titulo;
        private int quantidade;

        public Devolucao(String isbn, String titulo, int quantidade) {
            this.isbn = isbn;
            this.titulo = titulo;
            this.quantidade = quantidade;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitulo() {
            return titulo;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }
    }

    static class Utils {
        static boolean contains(List<Devolucao> list, String isbn) {
            for (Devolucao dev : list) {
                if (dev.getIsbn().equals(isbn)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        List<Devolucao> devolvidos = new ArrayList<>();

        Devolucao livro1 = new Devolucao("978-8572904400", "O Senhor dos Anéis", 1);
        devolvidos.add(livro1);

        Devolucao livro2 = new Devolucao("978-8572904400", "O Senhor dos Anéis", 1);
        devolvidos.add(livro2);

        Devolucao livro3 = new Devolucao("978-8572904410", "O Hobbit", 2);
        devolvidos.add(livro3);

        System.out.println("Exemplares devolvidos:");
        for (Devolucao dev : devolvidos) {
            System.out.println(dev.getIsbn() + " - " + dev.getTitulo() + " - " + dev.getQuantidade());
        }

        devolvidos.get(0).setQuantidade(2);
        System.out.println("\nApós aumentar a quantidade do livro 1:");
        for (Devolucao dev : devolvidos) {
            System.out.println(dev.getIsbn() + " - " + dev.getTitulo() + " - " + dev.getQuantidade());
        }
    }
}
