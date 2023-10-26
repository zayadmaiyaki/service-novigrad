package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.servicenovigrad.ServicesNovigradClass.Client;

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
                EditText username = (EditText) findViewById(R.id.editUsername);
                EditText password = (EditText) findViewById(R.id.editPassword);
                Client client = new Client(username.getText().toString(),password.getText().toString());
                openactivity_login();
            }
        });
    }

    public void openactivity_accueil() {
        Intent intent = new Intent(this, AccueilActivity.class);
        startActivity(intent);
    }
    public void openactivity_login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}