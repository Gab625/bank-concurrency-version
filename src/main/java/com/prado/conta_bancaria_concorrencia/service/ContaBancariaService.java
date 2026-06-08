package com.prado.conta_bancaria_concorrencia.service;


import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.prado.conta_bancaria_concorrencia.model.ContaBancaria;
import com.prado.conta_bancaria_concorrencia.repository.ContaBancariaRepository;

import jakarta.transaction.Transactional;

@Service
public class ContaBancariaService {
	
	private final ContaBancariaRepository repository;
	
	public ContaBancariaService(ContaBancariaRepository repository) {
		this.repository = repository;
	}
	
	@Transactional
	public void depositar(Long id, BigDecimal valor) {
		ContaBancaria conta = repository.findById(id)
				.orElseThrow(()-> new RuntimeException("Conta não encontrada"));
		
		BigDecimal novoSaldo = conta.getSaldo().add(valor);
		conta.setSaldo(novoSaldo);
		
		repository.save(conta);
	}
	
	@Transactional
	public void sacar(Long id, BigDecimal valor) {
		ContaBancaria conta = repository.findById(id)
				.orElseThrow(()-> new RuntimeException("Conta não encontrada"));
		
		BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
		conta.setSaldo(novoSaldo);
		
		repository.save(conta);
	}
	
	@Transactional
    public ContaBancaria criarContaInicial() {
        return repository.save(new ContaBancaria("Gabriel Prado", new BigDecimal("1000.00")));
    }

}
