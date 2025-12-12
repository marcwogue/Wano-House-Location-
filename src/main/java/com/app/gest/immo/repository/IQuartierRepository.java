package com.app.gest.immo.repository;

import com.app.gest.immo.entities.Quartier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IQuartierRepository extends JpaRepository<Quartier, Long>{
	
	List<Quartier> findQuartierByCode(String code);

}
