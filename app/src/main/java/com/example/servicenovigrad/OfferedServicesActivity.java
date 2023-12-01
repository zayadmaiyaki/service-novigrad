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

        String branchId = getIntent().getStringExtra("BRANCH_ID");
        if (branchId != null) {
            loadOfferedServices(branchId);
        } else {
            Toast.makeText(this, "Branch ID not found.", Toast.LENGTH_LONG).show();
            finish();
        }

        selectedServicesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected service name
                String selectedServiceName = offeredServiceNames.get(position);

                // Fetch the details for the selected service from Firebase
                DatabaseReference serviceRef = FirebaseDatabase.getInstance().getReference("services");
                serviceRef.orderByChild("name").equalTo(selectedServiceName).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                            DataSnapshot serviceSnapshot = dataSnapshot.getChildren().iterator().next();
                            Service service = serviceSnapshot.getValue(Service.class);
                            if (service != null) {
                                showServiceDetailsDialog(service);
                            } else {
                                Toast.makeText(OfferedServicesActivity.this, "Service details not found.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(OfferedServicesActivity.this, "Service not found in the database.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(OfferedServicesActivity.this, "Failed to load service details.", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
            }
        });
    }

    private void loadOfferedServices(String branchId) {
        branchesRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId).child("servicesOffered");
        branchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                offeredServiceNames.clear();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    String serviceName = serviceSnapshot.child("name").getValue(String.class);
                    if (serviceName != null && !serviceName.trim().isEmpty()) {
                        offeredServiceNames.add(serviceName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OfferedServicesActivity.this, "Failed to load services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showServiceDetailsDialog(Service service) {
        // Créer un dialogue
        AlertDialog.Builder builder = new AlertDialog.Builder(OfferedServicesActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_services, null);
        builder.setView(dialogView);

        // Details du service
        TextView serviceNameTextView = dialogView.findViewById(R.id.ServiceNameDialogText);
        ListView formFieldsListView = dialogView.findViewById(R.id.FormFieldDialogListView);
        ListView docsFieldsListView = dialogView.findViewById(R.id.DocsFieldDialogListView);

        serviceNameTextView.setText(service.getName());
        ArrayAdapter<String> formFieldsAdapter = new ArrayAdapter<>(OfferedServicesActivity.this,
                android.R.layout.simple_list_item_1, service.getFormFields());
        formFieldsListView.setAdapter(formFieldsAdapter);

        ArrayAdapter<String> docsFieldsAdapter = new ArrayAdapter<>(OfferedServicesActivity.this,
                android.R.layout.simple_list_item_1, service.getDocsFields());
        docsFieldsListView.setAdapter(docsFieldsAdapter);

        // Afficher le dialogue
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
