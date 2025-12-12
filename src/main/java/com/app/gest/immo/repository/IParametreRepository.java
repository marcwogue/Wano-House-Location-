package com.app.gest.immo.repository;

import com.app.gest.immo.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IParametreRepository extends JpaRepository<Parametre, Long>{
	
	Optional<Parametre> findParametreByCode(String code);

	Optional<Parametre> findParametreByCodeAndTypeParam(String code, TypeParametre typeParam);
	List<Parametre> findByTypeParam(TypeParametre typeParam);

}
