package com.prado.conta_bancaria_concorrencia.exception;

import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ControllerAdvice
public class GerenciadorDeExceptions {
	
	@ExceptionHandler({ObjectOptimisticLockingFailureException.class, StaleObjectStateException.class})
    public ResponseEntity<String> tratarErroDeConcorrenciaOtimista() {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Erro de Concorrência: A conta foi alterada por outro usuário. Por favor, tente novamente.");
    }
}
