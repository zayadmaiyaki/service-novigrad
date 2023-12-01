package com.example.servicenovigrad;

import java.util.List;

public class Client extends Utilisateurs {

    private String id;
    private List<Service> requiredServices;

    public Client (String username, String password){
        super (username, password, "client");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Service> getRequiredServices() {
        return requiredServices;
    }

    public void setRequiredServices(List<Service> requiredServices) {
        this.requiredServices = requiredServices;
    }
}
