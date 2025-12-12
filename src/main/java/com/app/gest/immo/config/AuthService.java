package com.app.gest.immo.config;

import com.app.gest.immo.config.securities.Utilisateur;
import com.app.gest.immo.dto.LoginRequest;
import com.app.gest.immo.dto.LoginResponse;
import com.app.gest.immo.implementation.UserDetailsServiceImpl;
import com.app.gest.immo.service.IUtilisateur;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final IUtilisateur repository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl service;

    public AuthService(IUtilisateur repository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       UserDetailsServiceImpl service) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.service = service;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        try {
            String username = loginRequest.getLogin().trim();
            System.out.println("Tentative d'authentification pour : " + username);

            // 1. Vérifier d'abord si l'utilisateur existe
            Utilisateur utilisateur = repository.login(username);
            if (utilisateur == null) {
                throw new RuntimeException("Utilisateur non trouvé");
            }

            // 2. Authentifier avec Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, loginRequest.getPassword())
            );

            // 3. Générer le token
            UserDetails userDetails = service.loadUserByUsername(username);
            String token = jwtService.generateToken(userDetails);

            return new LoginResponse(token, utilisateur);

        } catch (AuthenticationException ex) {
            throw new RuntimeException("Mot de passe incorrect");
        } catch (Exception ex) {
            throw new RuntimeException("Erreur d'authentification: " + ex.getMessage());
        }
    }


    public String refreshToken(UserDetails  user) {
        return jwtService.generateRefreshToken(user);
    }

    public Utilisateur getUtilisateurByLogin(String login) throws Exception {
        System.err.println("655555555555555555555555555555 " +login);

        return repository.login(login);
    }
}
