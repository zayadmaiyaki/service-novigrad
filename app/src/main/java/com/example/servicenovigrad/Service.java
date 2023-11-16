package com.example.servicenovigrad;

import java.util.ArrayList;
import java.util.List;

public class Service {
    private String id;
    private String name;
    private List<String> formFields;
    private List<String> docsFields;

    public Service() {
        // Default constructor required for calls to DataSnapshot.getValue(Service.class)
    }

    public Service(String id, String name, List<String> formFields, List<String> docsFields) {
        this.id = id;
        this.name = name;
        this.formFields = formFields;
        this.docsFields = docsFields;
    }

    public Service(String id, String name) {
        this.id = id;
        this.name = name;
        this.formFields = new ArrayList<>(); // Create an empty list for formFields
        this.docsFields = new ArrayList<>(); // Create an empty list for docsFields
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<String> formFields) {
        this.formFields = formFields;
    }

    public List<String> getDocsFields() {
        return docsFields;
    }

    public void setDocsFields(List<String> docsFields) {
        this.docsFields = docsFields;
    }
}
