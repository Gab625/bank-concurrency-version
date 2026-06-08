package com.prado.conta_bancaria_concorrencia.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prado.conta_bancaria_concorrencia.dto.TransacaoDTO;
import com.prado.conta_bancaria_concorrencia.model.ContaBancariaVersionada;
import com.prado.conta_bancaria_concorrencia.service.ContaBancariaVersionadaService;

@RestController
@RequestMapping("/contas-versionadas")
public class ContaBancariaVersionadaController {
	private final ContaBancariaVersionadaService service;

    public ContaBancariaVersionadaController(ContaBancariaVersionadaService service) {
        this.service = service;
    }
    
    @PostMapping("/inicializar")
    public ContaBancariaVersionada inicializar() {
    	return service.criarContaVersionadaInicial();
    }

    @PostMapping("/{id}/deposito")
    public String deposito(@PathVariable Long id, @RequestBody TransacaoDTO dto) {
        service.depositar(id, dto.getValor());
        return "Depósito de R$: " + dto.getValor() + " processado com sucesso (Otimista).";
    }

    @PostMapping("/{id}/saque")
    public String saque(@PathVariable Long id, @RequestBody TransacaoDTO dto) {
        service.sacar(id, dto.getValor());
        return "Saque de R$: " + dto.getValor() + " processado com sucesso (Otimista).";
    }
}
