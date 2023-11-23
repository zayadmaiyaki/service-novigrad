package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText usernameField, passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        usernameField = findViewById(R.id.editUsernameL);
        passwordField = findViewById(R.id.editPasswordL);

        Button signInButton = findViewById(R.id.buttonSignIn);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();
                if (username.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your username.", Toast.LENGTH_SHORT).show();
                    return; // Stop the function from proceeding further
                }

                // Check if the password field is empty
                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    return; // Stop the function from proceeding further
                }
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("users").whereEqualTo("username", username).limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            String emailForAuth = documents.get(0).getString("email");

                            mAuth.signInWithEmailAndPassword(emailForAuth, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Fetch the role from the document
                                        String userRole = documents.get(0).getString("role");

                                        Toast.makeText(LoginActivity.this, "Login successful.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(LoginActivity.this, AccueilActivity.class);
                                        intent.putExtra("USERNAME", username);
                                        intent.putExtra("ROLE", userRole); // Passing the role to AccueilActivity
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        TextView signUpTextView = findViewById(R.id.textViewSignUp);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}