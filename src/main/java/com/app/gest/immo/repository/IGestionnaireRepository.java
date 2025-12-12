package com.app.gest.immo.repository;

import com.app.gest.immo.entities.Client;
import com.app.gest.immo.entities.Gestionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IGestionnaireRepository extends JpaRepository<Gestionnaire, Long>{
	
	List<Gestionnaire> findGestionnaireByCode(String code);
	List<Gestionnaire> findByNom(String nom);

}
