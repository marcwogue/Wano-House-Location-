package com.app.gest.immo.dto;

import com.app.gest.immo.config.securities.Utilisateur;

public class LoginResponse {
    private String token;
    private Utilisateur user;

    public LoginResponse(String token, Utilisateur user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Utilisateur getUser() {
        return user;
    }
}
