import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Main {
    public static class RepositorioLivro {
        void salvar(Livro livro) throws IllegalArgumentException {
            throw new UnsupportedOperationException("Operação não suportada");
        }

        Optional<Livro> buscarPorIsbn(String isbn) {
            throw new UnsupportedOperationException("Operação não suportada");
        }
    }

    public static class RepositorioLivroMemoria extends RepositorioLivro {
        private final Map<String, Livro> livros = new HashMap<>();

        @Override
        void salvar(Livro livro) {
            livros.put(livro.getIsbn(), livro);
        }

        @Override
        Optional<Livro> buscarPorIsbn(String isbn) {
            return Optional.ofNullable(livros.get(isbn));
        }
    }

    public static void main(String[] args) {
        RepositorioLivroMemoria repositorio = new RepositorioLivroMemoria();
        Livro livro1 = new Livro("123-456-789", "Título 1");
        Livro livro2 = new Livro("987-654-321", "Título 2");

        repositorio.salvar(livro1);
        repositorio.salvar(livro2);

        Optional<Livro> livroEncontrado = repositorio.buscarPorIsbn("123-456-789");
        livroEncontrado.ifPresent(System.out::println);

        livroEncontrado = repositorio.buscarPorIsbn("000-000-000");
        livroEncontrado.ifPresent(System.out::println);
    }

    static class Livro {
        private final String isbn;
        private final String titulo;

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

        @Override
        public String toString() {
            return "Livro{" +
                    "isbn='" + isbn + '\'' +
                    ", titulo='" + titulo + '\'' +
                    '}';
        }
    }
}
