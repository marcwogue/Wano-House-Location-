package com.app.gest.immo.service;

import com.app.gest.immo.dto.ParametreDTO;
import com.app.gest.immo.entities.Parametre;
import com.app.gest.immo.entities.TypeParametre;

import java.util.List;
import java.util.Set;

public interface IParametre {
	
	Parametre save(ParametreDTO parametreDTO) throws Exception;
	Parametre update (Long id, ParametreDTO parametreDTO) throws Exception;
	Set<ParametreDTO> list() throws Exception;
	void delete (Parametre parametre) throws Exception;
	void deleteById(Long id) throws Exception;
	void deleteByCode(String id) throws Exception;
	ParametreDTO findByCode(String code) throws Exception;
	Parametre updateByCode(String code, ParametreDTO parametreDTO) throws Exception;
	List<ParametreDTO> getAlläramByTypeParam(String codeType) throws Exception;
	Parametre findParametreByCodeAndTypeParam(String code, String typeParam) throws Exception;

}
