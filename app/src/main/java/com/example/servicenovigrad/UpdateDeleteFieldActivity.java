package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.content.Intent;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDeleteFieldActivity extends AppCompatActivity {

    private EditText editText;
    private Button updateButton;
    private Button deleteButton;
    private String field;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_field);

        editText = findViewById(R.id.editFieldName);
        updateButton = findViewById(R.id.buttonUpdateField);
        deleteButton = findViewById(R.id.buttonDeleteField);

        // Get the field from the Intent
        field = getIntent().getStringExtra("field");

        // Set the field to the EditText
        editText.setText(field);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("fields");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the field in the database
                databaseReference.child(field).setValue(editText.getText().toString());

                // Display a success toast message
                Toast.makeText(UpdateDeleteFieldActivity.this, "Field Updated", Toast.LENGTH_SHORT).show();

                // Navigate back to the CreateService activity
                Intent intent = new Intent(UpdateDeleteFieldActivity.this, CreateEditServiceActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete the field from the database
                databaseReference.child(field).removeValue();

                // Display a success toast message
                Toast.makeText(UpdateDeleteFieldActivity.this, "Field Deleted", Toast.LENGTH_SHORT).show();

                // Navigate back to the CreateService activity
                Intent intent = new Intent(UpdateDeleteFieldActivity.this, CreateEditServiceActivity.class);
                startActivity(intent);
            }
        });
    }

}