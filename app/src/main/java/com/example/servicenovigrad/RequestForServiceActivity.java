package com.example.servicenovigrad;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestForServiceActivity extends AppCompatActivity {

    private TextView serviceNameEditText;
    private ListView formFieldsListView, docsFieldsListView;
    private Button submitRequest;
    private ArrayAdapter<String> formFieldsAdapter, docsFieldsAdapter;
    private ArrayList<String> formFieldsList = new ArrayList<>();
    private ArrayList<String> docsFieldsList = new ArrayList<>();
    private DatabaseReference serviceRef;
    private DatabaseReference requestsRef;
    private String serviceId;
    private ActivityResultLauncher<Intent> filePickerLauncher;
    private String selectedDocField;
    private Map<String,String>filledDocs = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_service);

        serviceNameEditText = findViewById(R.id.newRequestText);
        formFieldsListView = findViewById(R.id.formFieldListViewRequest);
        docsFieldsListView = findViewById(R.id.docsFieldListViewRequest);
        submitRequest = findViewById(R.id.buttonSubmitRequest);
        Map<String,String>filledForm = new HashMap<>();

        String username=getIntent().getStringExtra("username");

        serviceId = getIntent().getStringExtra("SERVICE_ID");
        if(serviceId == null) {
            // Gérer l'erreur
            Toast.makeText(this, "Error: Service ID is missing.", Toast.LENGTH_LONG).show();
            finish(); // Close the activity as there's no valid service ID
            return;
        }

        serviceRef = FirebaseDatabase.getInstance().getReference("services").child(serviceId);
        requestsRef= FirebaseDatabase.getInstance().getReference("requests");

        formFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, formFieldsList);
        docsFieldsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, docsFieldsList);
        formFieldsListView.setAdapter(formFieldsAdapter);
        docsFieldsListView.setAdapter(docsFieldsAdapter);

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri fileUri = result.getData().getData();
                        filledDocs.put(selectedDocField, fileUri.toString());
                        updateDocsStatus();
                    }
                }
        );

        fetchServiceData();
        formFieldsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked item
                String item = (String) parent.getItemAtPosition(position);



                // Create an AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(RequestForServiceActivity.this);
                builder.setTitle("Enter a value");

                // Set up the input
                final EditText input = new EditText(RequestForServiceActivity.this);
                // Check if the HashMap already contains a value for the item
                if (filledForm.containsKey(item)) {
                    // If it does, pre-fill the EditText with the value
                    input.setText(filledForm.get(item));
                }
                builder.setView(input);


                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = input.getText().toString();
                        if (value.isEmpty()) {
                            // Notify that the input is empty
                            Toast.makeText(getApplicationContext(), "Value cannot be empty", Toast.LENGTH_SHORT).show();
                            TextView statusText = findViewById(R.id.formStatus);  // Replace with the actual ID of your TextView
                            statusText.setText("Incomplete");
                            statusText.setTextColor(Color.RED);
                        }
                        else {
                            filledForm.put(item, value);  // Add the item and value to the HashMap

                            // Get the current adapter of docsFieldsListView
                            ListAdapter adapter = formFieldsListView.getAdapter();

                            // Check if the adapter is an instance of CustomAdapterDocs
                            if (adapter instanceof CustomAdapterDocs) {
                                // Cast the adapter to CustomAdapterDocs and notify that the data has changed
                                ((CustomAdapterDocs) adapter).notifyDataSetChanged();
                            } else {

                            }

                            // Check if all items have a value
                            boolean allFilled = true;
                            for (int i = 0; i < formFieldsAdapter.getCount(); i++) {
                                String listItem = (String) formFieldsListView.getItemAtPosition(i);
                                if (!filledForm.containsKey(listItem)) {
                                    allFilled = false;
                                    break;
                                }
                            }

                            // If all items have a value, change the text and color
                            if (allFilled) {
                                TextView statusText = findViewById(R.id.formStatus);  // Replace with the actual ID of your TextView
                                statusText.setText("Complete");
                                statusText.setTextColor(Color.GREEN);
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Show the dialog
                builder.show();

                return true;  // Return true to indicate that the long click was consumed
            }
        });

        docsFieldsListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            selectedDocField = item;

            // Open file picker
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // Set appropriate file type if needed
            filePickerLauncher.launch(intent);

            return true;
        });

        submitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView docsStatus = findViewById(R.id.docsStatus);
                TextView formStatus = findViewById(R.id.formStatus);
                if(docsStatus.getText().toString()=="Complete"&&formStatus.getText().toString()=="Complete") {
                    serviceRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String serviceName = dataSnapshot.getValue(String.class);
                            String requestId = serviceName + " request " + System.currentTimeMillis();  // Unique ID for the request

                            requestsRef.child(requestId).child("serviceRequested").setValue(serviceName);
                            requestsRef.child(requestId).child("form").setValue(filledForm);
                            requestsRef.child(requestId).child("username").setValue(username);
                            requestsRef.child(requestId).child("requestId").setValue(requestId);
                            requestsRef.child(requestId).child("branch").setValue(getIntent().getStringExtra("BRANCH_ID"));
                            requestsRef.child(requestId).child("docs").setValue(filledDocs)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Write was successful!
                                            // Display a success Toast message
                                            Toast.makeText(RequestForServiceActivity.this, "Request submitted successfully!", Toast.LENGTH_SHORT).show();
                                            SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
                                            prefs.edit().putBoolean("showRatingPopup", true).apply();
                                            // Redirect to MainPageClient
                                            Intent intent = new Intent(RequestForServiceActivity.this, MainPageClient.class);
                                            intent.putExtra("username",username);
                                            intent.putExtra("REQUEST_ID",requestId);
                                            intent.putExtra("BRANCH_ID",getIntent().getStringExtra("BRANCH_ID"));
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Write failed
                                            // Display an error Toast message
                                            Toast.makeText(RequestForServiceActivity.this, "Failed to submit request.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Handle possible errors.
                        }
                    });
                }
                else if (!(formStatus.getText().toString()=="Complete")){
                    Toast.makeText(getApplicationContext(), "Form fields must be filled", Toast.LENGTH_SHORT).show();
                }
                else if (!(docsStatus.getText().toString()=="Complete")){
                    Toast.makeText(getApplicationContext(), "Docs fields must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void updateDocsStatus() {
        boolean allFilled = areAllDocsFieldsFilled();
        TextView statusText = findViewById(R.id.docsStatus);
        if (allFilled) {
            statusText.setText("Complete");
            statusText.setTextColor(Color.GREEN);
        } else {
            statusText.setText("Incomplete");
            statusText.setTextColor(Color.RED);
        }
    }

    private boolean areAllDocsFieldsFilled() {
        for (String docField : docsFieldsList) {
            if (!filledDocs.containsKey(docField) || filledDocs.get(docField).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void fetchServiceData() {
        // Fetch le nom du service
        serviceRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String serviceName = dataSnapshot.getValue(String.class);
                serviceNameEditText.setText("New " + serviceName + " Request");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Gerer les erreurs possibles.
            }
        });

        // Fetch à partir des champs
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
                // Gerer les erreurs possibles .
            }
        });

        // Fetch les documents à partir des fields
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
                // Gerer les erreurs possibles.
            }
        });
    }



}