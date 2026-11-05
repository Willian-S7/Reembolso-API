package com.wil.SistemaReembolsos.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.wil.SistemaReembolsos.model.StatusPedido;

public record PedidoResponse(
    Long id,
    String funcionarioNome,
    Long funcionarioId,
    String descricao,
    StatusPedido statusAtual,
    BigDecimal valor,
    LocalDateTime dataCriacao,
    List<HistoricoStatusResponse> historicoStatus

) {

}
