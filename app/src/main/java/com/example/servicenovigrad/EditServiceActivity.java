package com.example.servicenovigrad;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class EditServiceActivity extends AppCompatActivity {


    private EditText serviceNameEditText;
    private ListView formFieldsListView, docsFieldsListView;
    private Button updateServiceButton, deleteServiceButton;
    private ArrayAdapter<String> formFieldsAdapter, docsFieldsAdapter;
    private ArrayList<String> formFieldsList = new ArrayList<>();
    private ArrayList<String> docsFieldsList = new ArrayList<>();
    private DatabaseReference serviceRef;
    private String serviceId;
    private Button addFormFieldButton;
    private Button addDocumentFieldButton;

    private final ActivityResultLauncher<Intent> updateDeleteFieldActivityResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            fetchServiceData();
                        }
                    }
            );



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_service); // Use your actual layout file name

        serviceNameEditText = findViewById(R.id.UpdateServiceName); // Use your actual ID
        formFieldsListView = findViewById(R.id.formFieldListUpdate); // Use your actual ID
        docsFieldsListView = findViewById(R.id.docsFieldListUpdate); // Use your actual ID
        updateServiceButton = findViewById(R.id.buttonUpdateService); // Use your actual ID
        deleteServiceButton = findViewById(R.id.buttonDeleteService); // Use your actual ID
        addFormFieldButton = findViewById(R.id.buttonAddFormFieldUpdate);
        addDocumentFieldButton = findViewById(R.id.buttonAddDocsFieldUpdate);

        serviceId = getIntent().getStringExtra("serviceId");
        if(serviceId == null) {
            // Handle the case where serviceId is not passed correctly
            Toast.makeText(this, "Error: Service ID is missing.", Toast.LENGTH_LONG).show();
            finish(); // Close the activity as there's no valid service ID
            return;
        }
        Log.d("EditServiceActivity", "Service ID: " + serviceId);

        serviceRef = FirebaseDatabase.getInstance().getReference("services").child(serviceId);

        formFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formFieldsList);
        docsFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, docsFieldsList);
        formFieldsListView.setAdapter(formFieldsAdapter);
        docsFieldsListView.setAdapter(docsFieldsAdapter);

        fetchServiceData();

        updateServiceButton.setOnClickListener(view -> updateService());
        deleteServiceButton.setOnClickListener(view -> deleteService());

        formFieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(parent.getItemAtPosition(position).toString(), position,"form");
            }
        });

        docsFieldsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDialog(parent.getItemAtPosition(position).toString(), position,"docs");
            }
        });

        addFormFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFieldDialog(formFieldsList, formFieldsAdapter);
            }
        });

        addDocumentFieldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFieldDialog(docsFieldsList, docsFieldsAdapter);
            }
        });
    }

    private void fetchServiceData() {
        // Fetch the service name
        serviceRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String serviceName = dataSnapshot.getValue(String.class);
                serviceNameEditText.setText(serviceName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Fetch form fields
        serviceRef.child("formFields").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                formFieldsList.clear();
                for (DataSnapshot fieldSnapshot : dataSnapshot.getChildren()) {
                    String field = fieldSnapshot.getValue(String.class);
                    formFieldsList.add(field);
                }
                formFieldsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        // Fetch document fields
        serviceRef.child("docsFields").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                docsFieldsList.clear();
                for (DataSnapshot fieldSnapshot : dataSnapshot.getChildren()) {
                    String field = fieldSnapshot.getValue(String.class);
                    docsFieldsList.add(field);
                }
                docsFieldsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void updateService() {
        String serviceName = serviceNameEditText.getText().toString().trim();
        if (serviceName.isEmpty()) {
            serviceNameEditText.setError("Service name is required");
            return;
        }

        // Update service name
        serviceRef.child("name").setValue(serviceName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Optionally, handle updating the lists of fields
            } else {
                // Handle failure
            }
        });
        Intent intent = new Intent(EditServiceActivity.this, MainPageAdmin.class);
        startActivity(intent);

    }

    private void deleteService() {
        serviceRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Service deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Deletion failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static final int UPDATE_DELETE_FIELD_REQUEST = 1; // Define this constant


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_DELETE_FIELD_REQUEST && resultCode == RESULT_OK) {
            // Refresh your fields list here. This might involve fetching data from Firebase again.
            fetchServiceData();
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
        // Decide which field type we are updating, form or document
        DatabaseReference fieldsRef;
        if ("form".equals(fieldType)) {
            fieldsRef = serviceRef.child("formFields");
        } else {
            fieldsRef = serviceRef.child("docsFields");
        }

        // The child key is the String value of the position, which works if you haven't deleted any items.
        // If you have deleted items, this will not work and you need to use the actual keys of the items.
        fieldsRef.child(String.valueOf(position)).setValue(item)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditServiceActivity.this, "Field updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditServiceActivity.this, "Update failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // Update the local list and notify the adapter
        if ("form".equals(fieldType)) {
            formFieldsList.set(position, item);
            formFieldsAdapter.notifyDataSetChanged();
        } else {
            docsFieldsList.set(position, item);
            docsFieldsAdapter.notifyDataSetChanged();
        }
    }

    private void deleteItemFromFirebase(int position, String fieldType) {
        DatabaseReference fieldsRef;
        if ("form".equals(fieldType)) {
            fieldsRef = serviceRef.child("formFields");
        } else {
            fieldsRef = serviceRef.child("docsFields");
        }

        // Remove the value at the specified position
        fieldsRef.child(String.valueOf(position)).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditServiceActivity.this, "Field deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditServiceActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                    }
                });

        // Remove the item from the local list and notify the adapter
        if ("form".equals(fieldType)) {
            formFieldsList.remove(position);
            formFieldsAdapter.notifyDataSetChanged();
        } else {
            docsFieldsList.remove(position);
            docsFieldsAdapter.notifyDataSetChanged();
        }
    }
}