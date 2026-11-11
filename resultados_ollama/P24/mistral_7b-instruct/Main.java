public class Main {

    static class Livro {
        final String titulo;
        final String isbn;

        Livro(String titulo, String isbn) {
            this.titulo = titulo;
            this.isbn = isbn;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Livro) {
                Livro outro = (Livro) obj;
                return outro.isbn.equals(isbn);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return isbn.hashCode();
        }
    }

    static class Devolucao {
        private final Set<Livro> livros = new TreeSet<>();

        void adicionar(Livro livro) {
            livros.add(livro);
        }

        void remover(Livro livro) {
            livros.remove(livro);
        }

        int quantidade() {
            return livros.size();
        }
    }

    public static void main(String[] args) {
        Devolucao devolucao = new Devolucao();
        devolucao.adicionar(new Livro("Livro1", "123"));
        devolucao.adicionar(new Livro("Livro2", "456"));
        devolucao.adicionar(new Livro("Livro1", "123"));
        System.out.println(devolucao.quantidade()); // 2
    }
}
