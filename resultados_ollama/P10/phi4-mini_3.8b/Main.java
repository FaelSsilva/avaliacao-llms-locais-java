abstract class Pessoa {
    private String nome;

    public Pessoa(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public abstract String tipo();
}

class Usuario extends Pessoa {
    private String identificador;

    public Usuario(String nome, String identificador) {
        super(nome);
        this.identificador = identificador;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    @Override
    public String tipo() {
        return "Usuario: " + identificador;
    }
}

class Bibliotecario extends Pessoa {
    private String matricula;

    public Bibliotecario(String nome, String matricula) {
        super(nome);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String tipo() {
        return "Bibliotecario: " + matricula;
    }
}

public class Main {
    public static void main(String[] args) {
        Pessoa usuario = new Usuario("João", "12345");
        Pessoa bibliotecario = new Bibliotecario("Maria", "67890");

        System.out.println(usuario.tipo());
        System.out.println(bibliotecario.tipo());
    }
}
