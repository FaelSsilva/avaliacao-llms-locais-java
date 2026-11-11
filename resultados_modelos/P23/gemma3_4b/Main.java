public class Main {

    interface EstrategiaBusca {
        String buscar(List<String> livros);
    }

    static class BuscaExata implements EstrategiaBusca {
        @Override
        public String buscar(List<String> livros) {
            StringBuilder resultado = new StringBuilder();
            for (String livro : livros) {
                if (livro != null) {
                    resultado.append(livro).append("\n");
                }
            }
            return resultado.toString();
        }
    }

    static class BuscaTrecho implements EstrategiaBusca {
        @Override
        public String buscar(List<String> livros) {
            StringBuilder resultado = new StringBuilder();
            for (String livro : livros) {
                if (livro != null) {
                    resultado.append(livro).append("\n");
                }
            }
            return resultado.toString();
        }
    }

    static class Catalogo {
        private EstrategiaBusca estrategia;

        public Catalogo(EstrategiaBusca estrategia) {
            this.estrategia = estrategia;
        }

        public String buscar(List<String> livros) {
            return estrategia.buscar(livros);
        }
    }

    public static void main(String[] args) {
        List<String> livros = new ArrayList<>();
        livros.add("O Senhor dos Anéis");
        livros.add("O Hobbit");
        livros.add("A Guerra dos Tronos");
        livros.add("O Nome do Vento");

        EstrategiaBusca buscaExata = new BuscaExata();
        Catalogo catalogoExato = new Catalogo(buscaExata);
        System.out.println("Busca Exata:\n" + catalogoExato.buscar(livros));

        EstrategiaBusca buscaTrecho = new BuscaTrecho();
        Catalogo catalogoTrecho = new Catalogo(buscaTrecho);
        System.out.println("\nBusca por Trecho:\n" + catalogoTrecho.buscar(livros));
    }
}
