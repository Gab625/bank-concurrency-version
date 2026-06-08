package com.prado.conta_bancaria_concorrencia.dto;

import java.math.BigDecimal;

public class TransacaoDTO {
	
	private BigDecimal valor;

    public TransacaoDTO() {}

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

}
