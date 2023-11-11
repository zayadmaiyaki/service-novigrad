package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class CreateServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);
    }
    // Coller a partir d'ici

    private EditText serviceNameEditText;
    private EditText addFormFieldEditText;
    private EditText addDocumentFieldEditText;
    private Button addFormFieldButton;
    private Button addDocumentFieldButton;
    private Button createServiceButton;

    // Maps to hold the formulaire and document field names and values
    private Map<String, String> formulaire = new HashMap<>();
    private Map<String, Object> documents = new HashMap<>(); // Replace Object with the actual type for documents


    // Other methods can be implemented as needed, such as methods to remove fields
}