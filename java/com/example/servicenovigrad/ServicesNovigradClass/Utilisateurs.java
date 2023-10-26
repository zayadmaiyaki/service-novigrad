package com.example.servicenovigrad.ServicesNovigradClass;

// Cette classe nous permet de créer des comptes d'utilisateurs et de leurs affecter des rôles (employer ou client)
public class Utilisateurs { 
    private String username;
    private String password;
    private String role;

    public Utilisateurs(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public boolean seConnecter(String username, String password) {
        return this.password.equals(password) && this.username.equals(username);
    }
}
