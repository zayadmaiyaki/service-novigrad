package com.example.servicenovigrad;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        EditText usernameEditText=findViewById(R.id.usernameEditText);
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();

                if (!username.isEmpty()) {
                    db.collection("users").whereEqualTo("username", username)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            // User exists, proceed to delete
                                            for (QueryDocumentSnapshot document : querySnapshot) {
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
                                            // User does not exist, show error message
                                            Toast.makeText(MainPageAdmin.this, "No user found with that username.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(MainPageAdmin.this, "Error searching for user", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(MainPageAdmin.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }


}