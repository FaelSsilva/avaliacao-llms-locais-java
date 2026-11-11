interface Pagavel {
    void pagar(double valor);
    double calcularTotal();
}

class Multa implements Pagavel {
    private final double valor;
    private boolean pago;

    public Multa(double valor) {
        this.valor = valor;
        this.pago = false;
    }

    @Override
    public void pagar(double valor) {
        if (!pago) {
            this.pago = true;
            System.out.printf("Pagamento de R$ %.2f realizado com sucesso.\n", this.valor);
        } else {
            throw new IllegalStateException("A multa já foi paga.");
        }
    }

    @Override
    public double calcularTotal() {
        return this.valor;
    }
}

public class Main {
    public static void main(String[] args) {
        Multa multa = new Multa(100.0);
        multa.pagar(100.0);
        System.out.printf("Total da multa: R$ %.2f\n", multa.calcularTotal());
        try {
            multa.pagar(100.0);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
        }
    }
}
