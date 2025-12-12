package com.app.gest.immo.entities;

import java.io.Serializable;
import java.util.Date;

import com.app.gest.immo.enumeration.ESexe;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PERSONNNE")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class Personne implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private Long id;
	@Column(name = "code", unique = true)
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
	private Date dateCreation= new java.util.Date();
	private Date dateModif;
	private String utiCreation;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


}
