package com.wil.SistemaReembolsos.exception;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.wil.SistemaReembolsos.dto.response.ErroResponse;

@RestControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler (ResponseStatusException.class)
    public ResponseEntity<ErroResponse> tratarErroNegocio(ResponseStatusException erro) {

        ErroResponse erroResponse = new ErroResponse(
            LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de regra de negócio", 
            List.of(erro.getMessage()));

        return ResponseEntity.status(erro.getStatusCode()).body(erroResponse);
        
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarErroValidacao(MethodArgumentNotValidException erro){

    List<String> mensagens = erro.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).toList();

        ErroResponse erroResponse = new ErroResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de Validação", mensagens);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }

}
 