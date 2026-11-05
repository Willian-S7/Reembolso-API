package com.wil.SistemaReembolsos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wil.SistemaReembolsos.dto.response.HistoricoStatusResponse;
import com.wil.SistemaReembolsos.model.Funcionario;
import com.wil.SistemaReembolsos.model.HistoricoStatus;
import com.wil.SistemaReembolsos.model.Pedido;
import com.wil.SistemaReembolsos.model.StatusPedido;
import com.wil.SistemaReembolsos.repository.HistoricoStatusRepository;

import jakarta.transaction.Transactional;

@Service 
public class HistoricoStatusService {

    private final HistoricoStatusRepository historicoStatusRepository;

    public HistoricoStatusService(HistoricoStatusRepository historicoStatusRepository) {
        this.historicoStatusRepository = historicoStatusRepository;
    }

    public HistoricoStatusResponse toDTO(HistoricoStatus historicoStatus) {
        return new HistoricoStatusResponse(
            historicoStatus.getId(),
            historicoStatus.getPedido().getId(),
            historicoStatus.getFuncionario().getNome(),
            historicoStatus.getFuncionario().getId(),
            historicoStatus.getStatusAnterior(),
            historicoStatus.getStatusNovo(),
            historicoStatus.getObservacao(),
            historicoStatus.getDataAlteracao()
        );
    }

    @Transactional
    public HistoricoStatus registrarAlteracao(Pedido pedido, Funcionario funcionario, StatusPedido statusNovo) {
        HistoricoStatus saved = new HistoricoStatus();
        saved.setPedido(pedido);
        saved.setFuncionario(funcionario);
        saved.setStatusAnterior(pedido.getStatusAtual());
        saved.setStatusNovo(statusNovo);
        saved.setDataAlteracao(LocalDateTime.now());
        
        return historicoStatusRepository.save(saved);
    }

    public HistoricoStatusResponse getHistoricoStatusById(Long id) {
        HistoricoStatus historicoStatus = historicoStatusRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Histórico não encontrado"));
        return toDTO(historicoStatus);
    }

    @Transactional 
    public HistoricoStatus alterarStatus(Pedido pedido, Funcionario funcionario, StatusPedido statusAnterior, StatusPedido statusNovo, String observacao) {

        HistoricoStatus historicoStatus = new HistoricoStatus();
        historicoStatus.setPedido(pedido);
        historicoStatus.setFuncionario(funcionario);
        historicoStatus.setStatusAnterior(statusAnterior);
        historicoStatus.setStatusNovo(statusNovo);
        historicoStatus.setObservacao(observacao);
        historicoStatus.setDataAlteracao(LocalDateTime.now());

        return historicoStatusRepository.save(historicoStatus);
    }


    public List<HistoricoStatusResponse> getHistoricoStatusByPedidoId(Long pedidoId) {
        return historicoStatusRepository.findByPedidoIdOrderByDataAlteracaoDesc(pedidoId).stream()
            .map(historico -> toDTO(historico))
            .toList();
    }

    public List<HistoricoStatusResponse> getHistoricoStatusByFuncionarioId(Long funcionarioId) {
        return historicoStatusRepository.findByFuncionarioIdOrderByDataAlteracaoDesc(funcionarioId).stream()
            .map(historico -> toDTO(historico))
            .toList();
    }

    public List<HistoricoStatusResponse> getHistoricoStatusByStatusNovo(StatusPedido statusNovo) {
        return historicoStatusRepository.findByStatusNovoOrderByDataAlteracaoDesc(statusNovo).stream()
            .map(historico -> toDTO(historico))
            .toList();
    }
    
    public List<HistoricoStatusResponse> getHistoricoStatusByDataAlteracao(LocalDateTime dataAlteracao) {
        return historicoStatusRepository.findByDataAlteracaoOrderByDataAlteracaoDesc(dataAlteracao).stream()
            .map(historico -> toDTO(historico))
            .toList();
    }

    public boolean existsById(Long id) {
        return historicoStatusRepository.existsById(id);
    }
    
    public List<HistoricoStatusResponse> listarHistorico() {
        return historicoStatusRepository.findAll().stream()
            .map(historico -> toDTO(historico))
            .toList();
    }
}
