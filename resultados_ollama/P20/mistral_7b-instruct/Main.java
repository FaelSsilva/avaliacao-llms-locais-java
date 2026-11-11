import java.util.HashMap;
import java.util.Optional;

public class Main {
    interface RepositorioLivro {
        void salvar(Livro livro);
        Optional<Livro> buscarPorIsbn(String isbn);
    }

    static class RepositorioLivroMemoria implements RepositorioLivro {
        private final HashMap<String, Livro> livros = new HashMap<>();

        @Override
        public void salvar(Livro livro) {
            if (livros.containsKey(livro.getIsbn())) {
                throw new RuntimeException("ISBN já existe");
            }
            livros.put(livro.getIsbn(), livro);
        }

        @Override
        public Optional<Livro> buscarPorIsbn(String isbn) {
            return Optional.ofNullable(livros.get(isbn));
        }
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
    }

    public static void main(String[] args) {
        RepositorioLivro repositorio = new RepositorioLivroMemoria();
        repositorio.salvar(new Livro("123-4567", "O Pequeno Príncipe"));
        Optional<Livro> livro1 = repositorio.buscarPorIsbn("123-4567");
        System.out.println(livro1.isPresent() ? livro1.get().getTitulo() : "Livro não encontrado");

        try {
            repositorio.salvar(new Livro("123-4567", "O Pequeno Príncipe"));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
}
