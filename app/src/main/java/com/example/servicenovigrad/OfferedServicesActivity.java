package com.example.servicenovigrad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OfferedServicesActivity extends AppCompatActivity {

    private ListView selectedServicesListView;
    private ArrayAdapter<String> adapter;
    private List<String> offeredServiceNames = new ArrayList<>();
    private DatabaseReference branchesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offered_services);

        selectedServicesListView = findViewById(R.id.selected_services_list_view);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, offeredServiceNames);
        selectedServicesListView.setAdapter(adapter);

        // Get the branch ID from the intent
        String branchId = getIntent().getStringExtra("BRANCH_ID");
        if (branchId != null) {
            loadOfferedServices(branchId);
        } else {
            Toast.makeText(this, "Branch ID not found.", Toast.LENGTH_LONG).show();
            finish(); // Close the activity if there is no branch ID
        }
    }

    private void loadOfferedServices(String branchId) {
        branchesRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId).child("serviceOfferred");
        branchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offeredServiceNames.clear();
                if (!dataSnapshot.exists()) {
                    Toast.makeText(OfferedServicesActivity.this, "ServiceOffered node does not exist for this branch.", Toast.LENGTH_LONG).show();
                    return;
                }

                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    // Extracting the service name directly
                    String serviceName = serviceSnapshot.child("name").getValue(String.class);
                    if (serviceName != null && !serviceName.trim().isEmpty()) {
                        offeredServiceNames.add(serviceName);
                    } else {
                        Log.d("OfferedServicesActivity", "Service name is null or empty for key: " + serviceSnapshot.getKey());
                    }
                }

                if (offeredServiceNames.isEmpty()) {
                    Toast.makeText(OfferedServicesActivity.this, "No services offered by this branch.", Toast.LENGTH_LONG).show();
                } else {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OfferedServicesActivity.this, "Failed to load services: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        selectedServicesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected service ID
                String selectedServiceId = offeredServiceNames.get(position); // This should be the ID, not the name

                // Create the dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(OfferedServicesActivity.this);

                // Inflate the dialog_services layout
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_services, null);

                // Set the custom layout as the dialog's view
                builder.setView(dialogView);

                TextView serviceNameTextView = dialogView.findViewById(R.id.ServiceNameDialogText);
                ListView formFieldsListView = dialogView.findViewById(R.id.FormFieldDialogListView);
                ListView docsFieldsListView = dialogView.findViewById(R.id.DocsFieldDialogListView);

                // Retrieve the service details from the database
                DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference("services").child(selectedServiceId);
                serviceRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Service service = dataSnapshot.getValue(Service.class);
                        if (service != null) {
                            serviceNameTextView.setText(service.getName());
                            ArrayAdapter<String> formFieldsAdapter = new ArrayAdapter<>(OfferedServicesActivity.this,
                                    android.R.layout.simple_list_item_1, service.getFormFields());
                            formFieldsListView.setAdapter(formFieldsAdapter);

                            ArrayAdapter<String> docsFieldsAdapter = new ArrayAdapter<>(OfferedServicesActivity.this,
                                    android.R.layout.simple_list_item_1, service.getDocsFields());
                            docsFieldsListView.setAdapter(docsFieldsAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(OfferedServicesActivity.this, "Failed to load service details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Close", (dialog, which) -> dialog.dismiss());
                builder.show();
                return true; // return true because the long click is handled
            }
        });


    }

}
