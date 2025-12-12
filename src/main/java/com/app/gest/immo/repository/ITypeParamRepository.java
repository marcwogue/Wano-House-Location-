package com.app.gest.immo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.gest.immo.entities.TypeParametre;

public interface ITypeParamRepository extends JpaRepository<TypeParametre, String>{

	Optional<TypeParametre> findTypeParametreByCode(String code);

}
