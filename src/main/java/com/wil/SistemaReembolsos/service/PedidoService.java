package com.wil.SistemaReembolsos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.wil.SistemaReembolsos.dto.request.PedidoAlterarDTO;
import com.wil.SistemaReembolsos.dto.request.PedidoRequest;
import com.wil.SistemaReembolsos.dto.response.PedidoResponse;
import com.wil.SistemaReembolsos.dto.response.ResumoResponse;
import com.wil.SistemaReembolsos.model.Cargo;
import com.wil.SistemaReembolsos.model.Funcionario;
import com.wil.SistemaReembolsos.model.HistoricoStatus;
import com.wil.SistemaReembolsos.model.Pedido;
import com.wil.SistemaReembolsos.model.StatusPedido;
import com.wil.SistemaReembolsos.repository.PedidoRepository;
import com.wil.SistemaReembolsos.specification.ResumoSpecification;

import jakarta.transaction.Transactional;

@Service 
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final FuncionarioService funcionarioService;
    private final HistoricoStatusService historicoStatusService;
    

    public PedidoService(PedidoRepository pedidoRepository, FuncionarioService funcionarioService, HistoricoStatusService historicoStatusService) {
        this.pedidoRepository = pedidoRepository;
        this.funcionarioService = funcionarioService;
        this.historicoStatusService = historicoStatusService;
    }

    public PedidoResponse toDTO(Pedido pedido) {
        return new PedidoResponse(
            pedido.getId(),
            pedido.getFuncionario().getNome(),
            pedido.getFuncionario().getId(),
            pedido.getDescricao(),
            pedido.getStatusAtual(),
            pedido.getValor(),
            pedido.getDataCriacao(),
            pedido.getHistoricoStatus().stream()
                .map(historico -> historicoStatusService.toDTO(historico))
                .toList()
        );
    }

    @Transactional 
    public PedidoResponse registrarReembolso(PedidoRequest pedidoRequest) {
        Pedido pedido = new Pedido();
        Funcionario funcionario  = funcionarioService.getFuncionarioById(pedidoRequest.funcionarioId());

        pedido.setFuncionario(funcionario);
        pedido.setDescricao(pedidoRequest.descricao());
        pedido.setValor(pedidoRequest.valor());
        pedido.setStatusAtual(StatusPedido.PENDENTE);
        pedido.setDataCriacao(LocalDateTime.now());

        //valor mensal calculado antigo
        BigDecimal totalMensal = calcularTotalMensal(funcionario.getId(), LocalDateTime.now());

        //valor mensal + o novo pedido
        BigDecimal totalComNovoPedido = totalMensal.add(pedidoRequest.valor());

        if (totalComNovoPedido.compareTo(funcionario.getLimiteMensal()) > 0) {
            pedido.setStatusAtual(StatusPedido.REJEITADO);
            System.out.println("Limite Mensal atingido, pedido Recusado");
        }

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        HistoricoStatus historico = historicoStatusService.registrarAlteracao(pedido, funcionario, pedido.getStatusAtual());

        pedidoSalvo.getHistoricoStatus().add(historico);

        return toDTO(pedidoSalvo);
    }

    @Transactional 
    public PedidoResponse alterarPedido(Long idPedido, PedidoAlterarDTO pedidoAlterarDTO, StatusPedido statusNovo) {

        Pedido pedido = pedidoRepository.findById(idPedido).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Funcionario funcionario = funcionarioService.getFuncionarioById(pedidoAlterarDTO.funcionarioId());

        if (funcionario.getCargo() != Cargo.GERENTE) {
        throw new IllegalArgumentException("Apenas gestores podem alterar pedidos");
        }

        if (statusNovo == pedido.getStatusAtual()) {
            throw new IllegalArgumentException("O status novo não pode ser igual ao status atual");
        }

        if (pedido.getStatusAtual() == StatusPedido.PAGO || pedido.getStatusAtual() == StatusPedido.REJEITADO) {
            throw new IllegalArgumentException("Não é possível alterar o status de um pedido que já foi pago ou reprovado");
        }

        if (pedido.getStatusAtual().equals(StatusPedido.CANCELADO)) {
            throw new IllegalArgumentException("Não é possivel cancelar ou alterar pedidos cancelados por este metodo");
            }

        if (statusNovo.equals(StatusPedido.CANCELADO)) {
            throw new IllegalArgumentException("Não é possivel cancelar ou alterar pedidos cancelados por este metodo");
            }

        StatusPedido statusAnterior = pedido.getStatusAtual();

        pedido.setStatusAtual(statusNovo);

        pedido.getHistoricoStatus().add(historicoStatusService.alterarStatus(pedido, funcionario, statusAnterior, statusNovo, "Alteração de status realizada pelo funcionário " + funcionario.getNome()));

        return toDTO(pedidoRepository.save(pedido));
    }


    public PedidoResponse getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return toDTO(pedido);
    }


    public void deletePedido(Long id, Long funcionarioId) {

        Funcionario funcionario = funcionarioService.getFuncionarioById(funcionarioId);

        if (funcionario.getCargo() != Cargo.GERENTE) {
        throw new IllegalArgumentException("Apenas gestores podem alterar pedidos");
        }       

        Pedido pedido = pedidoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedidoRepository.delete(pedido);
    }


    public List<PedidoResponse> listarPedidos() {
        return pedidoRepository.findAll().stream()
            .map(pedido -> toDTO(pedido))
            .toList();
    }


    public ResumoResponse gerarResumoDTO(Long funcionarioId, LocalDate dataInicio, LocalDate dataFinal) {
        Funcionario funcionario = funcionarioService.getFuncionarioById(funcionarioId);

        Specification<Pedido> specification = Specification.unrestricted();

        if (dataInicio != null && dataFinal != null && dataInicio.isAfter(dataFinal)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final");
        }

        if (funcionarioId != null) {
            specification = specification.and(ResumoSpecification.porFuncionario(funcionarioId));
        }

        if (dataInicio != null) {
            specification = specification.and(ResumoSpecification.porDataInicial(dataInicio));
        }

        if (dataFinal != null) {
            specification = specification.and(ResumoSpecification.porDataFinal(dataFinal));
        }
        
        List<Pedido> pedidos = pedidoRepository.findAll(specification);
        
        Long totalPedidos = (long) pedidos.size();
        BigDecimal totalValorPago = pedidos.stream()
            .filter(pedido -> pedido.getStatusAtual() == StatusPedido.PAGO)
            .map(pedido -> pedido.getValor())
            .reduce(BigDecimal.ZERO, (total, pedidoValor) -> total.add(pedidoValor));
      
        BigDecimal totalValorRejeitados = pedidos.stream()
            .filter(pedido -> pedido.getStatusAtual() == StatusPedido.REJEITADO)
            .map(pedido -> pedido.getValor())
            .reduce(BigDecimal.ZERO, (total, pedidoValor) -> total.add(pedidoValor));

        ResumoResponse resumo = new ResumoResponse(
            funcionario.getNome(),
            funcionario.getId(),
            totalPedidos,
            totalValorPago,
            totalValorRejeitados
        );
        return resumo;
    }


    public List<Pedido> filtroMensal(Long idFuncionario, LocalDateTime dataCriacao) {
        YearMonth mesReferencia = YearMonth.from(dataCriacao);
        LocalDateTime inicioMes = mesReferencia.atDay(1).atStartOfDay();
        LocalDateTime finalMes = mesReferencia.plusMonths(1).atDay(1).atStartOfDay();
        return pedidoRepository.findByFuncionarioIdAndDataCriacaoBetween(idFuncionario, inicioMes, finalMes);
    }


    public BigDecimal calcularTotalMensal(Long id, LocalDateTime dataCriacao) {
        List<Pedido> filtro = filtroMensal(id, dataCriacao);
        BigDecimal calculoMensal = filtro.stream()
            .filter(pedido -> pedido.getStatusAtual() != StatusPedido.REJEITADO && pedido.getStatusAtual() != StatusPedido.CANCELADO)
            .map(pedido -> pedido.getValor())
            .reduce(BigDecimal.ZERO, (total, valor) -> total.add(valor));
        return calculoMensal;
    }

    @Transactional 
    public PedidoResponse cancelarPedido(Long idPedido, Long idFuncionario) {
        Pedido pedidoCancelar = pedidoRepository.findById(idPedido)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        Funcionario funcionario = funcionarioService.getFuncionarioById(idFuncionario);

        if (pedidoCancelar.getStatusAtual() != StatusPedido.PENDENTE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido deve estar pendente para ser cancelado");
        }

        if (pedidoCancelar.getStatusAtual() == StatusPedido.CANCELADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido já foi cancelado");
        }
        if (funcionario.getCargo().equals(Cargo.FUNCIONARIO)) {

            if (!pedidoCancelar.getFuncionario().getId().equals(funcionario.getId())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionarios só podem cancelar seus próprios pedidos");
            }

            if(LocalDateTime.now().isAfter(pedidoCancelar.getDataCriacao().plusHours(48))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O cancelamento só é permitido antes de completar 48 horas");
            }
        }
        
        if (funcionario.getCargo().equals(Cargo.GERENTE)){
            if(LocalDateTime.now().isAfter(pedidoCancelar.getDataCriacao().plusHours(168))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é possivel cancelar após 7 dias do pedido");
            }
        }

        StatusPedido statusAnterior = pedidoCancelar.getStatusAtual();

        pedidoCancelar.setStatusAtual(StatusPedido.CANCELADO);

        pedidoCancelar.getHistoricoStatus().add(historicoStatusService.alterarStatus(pedidoCancelar, funcionario, statusAnterior, pedidoCancelar.getStatusAtual(), "Alteração de status realizada pelo funcionário " + funcionario.getNome()));
        pedidoRepository.save(pedidoCancelar);

        return toDTO(pedidoCancelar);
    }
}
