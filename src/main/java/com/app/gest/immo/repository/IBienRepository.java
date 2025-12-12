package com.app.gest.immo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.gest.immo.entities.Bien;
import com.app.gest.immo.entities.CategorieBien;
import com.app.gest.immo.enumeration.EEtatBien;

public interface IBienRepository extends JpaRepository<Bien, Long>{

	Optional<Bien> findBienByCode(String code);
	List<Bien> findBienByNom(String nom);
	List<Bien> findByEtatBien(EEtatBien etatBien);
	List<Bien> findByCategorieBien(CategorieBien categorieBien);

}
