package com.app.gest.immo.controller;

import com.app.gest.immo.dto.PersonneDTO;
import com.app.gest.immo.entities.Client;
import com.app.gest.immo.entities.Gestionnaire;
import com.app.gest.immo.entities.Personne;
import com.app.gest.immo.service.IPersonne;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.app.gest.immo.config.tools.GetIp;

@RestController
@RequestMapping("/api/personne")
@Tag(name = "Personne Bien Controller", description = "Exemple d'API documentée")
@CrossOrigin
public class PersonneController {

	@Autowired
	IPersonne iPersonne;

	@PostMapping("/savePersonne")
	ResponseEntity<Personne> savePersonne(@RequestBody PersonneDTO personneDTO, HttpServletRequest request) throws Exception {
        String Ip = GetIp(request);
		Personne categorie = iPersonne.save(personneDTO,Ip);
		return ResponseEntity.ok(categorie);
	}

	@PostMapping("/saveClient")
	ResponseEntity<Client> saveClient(@RequestBody PersonneDTO personneDTO,HttpServletRequest request) throws Exception {
        String Ip = GetIp(request);
		Client categorie = iPersonne.saveClient(personneDTO,Ip);
		return ResponseEntity.ok(categorie);
	}

	@PostMapping("/saveGestionnaire")
	ResponseEntity<Gestionnaire> saveGestionnaire(@RequestBody PersonneDTO personneDTO, HttpServletRequest request) throws Exception {
        String Ip = GetIp(request);
		Gestionnaire categorie = iPersonne.savGestionnaire(personneDTO,Ip);
		return ResponseEntity.ok(categorie);
	}

	@GetMapping("/list")
	ResponseEntity<Set<PersonneDTO>> list() throws Exception {
		Set<PersonneDTO> list = iPersonne.list();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/listClient")
	ResponseEntity<Set<PersonneDTO>> listClient() throws Exception {
		Set<PersonneDTO> list = iPersonne.listAllClient();
		return ResponseEntity.ok(list);
	}

	@GetMapping("/listGestionnaire")
	ResponseEntity<List<PersonneDTO>> listGestionnaire() throws Exception {
		List<PersonneDTO> list = iPersonne.listAllGestionnaire();
		return ResponseEntity.ok(list);
	}

	@PutMapping("/update/{id}")
	ResponseEntity<Personne> update(@PathVariable Long id, @RequestBody PersonneDTO categorieDTO) throws Exception {
		Personne categorie = iPersonne.update(id, categorieDTO);
		return ResponseEntity.ok(categorie);
	}

	@DeleteMapping("/delete")
	ResponseEntity<String> delete(@RequestBody Personne categorie) throws Exception {
		iPersonne.delete(categorie);
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("delete/{id}")
	ResponseEntity<String> deleteById(@PathVariable Long id) throws Exception {
		iPersonne.deleteById(id);
		return ResponseEntity.ok("success");
	}

	@DeleteMapping("deleteByCode/{code}")
	ResponseEntity<String> deleteByCode(@PathVariable String code) throws Exception {
		iPersonne.deleteByCode(code);
		return ResponseEntity.ok("success");
	}

	@GetMapping("/findByCode/{code}")
	ResponseEntity<PersonneDTO> findByCode(@PathVariable String code) throws Exception {
		PersonneDTO categorie = iPersonne.findByCode(code);
		return ResponseEntity.ok(categorie);
	}

	@PutMapping("/updateByCode/{code}")
	ResponseEntity<Personne> updateByCode(@PathVariable String code,@RequestBody PersonneDTO personneDTO) throws Exception {
		Personne categorie = iPersonne.updateByCode(code,personneDTO);
		return ResponseEntity.ok(categorie);
	}

}
