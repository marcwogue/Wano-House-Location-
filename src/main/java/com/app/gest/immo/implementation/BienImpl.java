package com.app.gest.immo.implementation;

import java.util.*;

import org.springframework.stereotype.Service;

import com.app.gest.immo.dto.BienDTO;
import com.app.gest.immo.entities.Bien;
import com.app.gest.immo.entities.CategorieBien;
import com.app.gest.immo.entities.Gestionnaire;
import com.app.gest.immo.entities.Parametre;
import com.app.gest.immo.entities.Personne;
import com.app.gest.immo.entities.UtilsAPP;
import com.app.gest.immo.enumeration.EEtatBien;
import com.app.gest.immo.repository.IBienRepository;
import com.app.gest.immo.repository.ICategorieRepository;
import com.app.gest.immo.repository.IParametreRepository;
import com.app.gest.immo.repository.IPersonneRepository;
import com.app.gest.immo.service.IBien;

@Service
public class BienImpl implements IBien {

	private final IBienRepository iBien;
	private final IParametreRepository iParam;
	private final IPersonneRepository personneRepos;
	private final ICategorieRepository iCategorieRepository;

	public BienImpl(IBienRepository iBien, IParametreRepository iParam,
			IPersonneRepository personneRepos, ICategorieRepository iCategorieRepository) {
		this.iBien = iBien;
		this.iParam = iParam;
		this.personneRepos = personneRepos;
		this.iCategorieRepository = iCategorieRepository;
	}

	@Override
	public Bien save(BienDTO bienDTO) throws Exception {
		Bien bien = setBien(bienDTO);
		if(!bien.getEtatBien().equals(EEtatBien.Libre)){
			bien.setEtatBien(EEtatBien.Libre);
		}
		return iBien.save(bien);
	}

	@Override
	public Bien update(Long id, BienDTO bienDTO) throws Exception {
		Optional<Bien> bien = iBien.findById(id);
		if (bien.get() == null) {
			throw new Exception("Pas de  Bien correspondant à cet id" + " -" + id);
		}
		Bien bie = bien.get();
		bie = setBien(bienDTO);
		return iBien.saveAndFlush(bie);
	}

	@Override
	public Set<BienDTO> list() throws Exception {
		List<Bien> list = iBien.findAll();
		Set<BienDTO> set = new HashSet<BienDTO>();
		if (list.isEmpty()) {
			return null;
		}
		for (Bien bien : list) {
			set.add(setBienDTO(bien));
		}
		return set;
	}

	@Override
	public void delete(Bien Bien) throws Exception {
		iBien.delete(Bien);

	}

	@Override
	public void deleteById(Long id) throws Exception {
		Optional<Bien> Bien = iBien.findById(id);
		if (Bien.get() == null) {
			throw new Exception("Pas de  Bien correspondant à cet id" + " -" + id);
		}
		Bien bien = Bien.get();
		iBien.delete(bien);

	}

	@Override
	public void deleteByCode(String code) throws Exception {
		Optional<Bien> Bien = iBien.findBienByCode(code);
		if (Bien.isEmpty()) {
			throw new Exception("Pas de  Bien correspondant à cet id" + " -" + code);
		}
		Bien bien = Bien.get();
		iBien.delete(bien);
	}

	@Override
	public BienDTO findByCode(String code) throws Exception {
		Bien bien = iBien.findBienByCode(code).get();
        return setBienDTO(bien);
	}

	@Override
	public Bien updateByCode(String code, BienDTO bienDTO) throws Exception {
		Optional<Bien> listBiens = iBien.findBienByCode(code);
		if (listBiens == null || listBiens.isEmpty()) {
			throw new Exception("");
		}
		Bien bien = listBiens.get();
		bien = toEntity(bienDTO);
		return iBien.saveAndFlush(bien);
	}

	@Override
	public List<BienDTO> listBienByEtat(EEtatBien etat) throws Exception {

		List<Bien> list = iBien.findByEtatBien(etat);
		List<BienDTO> lsitDTO = new ArrayList<BienDTO>();
		if (list == null || list.isEmpty()) {
			throw new Exception("Aucun Element Trouve");
		}
		for (Bien bien : list) {
			lsitDTO.add(setBienDTO(bien));
		}
		return lsitDTO;
	}

	@Override
	public List<BienDTO> listBienByCategorie(CategorieBien categorie) throws Exception {
		List<Bien> list = iBien.findByCategorieBien(categorie);
		List<BienDTO> lsitDTO = new ArrayList<BienDTO>();
		if (list == null || list.isEmpty()) {
			throw new Exception("Aucun Element Trouve");
		}
		for (Bien bien : list) {
			lsitDTO.add(setBienDTO(bien));
		}
		return lsitDTO;
	}

	private Bien setBien(BienDTO BienDTO) throws Exception {
		Bien Bien = toEntity(BienDTO);
		return Bien;
	}

	private BienDTO setBienDTO(Bien Bien) {
        return toDTO(Bien);
	}

	private BienDTO toDTO(Bien bien) {
		BienDTO bienDTO = new BienDTO();
		bienDTO.setVille(bien.getAdresse()==null ? null  : bien.getAdresse().getCode());
		bienDTO.setCode(bien.getCode());
		bienDTO.setCategorie(bien.getCategorieBien().getCode());
		bienDTO.setEtatBien(bien.getEtatBien());
		bienDTO.setRegion(bien.getRegion()==null ?null : bien.getRegion().getCode());
		bienDTO.setLongitude(bien.getLongitude());
		bienDTO.setListImages(bien.getListImages());
        bienDTO.setProprietaire(bien.getProprietaire().getCode());
		bienDTO.setLatittude(bien.getLatittude());
        bienDTO.setLocation(bien.getLocation());
        bienDTO.setLatittude(bien.getLatittude());
		bienDTO.setSuperficie(bien.getSuperficie());
		bienDTO.setQuartier(bien.getQuartier()==null ? null : bien.getQuartier().getCode());
		bienDTO.setGestionnaire(bien.getGestionnaire()==null ? null : bien.getGestionnaire().getCode());
		bienDTO.setMontantLoyer(bien.getMontantLoyer());
		bienDTO.setNom(bien.getNom());
		bienDTO.setNombrePieces(bien.getNombrePieces());
		return bienDTO;
	}

	public Map<String, Integer> nombre(String texte){
		String [] listeMot = texte.split(" ");
		Map<String, Integer> listMap = new HashMap<>();
		int conteur=0;int i=0;
		for(i=0; i<listeMot.length; i++){
			if(listMap.get(listeMot[i])==null){
				conteur = conteur+1;
				listMap.put(listeMot[i], conteur);
			}else {
				conteur= listMap.get(listeMot[i])+1;
				listMap.put(listeMot[i], conteur);
				List<Integer> list =null;
				list.add(listMap.get(listeMot[i]));
			}
		}
		return null;
	}

	@Override
	public Bien toEntity(BienDTO bienDTO) throws Exception {
		Bien bien = new Bien();
        Parametre region = iParam.findParametreByCode(bienDTO.getRegion())
                .orElseThrow(()->new Exception("region non trouvée :" + bienDTO.getRegion() ));
		Parametre quartier = iParam.findParametreByCode(bienDTO.getQuartier())
                .orElseThrow(()->new Exception("aucun quartier trouvé : "+bienDTO.getQuartier()));
        Parametre ville = iParam.findParametreByCode(bienDTO.getVille())
                .orElseThrow(() -> new Exception("Ville introuvable : " + bienDTO.getVille()));

        Personne proprietaire = personneRepos.findPersonneByCode(bienDTO.getProprietaire()).get(0);
		Personne gestionnaire = personneRepos.findPersonneByCode(bienDTO.getGestionnaire()).get(0);
        Optional<CategorieBien> listCategorie = iCategorieRepository.findCategorieBienByCode(bienDTO.getCategorie());


		if (listCategorie == null || listCategorie.isEmpty()) {
			throw new Exception();
		}
		CategorieBien categorie = listCategorie.get();

        bien.setAdresse(ville);

        if (gestionnaire != null) {
			bien.setGestionnaire(gestionnaire);
		}

        bien.setRegion(region);
        bien.setQuartier(quartier);
        bien.setCode(UtilsAPP.genCodeNum());

        bien.setProprietaire(proprietaire);
        bien.setEtatBien(bienDTO.getEtatBien());
		bien.setLongitude(bienDTO.getLongitude());
        bien.setLocation(bienDTO.getLocation());
		bien.setLatittude(bienDTO.getLatittude());
		bien.setSuperficie(bienDTO.getSuperficie());
		bien.setMontantLoyer(bienDTO.getMontantLoyer());
		bien.setNom(bienDTO.getNom());
		bien.setListImages(bienDTO.getListImages());
		bien.setNombrePieces(bienDTO.getNombrePieces());
		bien.setCategorieBien(categorie);
        bien.setLocation(bienDTO.getLocation());
        bien.setLatittude(bienDTO.getLatittude());
		return bien;
	}

	@Override
	public Bien miseLOyer( Bien bien) throws Exception {
		if (bien.getEtatBien().equals(EEtatBien.Occupe) || bien.getEtatBien().equals(EEtatBien.EnReparation)) {
			throw new Exception("impossible de mettre le bien en Location");
		}
		bien.setEtatBien(EEtatBien.Occupe);
		return iBien.saveAndFlush(bien);
	}

}
