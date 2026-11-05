package com.wil.SistemaReembolsos.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wil.SistemaReembolsos.dto.response.HistoricoStatusResponse;
import com.wil.SistemaReembolsos.model.StatusPedido;
import com.wil.SistemaReembolsos.service.HistoricoStatusService;

@RestController
@RequestMapping ("/historico")
public class HistoricoController {

    private final HistoricoStatusService historicoStatusService;
    public HistoricoController(HistoricoStatusService historicoStatusService) {
        this.historicoStatusService = historicoStatusService;
    }

    @GetMapping
    public ResponseEntity<List<HistoricoStatusResponse>> listarHistorico() {
        List<HistoricoStatusResponse> historicoResponses = historicoStatusService.listarHistorico();
        return ResponseEntity.ok(historicoResponses);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<HistoricoStatusResponse> buscarHistorico(@PathVariable Long id) {
        return ResponseEntity.ok(historicoStatusService.getHistoricoStatusById(id));
    }

    @GetMapping ("/exists/{id}")
    public ResponseEntity<Boolean> existsHistorico(@PathVariable Long id) {
        boolean exists = historicoStatusService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<HistoricoStatusResponse>> byFuncionarioId(@PathVariable Long funcionarioId) {
        List<HistoricoStatusResponse> historicoResponses = historicoStatusService.getHistoricoStatusByFuncionarioId(funcionarioId);
        return ResponseEntity.ok(historicoResponses);
    }

    @GetMapping ("/pedido/{pedidoId}")
    public ResponseEntity<List<HistoricoStatusResponse>> byPedidoId(@PathVariable Long pedidoId) {
        List<HistoricoStatusResponse> historicoResponses = historicoStatusService.getHistoricoStatusByPedidoId(pedidoId);
        return ResponseEntity.ok(historicoResponses);
    }

    @GetMapping ("/status/{status}")
    public ResponseEntity<List<HistoricoStatusResponse>> byStatus(@PathVariable StatusPedido status) {
        List<HistoricoStatusResponse> historicoResponses = historicoStatusService.getHistoricoStatusByStatusNovo(status);
        return ResponseEntity.ok(historicoResponses);
    }

    @GetMapping ("/data/{data}")
    public ResponseEntity<List<HistoricoStatusResponse>> byData(@PathVariable LocalDateTime data) {
        List<HistoricoStatusResponse> historicoResponses = historicoStatusService.getHistoricoStatusByDataAlteracao(data);
        return ResponseEntity.ok(historicoResponses);
    }

}

