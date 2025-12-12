package com.app.gest.immo.repository;

import com.app.gest.immo.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVilleRepository extends JpaRepository<Ville, Long>{
	
	List<Ville> findVilleByCode(String code);

}
