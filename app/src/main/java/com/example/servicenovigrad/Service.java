package com.example.servicenovigrad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Service<T> {
    String serviceName;
    Map<String,String> formulaire ; 
    Map<String,T>documents;
    

     // Le hashmap sera construit en utilisant les donnees de l'utilisateur demandées dans android studio
     // To create a new service we just use new services(parameters)
    public createService(String serviceName , Map <String,String>formulaire,Map<String,T>documents) {
        this.serviceName=serviceName;
        this.formulaire = formulaire;
        this.documents = documents;

    }

    public void editService() {
    

        public void editServiceName(String serviceName) {
            this.serviceName = serviceName;
        }


        public void addFieldNameFormulaire(String fieldName) {
            if (!formulaire.containsKey(fieldName)) {
                formulaire.put(fieldName, "");
            }     
        }

        public void editFieldNameFormulaire(String oldFieldName,String newFieldName) {

            if (formulaire.containsKey(oldFieldName)) {
                // Check if the new field name already exists
                if (!formulaire.containsKey(newFieldName)) {
                    String value = formulaire.remove(oldFieldName);
                    formulaire.put(newFieldName,);
                
                } 
                else {
                //System.out.println("The new field name already exists. Choose a different name.");
                }
            } 
            else {
                //System.out.println("Field name not Found ")
            }
    

        }
        private static void deleteFieldFormulaire(String fieldName) {
        //System.out.print("Enter the field name you want to delete: ");
        //String fieldName = scanner.nextLine();
        if (formulaire.containsKey(fieldName)) {
            formulaire.remove(fieldName);
            //System.out.println("Field name deleted successfully.");
        } else {
            //System.out.println("Field name not found.");
        }

        public void addFieldNameDocuments(String fieldName) {
            if (!documents.containsKey(fieldName)) {
                documents.put(fieldName, "");
            }     
        }

        public void editFieldNameDocuments(String oldFieldName,String newFieldName) {

            if (documents.containsKey(oldFieldName)) {
                // Check if the new field name already exists
                if (!documents.containsKey(newFieldName)) {
                    String value = documents.remove(oldFieldName);
                    documents.put(newFieldName,);
                
                } 
                else {
                //System.out.println("The new field name already exists. Choose a different name.");
                }
            } 
            else {
                //System.out.println("Field name not Found ")
            }
    

        }

        private static void deleteFieldDocuments(String fieldName) {
        //System.out.print("Enter the field name you want to delete: ");
        //String fieldName = scanner.nextLine();
            if (documents.containsKey(fieldName)) {
                documents.remove(fieldName);
                //System.out.println("Field name deleted successfully.");
            } 
            else {
            //System.out.println("Field name not found.");
            }
        }

    }
}
