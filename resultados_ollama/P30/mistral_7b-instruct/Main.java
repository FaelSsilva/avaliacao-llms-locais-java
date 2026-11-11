public class Main {

    static class Livro {
        private String titulo;
        private boolean emprestado;

        public Livro(String titulo) {
            this.titulo = titulo;
            this.emprestado = false;
        }

        public boolean isEmprestado() {
            return emprestado;
        }

        public void emprestar(Usuario usuario) {
            if (!emprestado) {
                emprestado = true;
                usuario.emprestar(this);
            } else {
                throw new IllegalStateException("Livro já está emprestado.");
            }
        }
    }

    static class Usuario {
        private String nome;
        private Livro livroEmprestado;

        public Usuario(String nome) {
            this.nome = nome;
            this.livroEmprestado = null;
        }

        public void emprestar(Livro livro) {
            if (livro.isEmprestado()) {
                throw new IllegalStateException("Livro já está emprestado.");
            }
            if (livroEmprestado != null) {
                throw new IllegalStateException("Usuário já possui um livro emprestado.");
            }
            livroEmprestado = livro;
            livro.emprestar(this);
        }

        public String getNome() {
            return nome;
        }

        public Livro getLivroEmprestado() {
            return livroEmprestado;
        }
    }

    static class ServicoEmprestimo {
        public void emprestarLivro(Usuario usuario, Livro livro) {
            try {
                usuario.emprestar(livro);
                System.out.printf("Livro %s emprestado para %s.%n", livro.titulo, usuario.getNome());
            } catch (IllegalStateException e) {
                System.out.printf("Falha ao emprestar livro %s para %s: %s.%n", livro.titulo, usuario.getNome(), e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Livro livro1 = new Livro("Livro 1");
        Livro livro2 = new Livro("Livro 2");
        Usuario usuario1 = new Usuario("Usuário 1");
        Usuario usuario2 = new Usuario("Usuário 2");
        ServicoEmprestimo servico = new ServicoEmprestimo();

        servico.emprestarLivro(usuario1, livro1);
        servico.emprestarLivro(usuario2, livro2);
        servico.emprestarLivro(usuario1, livro2);
        servico.emprestarLivro(usuario2, livro1);
    }
}
