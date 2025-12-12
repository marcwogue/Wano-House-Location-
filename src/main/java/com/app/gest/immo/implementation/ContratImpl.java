package com.app.gest.immo.implementation;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import com.app.gest.immo.dto.*;
import com.app.gest.immo.entities.*;
import com.app.gest.immo.enumeration.EEtatBien;
import com.app.gest.immo.enumeration.EPeriodicite;
import com.app.gest.immo.repository.*;
import com.app.gest.immo.service.IPersonne;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import com.app.gest.immo.enumeration.EEtatLoyer;
import com.app.gest.immo.service.IBien;
import com.app.gest.immo.service.IContrat;
import com.app.gest.immo.service.ILoyer;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContratImpl implements IContrat, ILoyer {

	private final IContratRepository contratRepository;
	private final IPersonneRepository personneRepository;
	private final IBienRepository bienService;
	private final IBien iBien;
	private final IClientRepository iClientRepository;
	private final ILoyerRepository loyerRepository;
	public ContratImpl(IContratRepository contratRepository, IPersonneRepository personneRepository, IBienRepository bienService,
					   IBien iBien, IGestionnaireRepository iGestionnaireRepository, IClientRepository iClientRepository, ILoyerRepository loyerRepository) {
		this.contratRepository = contratRepository;
		this.personneRepository = personneRepository;
		this.bienService = bienService;
		this.iBien = iBien;
		this.iClientRepository = iClientRepository;
		this.loyerRepository = loyerRepository;
	}

	@Override
	@Transactional
	public Contrat save(ContratDTO contratDTO) throws Exception {
		Contrat contrat = toEntity(contratDTO);
		Set<Loyer> list =generateLoyer(contrat);
		loyerRepository.saveAll(list);
		return contratRepository.save(contrat);
	}

	@Override
	public Contrat update(Long id, ContratDTO contratDTO) throws Exception {
		Optional<Contrat> optionalContrat = contratRepository.findById(id);
		if (!optionalContrat.isPresent()) {
			throw new Exception("Pas de Contrat correspondant à cet id: " + id);
		}
		Contrat contrat = optionalContrat.get();
		contrat= updateEntity(contrat, contratDTO);
		List<LoyerDTO> loyerListDTO = findLoyerByContrat(contrat);
		for(LoyerDTO loyerDTO : loyerListDTO){
			Loyer loyer = updateLoyerByCode(loyerDTO.getCode(), loyerDTO);
		}
		return contratRepository.saveAndFlush(contrat);
	}

	@Override
	public Set<ContratDTO> list() throws Exception {
		List<Contrat> contrats = contratRepository.findAll();
		Set<ContratDTO> contratDTOs = new HashSet<>();
		for (Contrat contrat : contrats) {
			contratDTOs.add(toDTO(contrat));
		}
		return contratDTOs;
	}

	@Override
	public void delete(Contrat contrat) throws Exception {
		contratRepository.delete(contrat);
	}

	@Override
	public void deleteById(Long id) throws Exception {
		contratRepository.deleteById(id);
	}

	@Override
	public void deleteContratByCode(String code) throws Exception {
		List<Contrat> contrat = contratRepository.findContratByCode(code);
		if(contrat==null || contrat.isEmpty()){
			throw new Exception("s");
		}
        List<Loyer> loyers = loyerRepository.findByContrat(contrat.get(0));
        for (int i = 0; i < loyers.toArray().length; i++) {
            loyerRepository.delete(loyers.get(i));
        }
        Optional<Bien> bien = bienService.findBienByCode(new ArrayList<>(contrat.get(0).getListBiens()).get(0).getCode());
        bien.get().setEtatBien(EEtatBien.Libre);
		contratRepository.delete(contrat.get(0));
	}

	@Override
	public ContratDTO findByCode(String code) throws Exception {
		List<Contrat> contrats = contratRepository.findContratByCode(code);
		if (contrats.isEmpty()) {
			throw new Exception("Pas de Contrat correspondant à ce code: " + code);
		}
		return toDTO(contrats.get(0));
	}

	@Override
	public Contrat updateByCode(String code, ContratDTO contratDTO) throws Exception {
		List<Contrat> contrats = contratRepository.findContratByCode(code);
		if (contrats.isEmpty()) {
			throw new Exception("Pas de Contrat correspondant à ce code: " + code);
		}
		Contrat contrat = contrats.get(0);
		 Contrat counter = updateEntity(contrat, contratDTO);
		return contratRepository.saveAndFlush(counter);
	}

	@Override
	public List<ContratDTO> listContratByBien(Bien bien) throws Exception {
		// List<Contrat> contrats = contratRepository.findContratByBien(bien);
		List<ContratDTO> contratDTOs = new ArrayList<>();
		/*
		 * for (Contrat contrat : contrats) {
		 * contratDTOs.add(toDTO(contrat));
		 * }
		 */
		return null;
	}

	Gestionnaire gestionnaire(Set<Bien> listBiens) {
		List<Bien> bienList = listBiens.stream().toList();
		Gestionnaire gestionnaire = (Gestionnaire) bienList.get(0).getGestionnaire();
		return gestionnaire;
	}

	Personne getPersonne(Set<Bien> listBiens) {
		List<Bien> bienList = listBiens.stream().toList();
		Personne personne = bienList.get(0).getProprietaire();
		return personne;
	}

	@Override
	public List<ContratDTO> findByDateMiseEnLoyer(Date dateMiseEnPlace) throws Exception {
		List<Contrat> contrats = contratRepository.findContratByDateMiseEnplace(dateMiseEnPlace);
		List<ContratDTO> contratDTOs = new ArrayList<>();
		for (Contrat contrat : contrats) {
			contratDTOs.add(toDTO(contrat));
		}
		return contratDTOs;
	}

	@Override
	public Set<Loyer> generateLoyer(Contrat contrat) throws Exception {
		Set<Loyer> loyers = new HashSet<>();

		if (contrat.getDurée()== null || contrat.getDurée() <= 0) {
			throw new Exception("La durée du contrat ne peut pas être nulle ou négative.");
		}

		if (contrat.getMontant() == null || contrat.getMontant() <= 0) {
			throw new Exception("Le montant ne peut pas être nul ou négatif.");
		}
		if(contrat.getMontantPercue()<contrat.getMontant()){
			throw new Exception("Le montant percue doit etre au moins equivalent a un loyer");
		}

		EPeriodicite periodicite = contrat.getPeriodicite();
		LocalDate dateInitiale = contrat.getDateMiseEnplace();
		int moisParPeriode = getMoisFromPeriodicite(periodicite);

		int nombreEcheances = contrat.getDurée() / moisParPeriode;
		Double montant= contrat.getMontant();
		Double reste = 0d;

		for (int i = 0; i < nombreEcheances; i++) {
			Loyer loyer =  new Loyer();
			if(contrat.isPeriodique()){
				loyer = createLoyer(i + 1, montant);
				reste = contrat.getMontantPercue()-montant*i;
				if(reste>=montant || reste==0){
					loyer.setEtatLoyer(EEtatLoyer.Regler);
				}else {
                    loyer.setEtatLoyer(EEtatLoyer.EnAttente);
                }
			}else {
				loyer = createLoyer(i + 1, montant);
				loyer.setEtatLoyer(EEtatLoyer.Regler);
			}
			//Loyer loyer = createLoyer(i + 1, isPeriodique ? montant / nombreEcheances : montant);
            loyer.setListBiens(contrat.getListBiens());
			loyer.setContrat(contrat);
            loyer.setDateModif(Date.from(contrat.getDateMiseEnplace().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			loyer.setDatePerception(dateInitiale.plusMonths(i * moisParPeriode));
			loyers.add(loyer);
		}

		return loyers;
	}

	private int getMoisFromPeriodicite(EPeriodicite periodicite) {
		return switch (periodicite) {
			case Mensuelle -> 1;
			case Trimestrielle -> 2;
			case Semestrielle -> 3;
			case Annuelle -> 6;
			default -> throw new IllegalArgumentException("Périodicité inconnue : " + periodicite);
		};
	}


	@Override
	public Contrat findByCodeContrat(String code) throws Exception {
		List<Contrat> contrats = contratRepository.findContratByCode(code);
		if (contrats.isEmpty()) {
			throw new Exception("Pas de Contrat correspondant à ce code: " + code);
		}
		return contrats.get(0);
	}

	private Loyer createLoyer(int i, double montantLoyer) {
		Loyer loyer = new Loyer();
		//loyer.setContrat(contrat);
		loyer.setCode(UtilsAPP.genCode("Loyer", 10));
		String libelle = loyer.getCode() + " - " + "Loyer Numere " +i;
		loyer.setDateCreation(new Date());
		loyer.setDescription(libelle);
		loyer.setEtatLoyer(EEtatLoyer.EnAttente);
		loyer.setMontant(montantLoyer);
		return loyer;
	}

	private Contrat toEntity(ContratDTO contratDTO) throws Exception {
		Contrat contrat = new Contrat();
		contrat.setCode(UtilsAPP.genCode("Contrat", 10));
		contrat.setDateCreate(contratDTO.getDateCreate());
		contrat.setListBiens(listBien(contratDTO));
		contrat.setMontantPercue(contratDTO.getMontantPercue());
		contrat.setDateMiseEnplace(contratDTO.getDateMiseEnplace());
		contrat.setDurée(contratDTO.getDurée());
		contrat.setGestionnaire(gestionnaire(contrat.getListBiens()));
		contrat.setListClients(listClient(contratDTO));
		contrat.setLibelle(contratDTO.getLibelle());
		contrat.setPeriodicite(contratDTO.getPeriodicite());
		contrat.setProprietaire(getPersonne(contrat.getListBiens()));
		contrat.setStatut(contratDTO.getStatut());
        contrat.setPeriodique(true);
		contrat.setTva(contratDTO.getTva());
		contrat.setMontant(montantContrat(contrat.getListBiens(), contrat.getTva()));
		return contrat;
	}

	Double montantContrat(Set<Bien> bienSet, Double tva){
		Double montant =0d;
		for(Bien bien : bienSet){
			montant=montant+ bien.getMontantLoyer();
		}
		if(tva !=null){
			Double value = (montant*tva)/100;
			montant=montant+value;
		}
		return montant;
	}

	private Contrat updateEntity(Contrat contrat, ContratDTO contratDTO) throws Exception {
		contrat.setCode(contratDTO.getCode());
		contrat.setDateCreate(contratDTO.getDateCreate());
		contrat.setDateMiseEnplace(contratDTO.getDateMiseEnplace());
		contrat.setDurée(contratDTO.getDurée());
		contrat.setLibelle(contratDTO.getLibelle());
		contrat.setListBiens(listBien(contratDTO));
		contrat.setGestionnaire(gestionnaire(contrat.getListBiens()));
		contrat.setPeriodicite(contratDTO.getPeriodicite());
		contrat.setStatut(contratDTO.getStatut());
		contrat.setMontantPercue(contratDTO.getMontantPercue());
		contrat.setTva(contratDTO.getTva());
		return contrat;
	}

	private ContratDTO toDTO(Contrat contrat) {
		ContratDTO contratDTO = new ContratDTO();
		contratDTO.setCode(contrat.getCode());
		contratDTO.setDateCreate(contrat.getDateCreate());
		contratDTO.setDateMiseEnplace(contrat.getDateMiseEnplace());
		contratDTO.setDurée(contrat.getDurée());
		contratDTO.setLibelle(contrat.getLibelle());
		contratDTO.setMontantPercue(contrat.getMontantPercue());
		contratDTO.setMontant(contrat.getMontant());
        contratDTO.setListClientsDTO(contrat.getListClients().stream()
                .map(Client::getCode)
                .collect(Collectors.toSet()));
        contratDTO.setListBiensDTP(contrat.getListBiens().stream()
                .map(Bien::getCode)
                .collect(Collectors.toSet()));
        contratDTO.setListBiensDTP(contrat.getListBiens().stream()
                .map(Bien::getCode)
                .collect(Collectors.toSet()));
        contratDTO.setProprietaire(contrat.getProprietaire().getCode());
        contratDTO.setGestionnaire(contrat.getGestionnaire().getCode());
        contratDTO.setPeriodicite(contrat.getPeriodicite());
        contratDTO.setTva(contrat.getTva());
        contratDTO.setStatut(contrat.getStatut());
        contratDTO.setPeriodique(true);

		return contratDTO;
	}

	private Set<Bien> listBien(ContratDTO contratDTO) throws Exception {
		Set<String> bienDTOs = contratDTO.getListBiensDTO();
		Set<Bien> biensList = new HashSet<>();
		for (String code : bienDTOs) {
			Optional<Bien> bienList=bienService.findBienByCode(code);
			if(bienList.isEmpty()){
				throw new Exception("Le Bein n existe pas");
			}
			Bien bien = bienList.get();
			iBien.miseLOyer(bien);
			biensList.add(bien);
		}
		return biensList;
	}

	private Set<Client> listClient(ContratDTO contratDTO) throws Exception {
		Set<String> clientDTP = contratDTO.getListClientsDTO();
		Set<Client> clientList = new HashSet<>();
		for (String code : clientDTP) {
			Client client=iClientRepository.findPersonneByCode(code).get(0);
			clientList.add(client);
		}
		return clientList;
	}

	@Override
	public Loyer saveLoyer(LoyerDTO loyerDTO) throws Exception {
		Loyer loyer = toEntityLoyer(loyerDTO);
		return loyerRepository.save(loyer);
	}

	Integer getDuree (EPeriodicite periode){
		Integer duree=0;

		switch (periode){
			case Annuelle :
				duree = 360;
			break;
			case Semestrielle :
				duree = 180;
			break;
			case Trimestrielle :
				duree = 90;
			break;
			case Mensuelle :
				duree=30;
			break;
			default:
				break;
		}
		return duree;
	}

	@Override
	public Loyer updateLoyer(Long id, LoyerDTO loyerDTO) throws Exception {
		Optional<Loyer> optionalLoyer = loyerRepository.findById(id);
		if (!optionalLoyer.isPresent()) {
			throw new Exception("Pas de Loyer correspondant à cet id: " + id);
		}
		Loyer loyer = optionalLoyer.get();
		updateLoyerEntity(loyer, loyerDTO);
		return loyerRepository.saveAndFlush(loyer);
	}

    @Override
    public Loyer UpdateLoyerPay(String code, LoyerDTO loyerDto) throws Exception {
        List<Loyer> opt = loyerRepository.findLoyerByCode(code);
        Optional<List<Loyer>> loyer = Optional.ofNullable(opt);
        if(!loyer.isPresent()){
            throw new Exception("ce loyer est abscent :"+ code);
        }
        System.out.println("updateLoyerPay in process");
        Loyer newLoyer = loyer.get().get(0);
        newLoyer.setEtatLoyer(loyerDto.getEtatLoyer());
        Loyer newer =  updateLoyerEntity(newLoyer, loyerDto);
        return loyerRepository.saveAndFlush(newer);
    }

    @Override
	public Set<LoyerDTO> listLoyer() throws Exception {
		List<Loyer> loyers = loyerRepository.findAll();
		Set<LoyerDTO> loyerDTOs = new HashSet<>();
		for (Loyer loyer : loyers) {
			loyerDTOs.add(toDTOLoyer(loyer));
		}
		return loyerDTOs;
	}

	@Override
	public void deleteLoyer(Loyer loyer) throws Exception {
		loyerRepository.delete(loyer);
	}

	@Override
	public void deleteLoyerByCode(String code) throws Exception {
		List<Loyer> loyer = loyerRepository.findLoyerByCode(code);
		if(loyer==null || loyer.isEmpty()){
			throw new Exception("s");
		}
		loyerRepository.delete(loyer.get(0));

	}

	@Override
	public void deleteLoyerById(Long id) throws Exception {
		loyerRepository.deleteById(id);
	}

	@Override
	public Loyer updateLoyerByCode(String code, LoyerDTO loyerDTO) throws Exception {
		List<Loyer> loyers = loyerRepository.findLoyerByCode(code);
		if (loyers.isEmpty()) {
			throw new Exception("Pas de Loyer correspondant à ce code: " + code);
		}
		Loyer loyer = loyers.get(0);
		updateLoyerEntity(loyer, loyerDTO);
		return loyerRepository.saveAndFlush(loyer);
	}

	@Override
	public List<LoyerDTO> listLoyerByEtat(EEtatLoyer etat) throws Exception {
		List<Loyer> loyers = loyerRepository.findByEtatLoyer(etat);
		List<LoyerDTO> loyerDTOs = new ArrayList<>();
		for (Loyer loyer : loyers) {
			loyerDTOs.add(toDTOLoyer(loyer));
		}
		return loyerDTOs;
	}

	@Override
	public LoyerDTO findLoyerByCode(String code) throws Exception {
		List<Loyer> loyers = loyerRepository.findLoyerByCode(code);
		if (loyers.isEmpty()) {
			return null;
		}
		return toDTOLoyer(loyers.get(0));
	}

	@Override
	public List<LoyerDTO> findLoyerByDatePerception(LocalDate datePerception) throws Exception {
		List<Loyer> loyers = loyerRepository.findByDatePerception(datePerception);
		List<LoyerDTO> loyerDTOs = new ArrayList<>();
		for (Loyer loyer : loyers) {
			loyerDTOs.add(toDTOLoyer(loyer));
		}
		return loyerDTOs;
	}

	@Override
	public List<LoyerDTO> findLoyerByContrat(Contrat contrat) throws Exception {
		List<Loyer> loyers = loyerRepository.findByContrat(contrat);
		List<LoyerDTO> loyerDTOs = new ArrayList<>();
		for (Loyer loyer : loyers) {
			loyerDTOs.add(toDTOLoyer(loyer));
		}
		return loyerDTOs;
	}

	private Loyer toEntityLoyer(LoyerDTO loyerDTO) throws Exception {
		ContratDTO contratDTO = findByCode(loyerDTO.getContrat());
		Contrat contrat = toEntity(contratDTO);
		Loyer loyer = new Loyer();
		loyer.setCode(UtilsAPP.genCode());
		loyer.setContrat(contrat); // À corriger si nécessaire
		loyer.setDateCreation(loyerDTO.getDateCreation());
		loyer.setDescription(loyerDTO.getDescription());
		loyer.setMontant(loyerDTO.getMontant());
		return loyer;
	}

	private Loyer updateLoyerEntity(Loyer loyer, LoyerDTO loyerDTO) {
		loyer.setCode(loyerDTO.getCode());
		loyer.setDateCreation(loyerDTO.getDateCreation());
		loyer.setDescription(loyerDTO.getDescription());
		loyer.setMontant(loyerDTO.getMontant());
		return loyer;
	}

	@SneakyThrows
    private LoyerDTO toDTOLoyer(Loyer loyer) {
		LoyerDTO dto = new LoyerDTO();
		dto.setCode(loyer.getCode());
        dto.setListBiens(loyer.getListBiens().stream()
                .map(Bien::getCode)
                .map( b->{
                        try {
                           return iBien.findByCode(b);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                } )
                .collect(Collectors.toSet()));
        dto.setUtiCreation(loyer.getUtiCreation());
        dto.setDescription(loyer.getDescription());
        dto.setDateModif(loyer.getDateModif());
		dto.setDateCreation(loyer.getDateCreation());
		dto.setDateModif(loyer.getDateModif());
		dto.setDatePerception(loyer.getDatePerception());
		dto.setEtatLoyer(loyer.getEtatLoyer());
		dto.setMontant(loyer.getMontant());
		dto.setIndice(loyer.getIndice());
		dto.setContact(
				loyer.getContrat().getListClients().stream()
						.map(Client::getTelephone1) // transforme chaque client en son contact
						.collect(Collectors.toList()) // collecte les contacts dans une liste
		);

		dto.setContrat(loyer.getContrat().getCode());
		return dto;
	}

    /*void notification(EEtatLoyer etat){
        List<Loyer> loyersList = loyerRepository.findByEtatLoyer(etat.EnAttente);
		LocalDate dateJour = new La
        for(Loyer loyer : loyersList){
            loyer.getDatePerception().compareTo();
            sen
        }
    }*/
}
