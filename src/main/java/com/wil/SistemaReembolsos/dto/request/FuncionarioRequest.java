package com.wil.SistemaReembolsos.dto.request;

import com.wil.SistemaReembolsos.model.Cargo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FuncionarioRequest(
    @NotBlank (message = "Nome invalido")
    String nome,
    @NotNull (message = "Cargo invalido ou nulo")
    Cargo cargo
) {

}
