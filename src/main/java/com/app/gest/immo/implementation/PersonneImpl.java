package com.app.gest.immo.implementation;

import java.time.ZoneId;
import java.util.*;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.config.securities.Status;
import com.app.gest.immo.config.securities.Utilisateur;
import com.app.gest.immo.entities.UtilsAPP;
import com.app.gest.immo.repository.IClientRepository;
import com.app.gest.immo.repository.IGestionnaireRepository;
import com.app.gest.immo.repository.IUtilisateurRepository;
import com.app.gest.immo.service.IGoupes;
import com.app.gest.immo.service.IUtilisateur;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import com.app.gest.immo.dto.PersonneDTO;
import com.app.gest.immo.entities.Client;
import com.app.gest.immo.entities.Gestionnaire;
import com.app.gest.immo.entities.Personne;
import com.app.gest.immo.mapper.IPersonneMapper;
import com.app.gest.immo.repository.IPersonneRepository;
import com.app.gest.immo.service.IPersonne;

@Service
public class PersonneImpl implements IPersonne {

	private final IPersonneRepository iPersonne;
	private final IClientRepository iClient;

	private final IGestionnaireRepository iGestionnaire;
	private final IPersonneMapper iPersonneMapper;
	private final IUtilisateur iUser;
	private final IGoupes iGoupes;
	private  static final String DEFAULTPASS ="123456987";
    private final IUtilisateurRepository iUtilisateurRepository;

    public PersonneImpl(IPersonneRepository iPersonne, IClientRepository iClient, IGestionnaireRepository iGestionnaire, IPersonneMapper iPersonneMapper, IUtilisateur iUser, IGoupes iGoupes, IUtilisateurRepository iUtilisateurRepository) {
		this.iPersonne = iPersonne;
		this.iClient = iClient;
		this.iGestionnaire = iGestionnaire;
		this.iPersonneMapper = iPersonneMapper;
        this.iUser = iUser;
        this.iGoupes = iGoupes;
        this.iUtilisateurRepository = iUtilisateurRepository;
    }

	@Override
	public Personne save(PersonneDTO personneDTO,String ip) throws Exception {
		Personne personne = setPersonne(personneDTO);
		Groupes groupes = iGoupes.findByNom("PROPRETAIRE");
		Utilisateur user = personToUser(personne,ip);
		if(groupes==null){
			groupes = defaultGroupes();
			groupes.setName("PROPRETAIRE");
			groupes.setLibelle("GROUPE PROPRETAIRE");
			groupes = iGoupes.save(groupes);
		}
		user.setGroupes(groupes);
		iUser.save(user,groupes.getId());
		personne.setCode("PRO"+personne.getCode());
		return iPersonne.save(personne);

	}



    @Override
	public Personne update(Long id, PersonneDTO personneDTO) throws Exception {
		Optional<Personne> personne = iPersonne.findById(id);
		if (personne.get() == null) {
			throw new Exception("Pas de Type Bien correspondant à cet id" + " -" + id);
		}
		Personne type = personne.get();
		type = setPersonne(personneDTO);
		return iPersonne.saveAndFlush(type);
	}




	@Override
	public Set<PersonneDTO> list() throws Exception {
		List<Personne> list = iPersonne.findAll();
		Set<PersonneDTO> setType = new HashSet<PersonneDTO>();
		if (list == null || list.isEmpty()) {
            System.out.println("empty box");
			return null;
		}

		for (Personne type : list) {
			setType.add(setPersonneDTO(type));
            System.out.println(type.getNom());
            System.out.println(setType.size());
		}
		return setType;
	}

	@Override
	public void delete(Personne personne) throws Exception {
		iPersonne.delete(personne);

	}

	@Override
	public void deleteById(Long id) throws Exception {
		Optional<Personne> personne = iPersonne.findById(id);
		if (personne.get() == null) {
			throw new Exception("Pas de Type Bien correspondant à cet id" + " -" + id);
		}
		Personne type = personne.get();
		iPersonne.delete(type);

	}

	@Override
	public void deleteByCode(String code) throws Exception {
		List<Personne> personne = iPersonne.findPersonneByCode(code);
		if (personne == null || personne.isEmpty()) {
			throw new Exception("Pas de Type Bien correspondant à cet id" + " -" + code);
		}
		Personne type = personne.get(0);
		iPersonne.delete(type);

	}

	PersonneDTO setPersonneDTO(Personne personne) {
        if ( personne == null ) {
            return null;
        }

        PersonneDTO personneDTO = new PersonneDTO();

        personneDTO.setId( personne.getId() );
        personneDTO.setNom(personne.getNom());
        personneDTO.setProfession(personne.getProfession());
        personneDTO.setEmail(personne.getEmail());
        personneDTO.setDateCreation(personne.getDateCreation());
        personneDTO.setUtiCreation(personne.getUtiCreation());
        personneDTO.setLieuNaissance(personne.getLieuNaissance());
        personneDTO.setDateModif(personne.getDateModif());
        personneDTO.setDateNaissance(personne.getDateNaissance());
        personneDTO.setTelephone2(personne.getTelephone2());
        personneDTO.setTelephone1(personne.getTelephone1());
        personneDTO.setDateModif(personne.getDateModif());
        personneDTO.setCode(personne.getCode());
        personneDTO.setPrenom(personne.getPrenom());
        personneDTO.setSexe(personne.getSexe());
        personneDTO.setDateNomination(personne.getDateCreation());
        return personneDTO;

	}

	Personne setPersonne(PersonneDTO personneDTO) {
		Personne personne = new Personne();
		personne.setSexe(personneDTO.getSexe());
		personne.setCode(UtilsAPP.genCodeNum());
        setter(personneDTO, personne);
        personne.setProfession(personneDTO.getProfession());
        personne.setUtiCreation(personneDTO.getUtiCreation());
        personne.setEmail(personneDTO.getEmail());

		return personne;
	}

    private void setter(PersonneDTO personneDTO, Personne personne) {
        personne.setPrenom(personneDTO.getPrenom());
        personne.setNom(personneDTO.getNom());
        personne.setDateNaissance(personneDTO.getDateNaissance());
        personne.setLieuNaissance(personneDTO.getLieuNaissance());
        personne.setTelephone1(personneDTO.getTelephone1());
        personne.setTelephone2(personneDTO.getTelephone2());
        personne.setUtiCreation(personneDTO.getUtiCreation());
        personne.setProfession(personneDTO.getProfession());
    }

    Gestionnaire setGestionnaire(PersonneDTO personneDTO) {
        Gestionnaire personne = new Gestionnaire();
        personne.setSexe(personneDTO.getSexe());
        personne.setCode(UtilsAPP.genCode("GEST", 8));
        personne.setEmail(personneDTO.getEmail());
        personne.setPrenom(personneDTO.getPrenom());
        personne.setProfession(personneDTO.getProfession());
        personne.setDateNomination(personneDTO.getDateNomination());
        personne.setNom(personneDTO.getNom());
        personne.setDateNaissance(personneDTO.getDateNaissance());
        personne.setLieuNaissance(personneDTO.getLieuNaissance());
        personne.setTelephone1(personneDTO.getTelephone1());
        personne.setTelephone2(personneDTO.getTelephone2());
        personne.setUtiCreation(personneDTO.getUtiCreation());
        return personne;
    }

	@Override
	public Client setClient(PersonneDTO personneDTO) {
		Client personne = new Client();
		personne.setSexe(personneDTO.getSexe());
		personne.setCode(UtilsAPP.genCode(personneDTO.getCode(), 8));
		personne.setEmail(personneDTO.getEmail());
        setter(personneDTO, personne);
        personne.setDateModif(personneDTO.getDateModif());
        personne.setTypeClient(personneDTO.getTypeClient());
        personne.setPiecesRecto(personneDTO.getPiecesRecto());
        personne.setPieceVerso(personneDTO.getPieceVerso());
		return personne;
	}

	@Override
	public PersonneDTO findByCode(String code) throws Exception {
		Personne type = iPersonne.findPersonneByCode(code).get(0);
		return setPersonneDTO(type);
	}

    @Override
    public Personne updateByCode(String code, PersonneDTO personneDTO) throws Exception {
        Personne type = iPersonne.findPersonneByCode(code).get(0);
        type.setProfession(personneDTO.getProfession());
        type.setCode(personneDTO.getCode());
        type.setEmail(personneDTO.getEmail());
        type.setUtiCreation(personneDTO.getUtiCreation());
        type.setId(personneDTO.getId());
        type.setDateNaissance(personneDTO.getDateNaissance());
        type.setPrenom(personneDTO.getPrenom());
        type.setDateModif(personneDTO.getDateModif());
        type.setEmail(personneDTO.getEmail());
        type.setLieuNaissance(personneDTO.getLieuNaissance());
        type.setTelephone1(personneDTO.getTelephone1());
        type.setTelephone2(personneDTO.getTelephone2());
        type.setSexe(personneDTO.getSexe());
        return iPersonne.saveAndFlush(type);
    }

	@Override
	public Client saveClient(PersonneDTO personneDTO, String Ip) throws Exception {
		Client client = setClient(personneDTO);
		Personne personne = setPersonne(personneDTO);
		Utilisateur user = personToUser(personne , Ip);
		Groupes groupes = iGoupes.findByNom("LOCATAIRE");
		if(groupes==null){
			groupes = iGoupes.save(defaultGroupes());
		}
		user.setGroupes(groupes);
		iUser.save(user,groupes.getId());
		return iPersonne.save(client);
	}

	@Override
	public Gestionnaire savGestionnaire(PersonneDTO personneDTO,String ip) throws Exception {
		Gestionnaire client = setGestionnaire(personneDTO);
		Personne personne = setPersonne(personneDTO);
		Utilisateur user = personToUser(personne,ip);
		Groupes groupes = iGoupes.findByNom("GESTIONNAIRE");
		if(groupes==null){
			groupes = defaultGroupes();
			groupes.setName("GESTIONNAIRE");
			groupes = iGoupes.save(groupes);
		}
		user.setGroupes(groupes);
		iUser.save(user,groupes.getId());
		return iPersonne.save(client);
	}

	Groupes defaultGroupes (){
		Groupes groupes = new Groupes();
		groupes.setDescription("GROUPES");
		groupes.setLibelle("LOCATAIRE");
		groupes.setName("LOCATAIRE");
		return groupes;
	}

	@Override
	public Set<PersonneDTO> listAllClient() {
		List<Client> list = iClient.findAll();
		Set<PersonneDTO> dtoList = new HashSet<>();
		for(Client gest : list){
			PersonneDTO personneDTO = new PersonneDTO();
            personneDTO.setId(gest.getId());
			personneDTO.setCode(gest.getCode());
			personneDTO.setEmail(gest.getEmail());
			personneDTO.setTelephone1(gest.getTelephone1());
			personneDTO.setNom(gest.getNom());
			personneDTO.setSexe(gest.getSexe());
			personneDTO.setDateNaissance(gest.getDateNaissance());
			personneDTO.setPrenom(gest.getPrenom());
            personneDTO.setRefFiscale(gest.getRefFiscale());
            personneDTO.setDateCreation(gest.getDateCreation());
            personneDTO.setUtiCreation(gest.getUtiCreation());
            personneDTO.setPiecesRecto(gest.getPiecesRecto());
            personneDTO.setPieceVerso(gest.getPieceVerso());
            personneDTO.setTelephone2(gest.getTelephone2());
            personneDTO.setDateModif(gest.getDateModif());
            personneDTO.setLieuNaissance(gest.getLieuNaissance());
            personneDTO.setTypeClient(gest.getTypeClient());
            personneDTO.setProfession(gest.getProfession());

			dtoList.add(personneDTO);


		}
		return dtoList;
	}

	@Override
	public List<PersonneDTO> listAllGestionnaire() {
		List<Gestionnaire> list = iGestionnaire.findAll();
		List<PersonneDTO> dtoList = new ArrayList<>();
		for(Gestionnaire gest : list){
			PersonneDTO personneDTO = new PersonneDTO();
			personneDTO.setCode(gest.getCode());
            personneDTO.setDateNomination(gest.getDateNomination());
            personneDTO.setLieuNaissance(gest.getLieuNaissance());
            personneDTO.setDateCreation(gest.getDateCreation());
            personneDTO.setProfession(gest.getProfession());
            personneDTO.setTelephone2(gest.getTelephone2());
			personneDTO.setEmail(gest.getEmail());
			personneDTO.setTelephone1(gest.getTelephone1());
			personneDTO.setNom(gest.getNom());
			personneDTO.setSexe(gest.getSexe());
			personneDTO.setDateNaissance(gest.getDateNaissance());
			personneDTO.setPrenom(gest.getPrenom());
			dtoList.add(personneDTO);

		}
		return dtoList;
	}

	Utilisateur personToUser(@NotNull("le nouvel utilisateur ne peut pas etre null") Personne personne,String ip){
		Utilisateur user = new Utilisateur();
		user.setLogin(personne.getEmail());
        if (personne.getDateCreation()!=null) {
            user.setDateCreation(personne.getDateCreation().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }
       user.setUtiCreation(personne.getUtiCreation());
        if (personne.getDateModif() != null) {
            user.setDateModif(personne.getDateModif().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

		user.setNom(personne.getNom());
        user.setiP(ip);
		user.setStatus(Status.ACTIF);
		user.setNumero(personne.getTelephone1());
		user.setEmail(personne.getEmail());
		user.setMaxSession(2);
		user.setPassWord(DEFAULTPASS);
		return user;
	}

}
