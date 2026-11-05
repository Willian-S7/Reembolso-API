package com.wil.SistemaReembolsos.controller;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import com.wil.SistemaReembolsos.dto.request.FuncionarioRequest;
import com.wil.SistemaReembolsos.dto.response.FuncionarioResponse;
import com.wil.SistemaReembolsos.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController(FuncionarioService funcionarioService) {
        this.funcionarioService = funcionarioService;
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponse> createFuncionario(@RequestBody @Valid FuncionarioRequest funcionarioRequest) {
        FuncionarioResponse funcionarioResponse = funcionarioService.createFuncionario(funcionarioRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponse> updateFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioRequest funcionarioRequest) {
        FuncionarioResponse funcionarioResponse = funcionarioService.updateFuncionario(id, funcionarioRequest);
        return ResponseEntity.ok(funcionarioResponse);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<FuncionarioResponse> buscarFuncionario(@PathVariable Long id) {
        FuncionarioResponse funcionarioResponse = funcionarioService.getFuncionarioDTO(id);
        return ResponseEntity.ok(funcionarioResponse);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponse>> listarFuncionarios() {
        List<FuncionarioResponse> funcionarioResponses = funcionarioService.listFuncionarios();
        return ResponseEntity.ok(funcionarioResponses);
    }

    @PatchMapping ("/limite/{id}")
    public ResponseEntity<FuncionarioResponse> alterarLimite(@PathVariable Long id, BigDecimal limiteNovo){
        FuncionarioResponse funcionarioResponse = funcionarioService.alterarLimiteFuncionario(id, limiteNovo);
        return ResponseEntity.ok(funcionarioResponse);
    }


    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        funcionarioService.deleteFuncionario(id);
        return ResponseEntity.noContent().build();
    }

}
