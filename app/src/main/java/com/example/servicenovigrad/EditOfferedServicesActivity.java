package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private List<String> serviceNames = new ArrayList<>();
    private Map<String, Service> allServices = new HashMap<>();
    private DatabaseReference servicesRef;
    private String branchId;
    private List<String> currentServiceIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_offered_services);

        branchId = getIntent().getStringExtra("BRANCH_ID");
        servicesListView = findViewById(R.id.service_list_view2);
        servicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, serviceNames);
        servicesListView.setAdapter(adapter);

        servicesRef = FirebaseDatabase.getInstance().getReference("services");
        loadServicesFromFirebase();

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
                loadCurrentServices();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to load services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCurrentServices() {
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId).child("servicesOffered");
        branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    String serviceId = serviceSnapshot.getKey();
                    currentServiceIds.add(serviceId);
                }
                markCurrentServices();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to load current services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void markCurrentServices() {
        for (int i = 0; i < servicesListView.getCount(); i++) {
            String serviceName = adapter.getItem(i);
            Service service = allServices.get(serviceName);
            if (service != null && currentServiceIds.contains(service.getId())) {
                servicesListView.setItemChecked(i, true);
            }
        }
    }

    private void confirmSelection() {
        // Get the selected services from the list view
        List<Service> selectedServices = new ArrayList<>();
        for (int i = 0; i < servicesListView.getCount(); i++) {
            if (servicesListView.isItemChecked(i)) {
                String serviceName = adapter.getItem(i);
                Service service = allServices.get(serviceName);
                if (service != null) {
                    selectedServices.add(service); // Assuming you have a Service class with id and name
                }
            }
        }

        // Prepare to update the Firebase database
        DatabaseReference branchServicesRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId).child("servicesOffered");
        branchServicesRef.setValue(selectedServices).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(EditOfferedServicesActivity.this, "Services updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditOfferedServicesActivity.this, "Failed to update services", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
