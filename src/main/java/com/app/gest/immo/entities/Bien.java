package com.app.gest.immo.entities;

import com.app.gest.immo.enumeration.EEtatBien;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "BIEN")
public class Bien implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(name = "CODE", unique = true)
	private String code;
	@Column(name = "NOM")
	private String nom;
	private String location;
	private String longitude;
	private String latittude;
	private int nombrePieces;
	private Double montantLoyer;
	private int superficie;
	@ManyToOne(fetch = FetchType.EAGER)
	private Parametre adresse;
	@ManyToOne(fetch = FetchType.EAGER)
	private Parametre region;
	@ManyToOne(fetch = FetchType.EAGER)
	private Parametre quartier;
	@ManyToOne(fetch = FetchType.EAGER)
	private Personne proprietaire;
	@ManyToOne
	private Personne gestionnaire;
	@ManyToOne(fetch = FetchType.EAGER)
	private CategorieBien categorieBien;
	private EEtatBien etatBien;
	@Column(columnDefinition = "TEXT")
	private List<String> listImages;

    public Bien(String code, String nom, String location, String longitude, String latittude, int nombrePieces,
			Double montantLoyer, int superficie, Ville adresse, Region region, Quartier quartier, Personne proprietaire,
			Gestionnaire gestionnaire, EEtatBien etatBien) {
		super();
		this.code = code;
		this.nom = nom;
		this.location = location;
		this.longitude = longitude;
		this.latittude = latittude;
		this.nombrePieces = nombrePieces;
		this.montantLoyer = montantLoyer;
		this.superficie = superficie;
		this.adresse = adresse;
		this.region = region;
		this.quartier = quartier;
		this.proprietaire = proprietaire;
		this.gestionnaire = gestionnaire;
		this.etatBien = etatBien;
	}
	public Bien() {
		super();
		// TODO Auto-generated constructor stub
	}

    @Override
	public String toString() {
		return "Bien [code=" + code + ", nom=" + nom + "]";
	}

}
