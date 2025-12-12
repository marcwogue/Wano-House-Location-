package com.app.gest.immo.service;

import java.util.List;
import java.util.Map;

import com.app.gest.immo.dto.UtilisateurDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import com.app.gest.immo.config.securities.Utilisateur;

public interface IUtilisateur {

    Utilisateur findById(Long id) throws Exception;

    Utilisateur toEntitieUser(UtilisateurDTO utilisateurDTO) throws Exception;

    Utilisateur save(Utilisateur utilisateur, Long idGroupe) throws Exception;

    Utilisateur saveAdmin(Utilisateur utilisateur, Long idGroupe) throws Exception;

    void disableUtilisateur(Long id) throws Exception;

    Utilisateur findByName(String nom) throws Exception;
    Utilisateur login(String login) throws Exception;

    String update(MultipartFile file, Long id) throws Exception;

    Map<String, Integer> statusListSave() throws Exception;

    Utilisateur saveIt(Utilisateur utilisateur) throws Exception;

    Utilisateur save(Utilisateur utilisateur) throws Exception;

    List<Utilisateur> list();
}