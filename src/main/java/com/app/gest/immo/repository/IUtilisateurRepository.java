package com.app.gest.immo.repository;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.config.securities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    List<Utilisateur> findByGroupes(Groupes groupes);
    Optional<Utilisateur> findUtilisateurByNom (String nom);
    Optional<Utilisateur> findUtilisateurByLogin (String login);
    Optional<Utilisateur> findUtilisateurByEmail(String email);
    Optional<Utilisateur> findByNumero(String numero);

}
