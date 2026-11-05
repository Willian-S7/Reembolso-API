package com.wil.SistemaReembolsos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wil.SistemaReembolsos.dto.request.FuncionarioRequest;
import com.wil.SistemaReembolsos.dto.response.FuncionarioResponse;
import com.wil.SistemaReembolsos.model.Funcionario;
import com.wil.SistemaReembolsos.repository.FuncionarioRepository;


@Service 
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public FuncionarioService(FuncionarioRepository funcionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
    }

    public FuncionarioResponse toDTO(Funcionario funcionario) {
        return new FuncionarioResponse(
            funcionario.getId(),
            funcionario.getNome(),
            funcionario.getCargo(),
            funcionario.getLimiteMensal()
        );
    }

    public FuncionarioResponse createFuncionario(FuncionarioRequest funcionarioRequest) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioRequest.nome());
        funcionario.setCargo(funcionarioRequest.cargo());
        funcionario.setLimiteMensal(new BigDecimal(2000));
        funcionarioRepository.save(funcionario);
        return toDTO(funcionario);
    }


    public Funcionario getFuncionarioById(Long id) {
        return funcionarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }


    public FuncionarioResponse getFuncionarioDTO(Long id) {
        Funcionario funcionario = getFuncionarioById(id);
        return toDTO(funcionario);
    }

    public List<FuncionarioResponse> listFuncionarios() {
        return funcionarioRepository.findAll().stream()
            .map(funcionario -> toDTO(funcionario))
            .toList();
    }

    public FuncionarioResponse updateFuncionario(Long id, FuncionarioRequest funcionarioRequest) {
        Funcionario funcionario = funcionarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        funcionario.setNome(funcionarioRequest.nome());
        funcionario.setCargo(funcionarioRequest.cargo());
        funcionarioRepository.save(funcionario);
        return toDTO(funcionario);
    }

    public void deleteFuncionario(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
        funcionarioRepository.delete(funcionario);
    }

    public FuncionarioResponse alterarLimiteFuncionario(Long id, BigDecimal limiteNovo) {
        Funcionario funcionario = getFuncionarioById(id);
        funcionario.setLimiteMensal(limiteNovo);
        return toDTO(funcionario);
    }

    
}
