package com.example.servicenovigrad;

import java.sql.Time;

public class Employee extends Utilisateurs {
    public Employee (String username , String password, String role){
        super(username, password,role="Employee");
    }
    public void createAccount(String username,String password){
        Employee employee = new Employee(username,password,"Employee");
    }
    public void selectService(String service){
    }

    public void setHeureTravail(Time heureDeTravail){
    }

    public void visualiserDemande(){
    }
    public void approuverDemande(){
    }
    public void rejectDemande(){
    }
}
