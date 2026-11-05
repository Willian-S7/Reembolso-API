package com.wil.SistemaReembolsos.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wil.SistemaReembolsos.dto.request.PedidoAlterarDTO;
import com.wil.SistemaReembolsos.dto.request.PedidoRequest;
import com.wil.SistemaReembolsos.dto.response.PedidoResponse;
import com.wil.SistemaReembolsos.dto.response.ResumoResponse;
import com.wil.SistemaReembolsos.service.PedidoService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping 
    public ResponseEntity<PedidoResponse> createPedido(@RequestBody @Valid PedidoRequest pedidoRequest) {
        PedidoResponse pedidoResponse = pedidoService.registrarReembolso(pedidoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoResponse);
    }

    @PutMapping ("/{id}")
    public ResponseEntity<PedidoResponse> alterarStatusPedido(@PathVariable Long id, @RequestBody @Valid PedidoAlterarDTO pedidoAlterarDto) {
        PedidoResponse pedidoResponse = pedidoService.alterarPedido(id, pedidoAlterarDto, pedidoAlterarDto.statusNovo());
        return ResponseEntity.ok(pedidoResponse);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<PedidoResponse> buscarPedido(@PathVariable Long id) {
        PedidoResponse pedidoResponse = pedidoService.getPedidoById(id);
        return ResponseEntity.ok(pedidoResponse);
    }

    @GetMapping 
    public ResponseEntity<List<PedidoResponse>> listarPedidos() {
        List<PedidoResponse> pedidoResponses = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidoResponses);
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deletarPedido(@PathVariable Long id, @RequestParam Long funcionarioId) {
        pedidoService.deletePedido(id, funcionarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/limite-mensal/{id}")
    public ResponseEntity<BigDecimal> limiteMensalReembolso(@PathVariable Long id, LocalDateTime data){
        BigDecimal calculo = pedidoService.calcularTotalMensal(id, data);
        return ResponseEntity.ok(calculo);
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoResponse> gerarResumo(
            @RequestParam Long funcionarioId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFinal) {
        ResumoResponse resumoResponse = pedidoService.gerarResumoDTO(funcionarioId, dataInicio, dataFinal);
        return ResponseEntity.ok(resumoResponse);
    }

    @PatchMapping ("/cancelar/{id}")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable("id") Long idPedido, @RequestParam Long idFuncionario) {
        PedidoResponse pedidoResponse = pedidoService.cancelarPedido(idPedido, idFuncionario);
        return ResponseEntity.ok(pedidoResponse);
    }

}
