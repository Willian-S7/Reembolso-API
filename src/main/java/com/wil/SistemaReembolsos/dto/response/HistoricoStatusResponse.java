package com.wil.SistemaReembolsos.dto.response;

import java.time.LocalDateTime;

import com.wil.SistemaReembolsos.model.StatusPedido;

public record HistoricoStatusResponse(
    Long id,  
    Long pedidoId,
    String funcionarioNome,
    Long funcionarioId,
    StatusPedido statusAnterior,
    StatusPedido statusNovo,
    String observacao,
    LocalDateTime dataAlteracao
) {

}
