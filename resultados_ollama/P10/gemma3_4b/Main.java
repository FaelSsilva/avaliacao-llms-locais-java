package com.example;

public class Main {

    static class Pessoa {
        private String nome;

        public Pessoa(String nome) {
            this.nome = nome;
        }

        public String nome() {
            return nome;
        }
    }

    static class Usuario extends Pessoa {
        private String identificador;

        public Usuario(String nome, String identificador) {
            super(nome);
            this.identificador = identificador;
        }

        @Override
        public String tipo() {
            return "Usuário: " + identificador;
        }
    }

    static class Bibliotecario extends Pessoa {
        private String matrícula;

        public Bibliotecario(String nome, String matrícula) {
            super(nome);
            this.matrícula = matrícula;
        }

        @Override
        public String tipo() {
            return "Bibliotecário: " + matrícula;
        }
    }

    public static void main(String[] args) {
        Usuario usuario = new Usuario("João", "USR123");
        Bibliotecario bibliotecario = new Bibliotecario("Maria", "BIB456");

        System.out.println(usuario.tipo());
        System.out.println(bibliotecario.tipo());

        Pessoa pessoa = new Pessoa("Carlos");
        System.out.println(pessoa.tipo());
    }
}
