package com.wil.SistemaReembolsos.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wil.SistemaReembolsos.model.HistoricoStatus;
import com.wil.SistemaReembolsos.model.StatusPedido;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, Long> {

    List<HistoricoStatus> findByPedidoIdOrderByDataAlteracaoDesc(Long pedidoId);
    
    List<HistoricoStatus> findByFuncionarioIdOrderByDataAlteracaoDesc(Long funcionarioId);

    List<HistoricoStatus> findByStatusNovoOrderByDataAlteracaoDesc(StatusPedido statusNovo);

    List<HistoricoStatus> findByDataAlteracaoOrderByDataAlteracaoDesc(LocalDateTime dataAlteracao);

    boolean existById(Long id);
}
