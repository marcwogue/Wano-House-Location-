package com.app.gest.immo.dto;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.config.securities.Status;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class UtilisateurDTO implements Serializable {
    private Long id;
    private String nom;
    private String login;
    private String passWord;
    private String iP;
    private int maxSession;
    private String email;
    private String numero;
    private Status status;
    protected boolean isFirstConnexion;
    private LocalDate dateCreation;
    private LocalDate dateModif;
    private String utiCreation;
    private String groupes;

}
