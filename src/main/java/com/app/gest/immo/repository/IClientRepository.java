package com.app.gest.immo.repository;

import com.app.gest.immo.entities.Client;
import com.app.gest.immo.entities.Personne;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IClientRepository extends JpaRepository<Client, Long>{
	
	List<Client> findPersonneByCode(String code);
	List<Client> findByNom(String nom);

}
