package com.example.servicenovigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;

public class DisplayRequest extends AppCompatActivity {

    private ListView formFieldsListView, docsFieldsListView;
    private ArrayAdapter<String> formFieldsAdapter, docsFieldsAdapter;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_request);

        TextView requestNameTextView = findViewById(R.id.displayRequestName);
        formFieldsListView = findViewById(R.id.displayFormFieldValues);
        docsFieldsListView = findViewById(R.id.displayDocsFieldsValues);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Retrieve the Request ID from the Intent
        String requestId = getIntent().getStringExtra("REQUEST_ID"); // Adjust "REQUEST_ID" as per your Intent key

        // Fetch data for Form Fields and Request Name
        mDatabase.child("requests").child(requestId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Get the request name (service name) and set it in the TextView
                        String serviceName = dataSnapshot.child("serviceRequested").getValue(String.class);
                        requestNameTextView.setText(serviceName);

                        // Fetch form fields
                        DataSnapshot formSnapshot = dataSnapshot.child("form");
                        ArrayList<String> formFieldsList = new ArrayList<>();
                        for (DataSnapshot snapshot : formSnapshot.getChildren()) {
                            String value = snapshot.getKey() + ": " + snapshot.getValue(String.class);
                            formFieldsList.add(value);
                        }
                        formFieldsAdapter = new ArrayAdapter<>(DisplayRequest.this, android.R.layout.simple_list_item_1, formFieldsList);
                        formFieldsListView.setAdapter(formFieldsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });

        // Fetch data for Document Fields
        mDatabase.child("requests").child(requestId).child("docs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<String> docsFieldsList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String value = snapshot.getKey() + ": " + snapshot.getValue(String.class);
                            docsFieldsList.add(value);
                        }
                        docsFieldsAdapter = new ArrayAdapter<>(DisplayRequest.this, android.R.layout.simple_list_item_1, docsFieldsList);
                        docsFieldsListView.setAdapter(docsFieldsAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle error
                    }
                });
    }
}