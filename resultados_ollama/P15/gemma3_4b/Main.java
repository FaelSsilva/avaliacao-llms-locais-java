package com.example;

import java.time.LocalDate;

class Emprestimo {
    private LocalDate dataRetirada;
    private LocalDate dataDevolucao;

    public Emprestimo(LocalDate dataRetirada, LocalDate dataDevolucao) {
        this.dataRetirada = dataRetirada;
        this.dataDevolucao = dataDevolucao;
        if (dataRetirada.isAfter(dataDevolucao)) {
            throw new IllegalArgumentException("A data de retirada não pode ser posterior à data de devolução.");
        }
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public boolean estaAtrasado(LocalDate dataAtual) {
        return dataAtual.isAfter(dataDevolucao);
    }
}

class TesteEmprestimo {
    public static void main(String[] args) {
        Emprestimo emprestimo1 = new Emprestimo(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 15));
        System.out.println("Emprestimo 1 está atrasado? " + emprestimo1.estaAtrasado(LocalDate.of(2024, 1, 12))); // false

        Emprestimo emprestimo2 = new Emprestimo(LocalDate.of(2024, 1, 1), LocalDate.of(2023, 12, 31));
        System.out.println("Emprestimo 2 está atrasado? " + emprestimo2.estaAtrasado(LocalDate.of(2024, 1, 10))); // true
    }
}
