package com.wil.SistemaReembolsos.dto.request;

import java.math.BigDecimal;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record PedidoRequest(
    @Positive(message = "O id do funcionario não pode ser invalido")
    Long funcionarioId,
    @NotBlank (message = "A descrição não pode ser vazia")
    String descricao,
    @Positive (message = "O valor deve ser positivo")
    BigDecimal valor

) {

}
