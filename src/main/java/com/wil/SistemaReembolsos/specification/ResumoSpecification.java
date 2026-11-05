package com.wil.SistemaReembolsos.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.wil.SistemaReembolsos.model.Pedido;

public class ResumoSpecification {

    public static Specification<Pedido> porFuncionario(Long funcionarioId) {
        return (root, query, cb) -> cb.equal(root.get("funcionario").get("id"), funcionarioId);
    }

    public static Specification<Pedido> porDataInicial(LocalDate dataInicial) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dataDespesa"), dataInicial.atStartOfDay());
    }

    public static Specification<Pedido> porDataFinal(LocalDate dataFinal) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dataDespesa"), dataFinal.plusDays(1).atStartOfDay());
    }
}
