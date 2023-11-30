package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditOfferedServicesActivity extends AppCompatActivity {

    private ListView servicesListView;
    private ArrayAdapter<String> adapter;
    private final List<String> serviceNames = new ArrayList<>();
    private final Map<String, Service> allServices = new HashMap<>();
    private List<String> currentlyOfferedServices = new ArrayList<>();
    private String branchId;
    private DatabaseReference servicesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offered_services);

        servicesListView = findViewById(R.id.service_list_view2);
        servicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, serviceNames);
        servicesListView.setAdapter(adapter);

        branchId = getIntent().getStringExtra("BRANCH_ID");
        servicesRef = FirebaseDatabase.getInstance().getReference("services");
        loadServicesFromFirebase();
        loadCurrentlyOfferedServices();

        Button confirmButton = findViewById(R.id.confirmButton2);
        confirmButton.setOnClickListener(v -> confirmSelection());
    }

    private void loadServicesFromFirebase() {
        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceNames.clear();
                allServices.clear();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    Service service = serviceSnapshot.getValue(Service.class);
                    if (service != null) {
                        serviceNames.add(service.getName());
                        allServices.put(service.getName(), service);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to load services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCurrentlyOfferedServices() {
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId).child("serviceOffered");
        branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentlyOfferedServices.clear();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    String serviceName = serviceSnapshot.child("name").getValue(String.class);
                    if (serviceName != null) {
                        currentlyOfferedServices.add(serviceName);
                    }
                }
                markCurrentlyOfferedServices();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to load current services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markCurrentlyOfferedServices() {
        for (int i = 0; i < serviceNames.size(); i++) {
            if (currentlyOfferedServices.contains(serviceNames.get(i))) {
                servicesListView.setItemChecked(i, true);
            }
        }
    }

    private void confirmSelection() {
        // Get the count of ListView items
        int itemCount = servicesListView.getCount();

        // Prepare a list to store the updated offered services' names
        List<String> updatedOfferedServicesNames = new ArrayList<>();

        // Iterate over all services
        for (int i = 0; i < itemCount; i++) {
            if (servicesListView.isItemChecked(i)) {
                String serviceName = adapter.getItem(i);
                // Add the service name to the list if it's checked
                updatedOfferedServicesNames.add(serviceName);
            }
        }

        // Update the branch's serviceOffered node with the new list of service names
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId);
        branchRef.child("serviceOffered").setValue(updatedOfferedServicesNames).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditOfferedServicesActivity.this, "Services updated successfully", Toast.LENGTH_SHORT).show();
                // If you want to refresh the OfferedServicesActivity upon return, consider using startActivityForResult when starting EditOfferedServicesActivity
                finish(); // Close the activity
            } else {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to update services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}