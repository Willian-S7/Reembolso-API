package com.wil.SistemaReembolsos.dto.request;

import com.wil.SistemaReembolsos.model.StatusPedido;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoAlterarDTO(
    @Positive (message = "O id do funcionario não pode ser invalido")
    Long funcionarioId,
    @NotNull (message = "Status do pedido invalido")
    StatusPedido statusNovo
) {
    
}
