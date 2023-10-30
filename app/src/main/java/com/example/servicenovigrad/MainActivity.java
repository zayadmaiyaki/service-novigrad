/**package com.example.servicenovigrad;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private Button buttonSignUp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_accueil();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openactivity_accueil();
            }
        });
    }

    public void openactivity_accueil() {
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }
}*/