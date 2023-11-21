package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainPageAdmin extends AppCompatActivity {
    private ListView adminPageServiceList;
    private ArrayAdapter<Service> adapter;
    private ArrayList<Service> servicesList = new ArrayList<>();
    private DatabaseReference servicesRef;
    private Button createServiceButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);

        adminPageServiceList = findViewById(R.id.adminPageServiceList);
        createServiceButton = findViewById(R.id.createServiceButton);

        // Initialize adapter
        adapter = new ArrayAdapter<Service>(this, android.R.layout.simple_list_item_1, servicesList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Inflate custom layout if you have one
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setText(servicesList.get(position).getName());
                return view;
            }
        };
        adminPageServiceList.setAdapter(adapter);

        // Set up Firebase
        servicesRef = FirebaseDatabase.getInstance().getReference("services");

        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                servicesList.clear(); // Clear the old list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        servicesList.add(service);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter of the dataset change
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });

        createServiceButton.setOnClickListener(view -> {
            // Redirect to Create Service page
            Intent intent = new Intent(MainPageAdmin.this, CreateServiceActivity.class);
            startActivity(intent);
        });

        adminPageServiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Service service = servicesList.get(position);
                // Redirect to Edit Service page with service ID
                Intent intent = new Intent(MainPageAdmin.this, EditServiceActivity.class);
                intent.putExtra("serviceId", service.getId());
                startActivity(intent);
            }
        });


        adminPageServiceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Service service = servicesList.get(position);
                Intent intent = new Intent(MainPageAdmin.this, EditServiceActivity.class);
                intent.putExtra("serviceId", service.getId()); // pass the service ID to the edit page
                startActivity(intent);
                return true; // return true to indicate the click was handled
            }
        });





    }


}