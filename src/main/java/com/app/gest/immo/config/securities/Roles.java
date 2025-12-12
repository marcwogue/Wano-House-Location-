package com.app.gest.immo.config.securities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Roles")
public class Roles {
    public Roles() {
    }

    public Roles(String nom, String description, String fonction) {
        this.nom = nom;
        this.description = description;
        this.fonction = fonction;
    }
    @ManyToMany
    private Set<Groupes> listGroupes = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "NOM", unique = true, updatable = false)
    private String nom;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "FONCTION")
    private String fonction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Set<Groupes> getListGroupes() {
        return listGroupes;
    }

    public void setListGroupes(Set<Groupes> listGroupes) {
        this.listGroupes = listGroupes;
    }

    @Override
    public String toString() {
        return "Roles{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", fonction='" + fonction + '\'' +
                '}';
    }
}