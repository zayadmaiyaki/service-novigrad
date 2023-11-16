package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateEditServiceActivity extends AppCompatActivity {

    private  EditText serviceName;
    private ListView formulaireFieldList;
    private Button addFormFieldButton;
    private ArrayAdapter<String> adapterForm;
    private List<String> formFields;
    private DatabaseReference databaseReference;

    private ListView documentsFieldList;
    private Button addDocsFieldButton;
    private ArrayAdapter<String> adapterDocs;
    private List<String> docsFields;

    private Button createUpdateButton;
    private Button deleteServiceButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_service);

        //initialize form content
        formulaireFieldList = findViewById(R.id.formulaireFieldsList);
        addFormFieldButton = findViewById(R.id.addFormFieldButton);
        formFields = new ArrayList<>();
        adapterForm = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formFields);
        formulaireFieldList.setAdapter(adapterForm);
        //initialize docs content
        documentsFieldList = findViewById(R.id.documentsFieldsList);
        addDocsFieldButton = findViewById(R.id.addDocumentFieldButton);
        docsFields = new ArrayList<>();
        adapterDocs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, docsFields);
        documentsFieldList.setAdapter(adapterDocs);

        //initialize create and delete service buttons
        createUpdateButton = findViewById(R.id.createButton);
        deleteServiceButton = findViewById(R.id.DeleteServiceButton);
        //initialize service name
        serviceName = findViewById(R.id.serviceNameEditText);

        // Get the service id from the Intent
        String serviceId = getIntent().getStringExtra("serviceId");

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("services").child(serviceId);

        addFormFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate a unique id for the new field
                String id = databaseReference.push().getKey();

                // Add the new field to the database
                databaseReference.child(id).setValue("New Field");

                // Update the local list and notify the adapter
                formFields.add("New Field");
                adapterForm.notifyDataSetChanged();
            }
        });
        addDocsFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate a unique id for the new field
                String id = databaseReference.push().getKey();

                // Add the new field to the database
                databaseReference.child(id).setValue("New Field");

                // Update the local list and notify the adapter
                docsFields.add("New Field");
                adapterDocs.notifyDataSetChanged();
            }
        });
        formulaireFieldList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked field
                String field = formFields.get(position);

                // Start the update/delete activity
                Intent intent = new Intent(CreateEditServiceActivity.this, UpdateDeleteFieldActivity.class);
                intent.putExtra("field", field);
                startActivity(intent);

                return true;
            }
        });

        documentsFieldList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked field
                String field = docsFields.get(position);

                // Start the update/delete activity
                Intent intent = new Intent(CreateEditServiceActivity.this, UpdateDeleteFieldActivity.class);
                intent.putExtra("field", field);
                startActivity(intent);

                return true;
            }
        });

        createUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the service data in the database
                Service service = new Service(serviceId, serviceName.getText().toString() , formFields, docsFields);
                databaseReference.setValue(service);

                // Redirect back to the MainPageAdmin page
                Intent intent = new Intent(CreateEditServiceActivity.this, MainPageAdmin.class);
                startActivity(intent);
            }
        });

        deleteServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the service from the database
                databaseReference.child(serviceId).removeValue();

                // Redirect back to the MainPageAdmin page
                Intent intent = new Intent(CreateEditServiceActivity.this, MainPageAdmin.class);
                startActivity(intent);
            }
        });

    }
}