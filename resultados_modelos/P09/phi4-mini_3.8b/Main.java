public class Main {
    public static void main(String[] args) {
        List<Publicacao> publicacoes = new ArrayList<>();
        publicacoes.add(new Livro("1984", 1949, "George Orwell"));
        publicacoes.add(new Revista("Nature", 2021, "Revista Nature"));

        for (Publicacao publica : publicacoes) {
            System.out.println(publica.descricao());
        }
    }

    static class Publicacao {
        private String titulo;
        private int ano;

        public Publicacao(String titulo, int ano) {
            this.titulo = titulo;
            this.ano = ano;
        }

        public String descricao() {
            return "Título: " + titulo + ", Ano: " + ano;
        }
    }

    static class Livro extends Publicacao {
        private String autor;

        public Livro(String titulo, int ano, String autor) {
            super(titulo, ano);
            this.autor = autor;
        }

        @Override
        public String descricao() {
            return super.descricao() + ", Autor: " + autor;
        }
    }

    static class Revista extends Publicacao {
        private String edicao;

        public Revista(String titulo, int ano, String edicao) {
            super(titulo, ano);
            this.edicao = edicao;
        }

        @Override
        public String descricao() {
            return super.descricao() + ", Edição: " + edicao;
        }
    }
}
