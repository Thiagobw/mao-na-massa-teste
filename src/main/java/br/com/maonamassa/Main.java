package br.com.maonamassa;

import br.com.maonamassa.model.Funcionario;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        List<Funcionario> funcionarios = inserirFuncionarios();

        removerFuncionario(funcionarios, "João");

        imprimirFuncionarios(funcionarios, "Lista de funcionarios:");

        aplicarAumento(funcionarios, new BigDecimal("1.1"));

        Map<String, List<Funcionario>> funcionariosPorFuncao = agruparPorFuncao(funcionarios);
        imprimirFuncionariosAgrupados(funcionariosPorFuncao);

        imprimirAniversariantes(funcionarios, Month.OCTOBER, Month.DECEMBER);

        imprimirFuncionarioComMaiorIdade(funcionarios);

        ordenarPorNome(funcionarios);
        imprimirFuncionarios(funcionarios, "Funcionários em ordem alfabética:");

        imprimirTotalSalarios(funcionarios);

        imprimirSalariosMinimos(funcionarios, new BigDecimal("1212.00"));
    }
    private static List<Funcionario> inserirFuncionarios() {
        return new ArrayList<>(Arrays.asList(
                new Funcionario("Maria", LocalDate.of(2000, 10, 18), new BigDecimal("2009.44"), "Operador"),
                new Funcionario("João", LocalDate.of(1990, 5, 12), new BigDecimal("2284.38"), "Operador"),
                new Funcionario("Caio", LocalDate.of(1961, 5, 2), new BigDecimal("9836.14"), "Coordenador"),
                new Funcionario("Miguel", LocalDate.of(1988, 10, 14), new BigDecimal("19119.88"), "Diretor"),
                new Funcionario("Alice", LocalDate.of(1995, 1, 5), new BigDecimal("2234.68"), "Recepcionista"),
                new Funcionario("Heitor", LocalDate.of(1999, 11, 19), new BigDecimal("1582.72"), "Operador"),
                new Funcionario("Arthur", LocalDate.of(1993, 3, 31), new BigDecimal("4071.84"), "Contador"),
                new Funcionario("Laura", LocalDate.of(1994, 7, 8), new BigDecimal("3017.45"), "Gerente"),
                new Funcionario("Heloisa", LocalDate.of(2003, 5, 24), new BigDecimal("1606.85"), "Eletricista"),
                new Funcionario("Helena", LocalDate.of(1996, 9, 2), new BigDecimal("2799.93"), "Gerente")
        ));
    }

    private static void removerFuncionario(List<Funcionario> funcionarios, String nome) {
        boolean existe = funcionarios.stream().anyMatch(f -> f.getNome().equalsIgnoreCase(nome));
        if (existe) {
            funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase(nome));
        }
    }

    private static void imprimirFuncionarios(List<Funcionario> funcionarios, String titulo) {
        System.out.println("\n----------------------------------------------\n"+titulo);
        funcionarios.forEach(System.out::println);
    }

    private static void aplicarAumento(List<Funcionario> funcionarios, BigDecimal percentual) {
        funcionarios.forEach(f -> f.setSalario(f.getSalario().multiply(percentual)));
    }

    private static Map<String, List<Funcionario>> agruparPorFuncao(List<Funcionario> funcionarios) {
        return funcionarios.stream().collect(Collectors.groupingBy(Funcionario::getFuncao));
    }

    private static void imprimirFuncionariosAgrupados(Map<String, List<Funcionario>> funcionariosPorFuncao) {
        System.out.println("\n----------------------------------------------\nFuncionários Agrupados por Função:");
        funcionariosPorFuncao.forEach((funcao, lista) -> {
            System.out.println("\nFunção: " + funcao);
            lista.forEach(System.out::println);
        });
    }

    private static void imprimirAniversariantes(List<Funcionario> funcionarios, Month... meses) {
        System.out.println("\n----------------------------------------------\nAniversariantes de " + Arrays.toString(meses) + ":");
        funcionarios.stream()
                .filter(f -> Arrays.asList(meses).contains(f.getDataNascimento().getMonth()))
                .forEach(System.out::println);
    }

    private static void imprimirFuncionarioComMaiorIdade(List<Funcionario> funcionarios) {
        funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento))
                .ifPresent(f -> {
                    Period idade = Period.between(f.getDataNascimento(), LocalDate.now());
                    System.out.println("\n----------------------------------------------\nFuncionário com maior idade:\n " + f.getNome() + ", Idade: " + idade.getYears());
                });
    }

    private static void ordenarPorNome(List<Funcionario> funcionarios) {
        funcionarios.sort(Comparator.comparing(Funcionario::getNome));
    }

    private static void imprimirTotalSalarios(List<Funcionario> funcionarios) {
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        System.out.println("\n----------------------------------------------\nTotal de Salários: " + formatter.format(totalSalarios));
    }

    private static void imprimirSalariosMinimos(List<Funcionario> funcionarios, BigDecimal salarioMinimo) {
        System.out.println("\n----------------------------------------------\nQuantidade de Salários Mínimos por Funcionário:");
        funcionarios.forEach(f -> {
            BigDecimal quantidade = f.getSalario().divide(salarioMinimo, 2, RoundingMode.DOWN);
            System.out.println(f.getNome() + ": " + quantidade);
        });
    }
}