public class Main {

    static class Publicacao {
        private String titulo;
        private int ano;

        public Publicacao(String titulo, int ano) {
            this.titulo = titulo;
            this.ano = ano;
        }

        public String getTitulo() {
            return titulo;
        }

        public int getAno() {
            return ano;
        }

        public String descricao() {
            return titulo + " (" + ano + ")";
        }
    }

    static class Livro extends Publicacao {
        private String autor;

        public Livro(String titulo, int ano, String autor) {
            super(titulo, ano);
            this.autor = autor;
        }

        public String getAutor() {
            return autor;
        }

        @Override
        public String descricao() {
            return super.descricao() + ", por " + autor;
        }
    }

    static class Revista extends Publicacao {
        private int edicao;

        public Revista(String titulo, int ano, int edicao) {
            super(titulo, ano);
            this.edicao = edicao;
        }

        public int getEdicao() {
            return edicao;
        }

        @Override
        public String descricao() {
            return super.descricao() + ", edição " + edicao;
        }
    }

    public static void main(String[] args) {
        Publicacao[] publicacoes = new Publicacao[3];
        publicacoes[0] = new Livro("O Senhor dos Anéis", 1954, "J.R.R. Tolkien");
        publicacoes[1] = new Revista("National Geographic", 1985, 5);
        publicacoes[2] = new Livro("A Bíblia", 1604, "Muitos autores");

        for (Publicacao publicacao : publicacoes) {
            System.out.println(publicacao.descricao());
        }
    }
}
