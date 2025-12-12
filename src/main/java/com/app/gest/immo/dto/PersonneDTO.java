package com.app.gest.immo.dto;

import java.io.Serializable;
import java.util.Date;

import com.app.gest.immo.enumeration.ESexe;
import com.app.gest.immo.enumeration.ETypeClient;

public class PersonneDTO implements Serializable {
    
	private long id;
    private String code;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String lieuNaissance;
    private String telephone1;
    private String telephone2;
    private String profession;
	private String email;
    private ESexe sexe;
    private Date dateCreation;
	private Date dateModif;
	private String utiCreation;

	private Date dateNomination;

	private String piecesRecto;

	private String pieceVerso;

	private String refFiscale;

	private ETypeClient typeClient;

	private String typePersonne;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public Date getDateNaissance() {
		return dateNaissance;
	}
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	public String getLieuNaissance() {
		return lieuNaissance;
	}
	public void setLieuNaissance(String lieuNaissance) {
		this.lieuNaissance = lieuNaissance;
	}
	public String getTelephone1() {
		return telephone1;
	}
	public void setTelephone1(String telephone1) {
		this.telephone1 = telephone1;
	}
	public String getTelephone2() {
		return telephone2;
	}
	public void setTelephone2(String telephone2) {
		this.telephone2 = telephone2;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public ESexe getSexe() {
		return sexe;
	}
	public void setSexe(ESexe sexe) {
		this.sexe = sexe;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	public Date getDateModif() {
		return dateModif;
	}
	public void setDateModif(Date dateModif) {
		this.dateModif = dateModif;
	}
	public String getUtiCreation() {
		return utiCreation;
	}
	public void setUtiCreation(String utiCreation) {
		this.utiCreation = utiCreation;

	}

	public Date getDateNomination() {
		return dateNomination;
	}

	public void setDateNomination(Date dateNomination) {
		this.dateNomination = dateNomination;
	}

	public String getPiecesRecto() {
		return piecesRecto;
	}

	public void setPiecesRecto(String piecesRecto) {
		this.piecesRecto = piecesRecto;
	}

	public String getPieceVerso() {
		return pieceVerso;
	}

	public void setPieceVerso(String pieceVerso) {
		this.pieceVerso = pieceVerso;
	}

	public String getRefFiscale() {
		return refFiscale;
	}

	public void setRefFiscale(String refFiscale) {
		this.refFiscale = refFiscale;
	}

	public ETypeClient getTypeClient() {
		return typeClient;
	}

	public void setTypeClient(ETypeClient typeClient) {
		this.typeClient = typeClient;
	}

	public String getTypePersonne() {
		return typePersonne;
	}

	public void setTypePersonne(String typePersonne) {
		this.typePersonne = typePersonne;
	}
}
