package com.wil.SistemaReembolsos.dto.response;

import java.math.BigDecimal;

public record ResumoResponse(
    String nomeFuncionario,
    Long idFuncionario,
    Long quantidadePedidos,
    BigDecimal totalAprovado,
    BigDecimal totalRejeitado
) {
}