package com.app.gest.immo.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.app.gest.immo.enumeration.EEtatBien;

public class BienDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String code;
	private String nom;
	private String location;
	private String longitude;
	private String latittude;
	private int nombrePieces;
	private Double montantLoyer;
	private int superficie;
	private String region;
	private String ville;
	private String quartier;
	private String proprietaire;
	private String gestionnaire;
	private EEtatBien etatBien;
	private String categorie;

	private List<String> listImages;


	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatittude() {
		return latittude;
	}
	public void setLatittude(String latittude) {
		this.latittude = latittude;
	}
	public int getNombrePieces() {
		return nombrePieces;
	}
	public void setNombrePieces(int nombrePieces) {
		this.nombrePieces = nombrePieces;
	}
	public Double getMontantLoyer() {
		return montantLoyer;
	}
	public void setMontantLoyer(Double montantLoyer) {
		this.montantLoyer = montantLoyer;
	}
	public int getSuperficie() {
		return superficie;
	}
	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}
	
	public EEtatBien getEtatBien() {
		return etatBien;
	}
	public void setEtatBien(EEtatBien etatBien) {
		this.etatBien = etatBien;
	}
	

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getQuartier() {
		return quartier;
	}
	public void setQuartier(String quartier) {
		this.quartier = quartier;
	}
	public String getProprietaire() {
		return proprietaire;
	}
	public void setProprietaire(String proprietaire) {
		this.proprietaire = proprietaire;
	}
	public String getGestionnaire() {
		return gestionnaire;
	}
	public void setGestionnaire(String gestionnaire) {
		this.gestionnaire = gestionnaire;
	}
	public BienDTO() {
		super();
	}
	@Override
	public String toString() {
		return "Bien [code=" + code + ", nom=" + nom + "]";
	}
	public BienDTO(String code, String nom, String location, String ville, String longitude, String latittude, int nombrePieces,
			Double montantLoyer, int superficie, String region, String quartier, String proprietaire,
			String gestionnaire, EEtatBien etatBien) {
		super();
		this.code = code;
		this.nom = nom;
		this.location = location;
		this.ville = ville;
		this.longitude = longitude;
		this.latittude = latittude;
		this.nombrePieces = nombrePieces;
		this.montantLoyer = montantLoyer;
		this.superficie = superficie;
		this.region = region;
		this.quartier = quartier;
		this.proprietaire = proprietaire;
		this.gestionnaire = gestionnaire;
		this.etatBien = etatBien;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}

	public List<String> getListImages() {
		return listImages;
	}

	public void setListImages(List<String> listImages) {
		this.listImages = listImages;
	}
}
