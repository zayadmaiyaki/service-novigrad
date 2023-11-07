package com.example.servicenovigrad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Service {
    private static Map<String, String> fieldFeatureMap ;
    // Create a Map that represent the created service with the name
    // and the informations the Clients have to fill
    public Service() {
        this.fieldFeatureMap = new HashMap<>();

    }
    private static Scanner scanner = new Scanner(System.in);




    public static void main(String[] args) {
        //Handle the lines below w the front end
        System.out.println("Are you an 'administrator' or a 'client'? Type 'exit' to stop.");
        String userType = scanner.nextLine().trim().toLowerCase();

        switch (userType) {
            case "administrator":
                createService();
                break;
            case "client":
                //Not necessary for the livrable 2
                //fillFieldFeatureMap();
                break;
            case "exit":
                System.out.println("Exiting program.");
                break;
            default:
                System.out.println("Invalid user type. Exiting program.");
                break;
        }

        scanner.close();

        // Print the hashmap
        System.out.println("Final Field-Feature Map: ");
        fieldFeatureMap.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    private static void createService() {
        // Handle prints and scanners with front end
        System.out.println("Administrator mode: Please enter field names. Type 'done' when finished.");

        while (true) {
            System.out.print("Enter field name: ");
            String field = scanner.nextLine();

            if ("done".equalsIgnoreCase(field)) {
                break;
            }

            //if (!field.isBlank()) {
            //    fieldFeatureMap.put(field, ""); // Add the field with an empty value
            //}
        }
    }

    /**
     * This method is not necessary for the livrable 2
     *
     * private static void fillFieldFeatureMap() {
     System.out.println("Client mode: Please enter field values for the following fields.");

     for (String key : fieldFeatureMap.keySet()) {
     System.out.print("Enter value for '" + key + "': ");
     String fieldValue = scanner.nextLine();
     fieldFeatureMap.put(key, fieldValue);
     }
     }*/
}