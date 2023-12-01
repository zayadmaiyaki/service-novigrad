package com.example.servicenovigrad;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.content.DialogInterface;
import android.text.InputType;
import androidx.appcompat.app.AlertDialog;

public class CreateServiceActivity extends AppCompatActivity {

    private EditText serviceNameEditText;
    private Button addFormFieldButton, addDocumentFieldButton, createServiceButton, deleteServiceButton;
    private ListView formFieldsListView, documentFieldsListView;
    private ArrayAdapter<String> formFieldsAdapter, documentFieldsAdapter;
    private ArrayList<String> formFieldsList = new ArrayList<>();
    private ArrayList<String> documentFieldsList = new ArrayList<>();
    private DatabaseReference servicesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        serviceNameEditText = findViewById(R.id.createServiceNameEditText);
        addFormFieldButton = findViewById(R.id.addFormFieldButtonCreatePage);
        addDocumentFieldButton = findViewById(R.id.addDocumentFieldButtonCreatePage);
        createServiceButton = findViewById(R.id.createServiceButtonCreatePage);
        deleteServiceButton = findViewById(R.id.DeleteServiceButtonCreatePage);
        formFieldsListView = findViewById(R.id.formulaireFieldsListCreate);
        documentFieldsListView = findViewById(R.id.documentsFieldsListCreate);


        servicesRef = FirebaseDatabase.getInstance().getReference("services");


        formFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formFieldsList);
        documentFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, documentFieldsList);
        formFieldsListView.setAdapter(formFieldsAdapter);
        documentFieldsListView.setAdapter(documentFieldsAdapter);

        addFormFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFieldDialog(formFieldsList, formFieldsAdapter);
            }
        });

        addDocumentFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFieldDialog(documentFieldsList, documentFieldsAdapter);
            }
        });
        formFieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(parent.getItemAtPosition(position).toString(), position,"form");
            }
        });

        documentFieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(parent.getItemAtPosition(position).toString(), position,"docs");
            }
        });

        createServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createService();
            }
        });

        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void createService() {
        String serviceName = serviceNameEditText.getText().toString().trim();

        // Validation simple
        if (serviceName.isEmpty()) {
            serviceNameEditText.setError("Service name required");
            serviceNameEditText.requestFocus();
            return;
        }

        String id = servicesRef.push().getKey();

        Service newService = new Service(id, serviceName, formFieldsList, documentFieldsList);

        servicesRef.child(id).setValue(newService).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateServiceActivity.this, "Service created", Toast.LENGTH_LONG).show();
                    // Redirection vers MainPageAdmin
                    finish();
                } else { // Pour retourner à l'activité précédente
                    Toast.makeText(CreateServiceActivity.this, "Failed to create service", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void addField(ArrayList<String> fieldsList, ArrayAdapter<String> adapter) {

        String fieldName = "New Field";

        if (!fieldName.trim().isEmpty() && !fieldsList.contains(fieldName.trim())) {
            fieldsList.add(fieldName.trim());
            adapter.notifyDataSetChanged();
        }
    }
    private void showAddFieldDialog(final ArrayList<String> fieldsList, final ArrayAdapter<String> adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Field");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String fieldName = input.getText().toString().trim();
                if (!fieldName.isEmpty() && !fieldsList.contains(fieldName)) {
                    fieldsList.add(fieldName);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void showDialog(String item, int position,String formType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update or Delete");

        final EditText input = new EditText(this);
        input.setText(item);
        builder.setView(input);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String updatedItem = input.getText().toString();
                updateItemInFirebase(updatedItem, position,formType);
            }
        });

        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItemFromFirebase(position,formType);
            }
        });

        builder.show();
    }
    private void updateItemInFirebase(String item, int position, String fieldType) {

        DatabaseReference fieldsRef;
        if ("form".equals(fieldType)) {
            fieldsRef = servicesRef.child("formFields");
        } else {
            fieldsRef = servicesRef.child("docsFields");
        }

        if ("form".equals(fieldType)) {
            formFieldsList.set(position, item);
            formFieldsAdapter.notifyDataSetChanged();
        } else {
            documentFieldsList.set(position, item);
            documentFieldsAdapter.notifyDataSetChanged();
        }
    }

    private void deleteItemFromFirebase(int position, String fieldType) {
        DatabaseReference fieldsRef;
        if ("form".equals(fieldType)) {
            fieldsRef = servicesRef.child("formFields");
        } else {
            fieldsRef = servicesRef.child("docsFields");
        }

        if ("form".equals(fieldType)) {
            formFieldsList.remove(position);
            formFieldsAdapter.notifyDataSetChanged();
        } else {
            documentFieldsAdapter.remove(String.valueOf(position));
            documentFieldsAdapter.notifyDataSetChanged();
        }
    }


}