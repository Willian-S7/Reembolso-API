package com.wil.SistemaReembolsos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wil.SistemaReembolsos.model.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

}
