public class Main {
    static class Livro {
        private String isbn;
        private String titulo;

        public Livro(String isbn, String titulo) {
            this.isbn = isbn;
            this.titulo = titulo;
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitulo() {
            return titulo;
        }
    }

    static class Catalogo {
        private Map<String, Livro> livros = new HashMap<>();

        public boolean adicionar(Livro livro) {
            if (livros.containsKey(livro.getIsbn())) {
                return false;
            }
            livros.put(livro.getIsbn(), livro);
            return true;
        }

        public boolean remover(String isbn) {
            return livros.remove(isbn) != null;
        }
    }

    public static void main(String[] args) {
        Catalogo catalogo = new Catalogo();
        catalogo.adicionar(new Livro("1234567890", "Livro 1"));
        catalogo.adicionar(new Livro("0987654321", "Livro 2"));
        System.out.println("Adicionar Livro 1: " + catalogo.adicionar(new Livro("1234567890", "Livro 1"))); // false
        System.out.println("Adicionar Livro 2: " + catalogo.adicionar(new Livro("0987654321", "Livro 2"))); // true
        System.out.println("Remover Livro 1: " + catalogo.remover("1234567890")); // true
        System.out.println("Remover Livro 3: " + catalogo.remover("0987654321")); // false
    }
}
