package com.prado.conta_bancaria_concorrencia.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.prado.conta_bancaria_concorrencia.model.ContaBancariaVersionada;
import com.prado.conta_bancaria_concorrencia.repository.ContaBancariaVersionadaRepository;

import jakarta.transaction.Transactional;

@Service
public class ContaBancariaVersionadaService {

	private final ContaBancariaVersionadaRepository repository;

    public ContaBancariaVersionadaService(ContaBancariaVersionadaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        ContaBancariaVersionada conta = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        conta.setSaldo(conta.getSaldo().add(valor));
        repository.save(conta);
    }

    @Transactional
    public void sacar(Long id, BigDecimal valor) {
        ContaBancariaVersionada conta = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        
        conta.setSaldo(conta.getSaldo().subtract(valor));
        repository.save(conta);
    }
    
    @Transactional
    public ContaBancariaVersionada criarContaVersionadaInicial() {
        return repository.save(new ContaBancariaVersionada("Gabriel Prado", new BigDecimal("1000.00")));
    }
}
