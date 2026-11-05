package com.wil.SistemaReembolsos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.wil.SistemaReembolsos.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {

    List<Pedido> findByFuncionarioIdOrderByDataCriacaoDesc(Long funcionarioId);

    List<Pedido> findByStatusAtualOrderByDataCriacaoDesc(String statusAtual);
    
    List<Pedido> findByDataCriacaoOrderByDataCriacaoDesc(String dataCriacao);

    boolean existsById(Long id);

    List<Pedido> findByFuncionarioIdAndDataCriacaoBetween(Long idFuncionario, LocalDateTime inicio, LocalDateTime fim);
}
