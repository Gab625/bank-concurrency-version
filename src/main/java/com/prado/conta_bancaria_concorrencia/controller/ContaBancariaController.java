package com.prado.conta_bancaria_concorrencia.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prado.conta_bancaria_concorrencia.dto.TransacaoDTO;
import com.prado.conta_bancaria_concorrencia.model.ContaBancaria;
import com.prado.conta_bancaria_concorrencia.service.ContaBancariaService;

@RestController
@RequestMapping("/contas")
public class ContaBancariaController {
	
	private final ContaBancariaService service;
	
	public ContaBancariaController(ContaBancariaService service) {
		this.service = service;
	}
	
	@PostMapping("/inicializar")
	public ContaBancaria inicializar() {
		return service.criarContaInicial();
	}
	
	@PostMapping("/{id}/deposito")
	public String deposito(@PathVariable Long id, @RequestBody TransacaoDTO dto) {
	    service.depositar(id, dto.getValor());
	    return "Deposito de R$: " + dto.getValor() + " processado.";
	}
	
	@PostMapping("/{id}/saque")
	public String saque(@PathVariable Long id, @RequestBody TransacaoDTO dto) {
	    service.sacar(id, dto.getValor());
	    return "Valor de R$: " + dto.getValor() + " processado.";
	}
}
