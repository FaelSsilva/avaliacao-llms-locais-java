class LivroIndisponivelException extends RuntimeException {}

class Livro {
    private boolean emprestado;

    public Livro(boolean emprestado) {
        this.emprestado = emprestado;
    }

    public void emprestar() throws LivroIndisponivelException {
        if (emprestado) {
            throw new LivroIndisponivelException();
        }
        emprestado = true;
    }
}

public class Main {
    public static void main(String[] args) {
        try {
            Livro livro = new Livro(false);
            livro.emprestar();
            System.out.println("Livro emprestado com sucesso.");

            livro.emprestar();
            System.out.println("Livro emprestado novamente.");
        } catch (LivroIndisponivelException e) {
            System.err.println("Livro indisponível para empréstimo.");
        }
    }
}
