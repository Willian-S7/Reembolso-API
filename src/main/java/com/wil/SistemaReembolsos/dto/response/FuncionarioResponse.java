package com.wil.SistemaReembolsos.dto.response;

import java.math.BigDecimal;

import com.wil.SistemaReembolsos.model.Cargo;

public record FuncionarioResponse(
    Long id,
    String nome,
    Cargo cargo,
    BigDecimal LimiteMensal
) {

}
