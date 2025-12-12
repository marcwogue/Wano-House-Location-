package com.app.gest.immo.implementation;

import com.app.gest.immo.config.securities.Status;
import com.app.gest.immo.config.securities.Utilisateur;
import com.app.gest.immo.controller.UserDetailsImpl;
import com.app.gest.immo.repository.IUtilisateurRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUtilisateurRepository repository;

    public UserDetailsServiceImpl(IUtilisateurRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        username = username.trim();
        System.out.println("Recherche de l'utilisateur: " + username);

        Optional<Utilisateur> user = repository.findUtilisateurByEmail(username);
        if (user.isEmpty()) {
            user = repository.findUtilisateurByNom(username);
            if (user.isEmpty()) {
                System.out.println("Utilisateur introuvable: " + username);
                throw new UsernameNotFoundException("Utilisateur introuvable");
            }
        }

        Utilisateur utilisateur = user.get();

        System.out.println("Utilisateur trouvé: " + utilisateur.getEmail());

        if (utilisateur.getStatus().equals(Status.INNATIF)) {
            System.out.println("Utilisateur inactif: " + username);
            throw new UsernameNotFoundException("Utilisateur inactif");
        }

        if (utilisateur.getGroupes() == null || utilisateur.getGroupes().getRoles() == null) {
            System.out.println("Pas de rôles pour l'utilisateur: " + username);
            throw new UsernameNotFoundException("Pas de rôles définis");
        }

        return UserDetailsImpl.build(utilisateur, utilisateur.getGroupes().getRoles());
    }
}
