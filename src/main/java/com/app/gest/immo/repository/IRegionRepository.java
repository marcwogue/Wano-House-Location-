package com.app.gest.immo.repository;

import com.app.gest.immo.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRegionRepository extends JpaRepository<Region, Long>{
	
	List<Region> findRegionByCode(String code);

}
