package com.example.servicenovigrad.ServicesNovigradClass;

import java.sql.Time;

public class Employee extends Utilisateurs {
    Service service;
    public Employee (String username , String password, String role){
        super(username, password,role="Employee");
		this.service = new Service("default_service_type"); // Créer une nouvelle instance de Service
    }
    public void createAccount(String username,String password){
        Employee employee = new Employee(username,password,"Employee");
    }
    public void selectService(String service){
		 if (this.service != null) {
        this.service.serviceType = service ;
    }
	}
    public void setHeureTravail(Time heureDeTravail){
		 if (this.service != null) {
        this.service.heureDeTravail=heureDeTravail;
    }
	}
    public void visualiserDemande(){
    }
    public void approuverDemande(){
    }
    public void rejectDemande(){
    }
}