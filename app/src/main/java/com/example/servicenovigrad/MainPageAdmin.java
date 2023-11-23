package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainPageAdmin extends AppCompatActivity {

    private ArrayList<String> services;
    private ArrayAdapter<String> adapter;
    ListView listViewServices;
    Button createServiceButton;
    DatabaseReference databaseServices;

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);

        createServiceButton = findViewById(R.id.createServiceButton);
        listViewServices = findViewById(R.id.adminPageServiceList);

        // Initialize your array list and adapter
        services = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, services);
        listViewServices.setAdapter(adapter);

        // Initialize Firebase Database reference
        databaseServices = FirebaseDatabase.getInstance().getReference("services");
        db = FirebaseFirestore.getInstance();

        createServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate a unique id for the new service
                String id = databaseServices.push().getKey();

                // Add the new service to the database
                Service service = new Service(id, "New Service");
                databaseServices.child(id).setValue(service);

                // Update the local list and notify the adapter
                services.add(service.getName());
                adapter.notifyDataSetChanged();

                // Redirect to the CreateServiceActivity page
                Intent intent = new Intent(MainPageAdmin.this, CreateServiceActivity.class);
                intent.putExtra("serviceId", id); // Pass the service id to the CreateServiceActivity
                startActivity(intent);
            }
        });

        listViewServices.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the service name
                String serviceName = services.get(position);

                // Create an intent to start CreateEditServiceActivity
                Intent intent = new Intent(MainPageAdmin.this, CreateServiceActivity.class);

                // Pass the service name to CreateEditServiceActivity
                intent.putExtra("serviceName", serviceName);

                // Start the activity
                startActivity(intent);

                return true; // return true to indicate that the long click was handled
            }
        });

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        Button deleteButton = findViewById(R.id.deleteButton);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();

                if (!username.isEmpty()) {
                    // Query the users collection for the document with the matching username
                    db.collection("users").whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Once we find the document, we delete it
                                            db.collection("users").document(document.getId()).delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(MainPageAdmin.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainPageAdmin.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(MainPageAdmin.this, "Failed to find user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainPageAdmin.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();

                if (!username.isEmpty()) {
                    // Query the users collection for the document with the matching username
                    db.collection("users").whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Once we find the document, we delete it
                                            db.collection("users").document(document.getId()).delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(MainPageAdmin.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(MainPageAdmin.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(MainPageAdmin.this, "Failed to find user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainPageAdmin.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        databaseServices.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the previous service list
                services.clear();

                // Iterate through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Get service
                    Service service = postSnapshot.getValue(Service.class);
                    // Add service to the list
                    services.add(service.getName());
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}