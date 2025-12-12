package com.app.gest.immo.service;

import java.util.List;
import java.util.Set;

import com.app.gest.immo.dto.PersonneDTO;
import com.app.gest.immo.entities.Client;
import com.app.gest.immo.entities.Gestionnaire;
import com.app.gest.immo.entities.Personne;
import jakarta.servlet.http.HttpServletRequest;

public interface IPersonne {

	Personne save(PersonneDTO personneDTO, String ip) throws Exception;

	Personne update(Long id, PersonneDTO personneDTO) throws Exception;

	Set<PersonneDTO> list() throws Exception;

	void delete(Personne personne) throws Exception;

	void deleteById(Long id) throws Exception;

	void deleteByCode(String code) throws Exception;

	PersonneDTO findByCode(String code) throws Exception;

	Personne updateByCode(String code,PersonneDTO personneDTO) throws Exception;

	Client saveClient(PersonneDTO personneDTO, String ip) throws Exception;

	Gestionnaire savGestionnaire(PersonneDTO personneDTO,String ip) throws Exception;

	Set<PersonneDTO> listAllClient();

	List<PersonneDTO> listAllGestionnaire();

	Client setClient(PersonneDTO personneDTO);

}
