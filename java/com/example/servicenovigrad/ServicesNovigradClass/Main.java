package com.example.servicenovigrad.ServicesNovigradClass;

import java.sql.Time;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("client123", "client_password");
        Employee employee = new Employee("employee123", "employee_password", "Employee");
        Administrateur admin = new Administrateur("admin123", "admin_password");

        Service service = new Service("some_service_type");
        employee.selectService("some_service_type");
        employee.setHeureTravail(Time.valueOf("08:00:00"));

        System.out.println("Client Username: " + client.getUsername());
        System.out.println("Client Role: " + client.getRole());

        System.out.println("Employee Username: " + employee.getUsername());
        System.out.println("Employee Role: " + employee.getRole());

        if (client.seConnecter("client123", "client_password")) {
            System.out.println("Client successfully connected.");
        } else {
            System.out.println("Invalid client credentials.");
        }

        if (employee.seConnecter("employee123", "employee_password")) {
            System.out.println("Employee successfully connected.");
        } else {
            System.out.println("Invalid employee credentials.");
        }

        System.out.println("Admin Username: " + admin.getUsername());

        admin.setUsername("new_admin123");
        admin.setPassword("new_admin_password");

        System.out.println("Updated Admin Username: " + admin.getUsername());
    }
}
