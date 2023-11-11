package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AccueilActivity extends AppCompatActivity {

    private TextView welcomeTextView;
    Button buttonAccueil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        buttonAccueil = findViewById(R.id.buttonAccueil);
        welcomeTextView = findViewById(R.id.welcomeTextView);

        String username = getIntent().getStringExtra("USERNAME");

        if (username != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").whereEqualTo("username", username).limit(1).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (!documents.isEmpty()) {
                        String role = documents.get(0).getString("role");
                        welcomeTextView.setText("Bienvenue! " + username + ". Vous êtes connecté en tant que " + role + ".");
                    } else {
                        welcomeTextView.setText("Bienvenue! " + username);
                    }
                } else {
                    welcomeTextView.setText("Erreur lors de la récupération des données de l'utilisateur.");
                }
            });
        } else {
            welcomeTextView.setText("Bienvenue!");
        }

        buttonAccueil.setOnClickListener(view -> {
            Intent intent = new Intent(AccueilActivity.this, MainPageAdmin.class);
            startActivity(intent);
            finish();
        });
    }
}