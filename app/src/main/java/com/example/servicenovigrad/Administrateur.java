package com.example.servicenovigrad;

public class Administrateur extends Utilisateurs {
    private String username;
    private String password;

    public Administrateur(String username, String password,String role) {
        super(username, password,role="Administrator");
        this.username = "admin";
        this.password = "123admin456";
    }

}
