package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        Spinner role = findViewById(R.id.role_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        role.setAdapter(adapter);
        role.setSelection(-1); // This line ensure role is displayed initially

        EditText usernameField = findViewById(R.id.editUsernameL);
        EditText passwordField = findViewById(R.id.editPasswordL);
        EditText emailField = findViewById(R.id.editEmail);
        Button signUpButton = findViewById(R.id.buttonSignUp);
        TextView alreadyHaveAccountText = findViewById(R.id.textAlreadyHaveAccount);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String enteredUsername = usernameField.getText().toString().trim();
                String enteredPassword = passwordField.getText().toString().trim();
                String enteredEmail = emailField.getText().toString().trim();
                String selectedRole = role.getSelectedItem().toString();
                Administrateur admin= new Administrateur("admin","123admin456","Administrator");



                if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.collection("users").whereEqualTo("username", enteredUsername).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // Username doesn't exist, so you can proceed with registration

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("username", enteredUsername);
                                    data.put("email", enteredEmail); // This is the actual email of the user.
                                    data.put("role", selectedRole);

                                    db.collection("users").document().set(data)
                                            .addOnSuccessListener(aVoid -> {
                                                // Create Firebase Authentication account with the email and password
                                                mAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()) {
                                                                    Toast.makeText(RegisterActivity.this, "Account created.", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle any errors
                                            });
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Username already exists.", Toast.LENGTH_SHORT).show();
                                }
                                if (selectedRole=="Client"){
                                    Client client = new Client(enteredUsername,enteredPassword);
                                }
                                else if (selectedRole=="Employee"){
                                    Employee employee = new Employee(enteredUsername,enteredPassword,selectedRole);
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Error checking username.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        alreadyHaveAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}