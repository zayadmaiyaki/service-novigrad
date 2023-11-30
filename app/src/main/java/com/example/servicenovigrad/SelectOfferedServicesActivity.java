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

public class SelectOfferedServicesActivity extends AppCompatActivity {

    private ListView servicesListView;
    private ArrayAdapter<String> adapter;
    private final List<String> serviceNames = new ArrayList<>();
    private final Map<String, Service> allServices = new HashMap<>();
    private DatabaseReference servicesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_offered_services);

        servicesListView = findViewById(R.id.service_list_view);
        servicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, serviceNames);
        servicesListView.setAdapter(adapter);

        servicesRef = FirebaseDatabase.getInstance().getReference("services");
        loadServicesFromFirebase();

        Button confirmButton = findViewById(R.id.confirmButton);
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
                Toast.makeText(SelectOfferedServicesActivity.this, "Failed to load services.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmSelection() {
        ArrayList<String> selectedServiceIds = new ArrayList<>();
        for (int i = 0; i < servicesListView.getCount(); i++) {
            if (servicesListView.isItemChecked(i)) {
                String serviceName = adapter.getItem(i);
                Service service = allServices.get(serviceName);
                if (service != null) {
                    selectedServiceIds.add(service.getId());
                }
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedServiceIds", selectedServiceIds);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}