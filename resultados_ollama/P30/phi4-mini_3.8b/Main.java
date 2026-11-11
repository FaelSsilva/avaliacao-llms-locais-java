public class Main {
    static class Livro {
        private String titulo;
        private boolean emprestado;

        public Livro(String titulo) {
            this.titulo = titulo;
            this.emprestado = false;
        }

        public String getTitulo() {
            return titulo;
        }

        public boolean isEmprestado() {
            return emprestado;
        }

        public void emprestar() {
            this.emprestado = true;
        }

        public void devolver() {
            this.emprestado = false;
        }
    }

    static class Usuario {
        private String nome;

        public Usuario(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    static class ServicoEmprestimo {
        public void emprestarLivro(Livro livro, Usuario usuario) throws IllegalStateException {
            if (!livro.isEmprestado() && !livro.isAtivo()) {
                livro.emprestar();
                System.out.println(usuario.getNome() + " emprestou o livro: " + livro.getTitulo());
            } else {
                throw new IllegalStateException("O livro não está disponível ou já foi emprestado.");
            }
        }

        public void devolverLivro(Livro livro, Usuario usuario) throws IllegalStateException {
            if (livro.isEmprestado()) {
                livro.devolver();
                System.out.println(usuario.getNome() + " devolveu o livro: " + livro.getTitulo());
            } else {
                throw new IllegalStateException("O livro não está emprestado.");
            }
        }
    }

    public static void main(String[] args) {
        Livro livro = new Livro("1984");
        Usuario usuario = new Usuario("Alice");
        ServicoEmprestimo servico = new ServicoEmprestimo();

        try {
            servico.emprestarLivro(livro, usuario);
            servico.devolverLivro(livro, usuario);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
