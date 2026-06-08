package com.prado.conta_bancaria_concorrencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.prado.conta_bancaria_concorrencia.model.ContaBancariaVersionada;

@Repository
public interface ContaBancariaVersionadaRepository extends JpaRepository<ContaBancariaVersionada, Long>{
	
}
