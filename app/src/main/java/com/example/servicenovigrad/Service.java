package com.example.servicenovigrad;

import java.util.HashMap;
import java.util.Map;

public class Service<T> {
    String serviceName;
    Map<String, String> formulaire;
    Map<String, T> documents;

    // Constructor for Service
    public Service(String serviceName, Map<String, String> formulaire, Map<String, T> documents) {
        this.serviceName = serviceName;
        this.formulaire = formulaire;
        this.documents = documents;
    }

    public void editServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void addFieldNameFormulaire(String fieldName) {
        if (!formulaire.containsKey(fieldName)) {
            formulaire.put(fieldName, "");
        }
    }

    public void editFieldNameFormulaire(String oldFieldName, String newFieldName) {
        if (formulaire.containsKey(oldFieldName) && !formulaire.containsKey(newFieldName)) {
            String value = formulaire.remove(oldFieldName);
            formulaire.put(newFieldName, value);
        }
    }

    public void deleteFieldFormulaire(String fieldName) {
        formulaire.remove(fieldName);
    }

    public void addFieldNameDocuments(String fieldName, T document) {
        if (!documents.containsKey(fieldName)) {
            documents.put(fieldName, document);
        }
    }

    public void editFieldNameDocuments(String oldFieldName, String newFieldName) {
        if (documents.containsKey(oldFieldName) && !documents.containsKey(newFieldName)) {
            T value = documents.remove(oldFieldName);
            documents.put(newFieldName, value);
        }
    }

    public void deleteFieldDocuments(String fieldName) {
        documents.remove(fieldName);
    }
}
