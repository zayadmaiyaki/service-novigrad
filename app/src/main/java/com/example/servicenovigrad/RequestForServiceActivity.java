package com.example.servicenovigrad;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestForServiceActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private HashMap<String, String> formValues;
    private HashMap<String, String> docsValues;
    private ArrayAdapter<String> formAdapter;
    private ArrayAdapter<String> docsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_for_service);
/**
        database = FirebaseDatabase.getInstance();

        String serviceId = getIntent().getStringExtra("serviceId");
        myRef = database.getReference("services").child(serviceId);

        formValues = new HashMap<>();
        docsValues = new HashMap<>();

        ListView formListView = findViewById(R.id.formFieldListViewRequest);
        ListView docsListView = findViewById(R.id.docsFieldListViewRequest);

        formAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        docsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());

        formListView.setAdapter(formAdapter);
        docsListView.setAdapter(docsAdapter);

        // Load the field names from Firebase
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                // Assuming "formFields" and "docsFields" are the keys in your Firebase database
                Map<String, Object> formFields = (Map<String, Object>) map.get("formFields");
                Map<String, Object> docsFields = (Map<String, Object>) map.get("docsFields");

                // Now you can get the field names
                for (String key : formFields.keySet()) {
                    // key is the field name
                    formValues.put(key, "");
                    formAdapter.add(key);
                }

                for (String key : docsFields.keySet()) {
                    // key is the field name
                    docsValues.put(key, "");
                    docsAdapter.add(key);
                }

                // Notify the adapters that the data has changed
                formAdapter.notifyDataSetChanged();
                docsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        formListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String fieldName = formAdapter.getItem(position);  // Get the field name

                AlertDialog.Builder builder = new AlertDialog.Builder(RequestForServiceActivity.this);
                builder.setTitle("Enter Field Value");

                // Set up the input
                final EditText input = new EditText(RequestForServiceActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String userInput = input.getText().toString();
                        formValues.put(fieldName, userInput);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
                return true;
            }
        });


        Button submitButton = findViewById(R.id.buttonSubmitRequest);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceName = "YourServiceName";  // Replace with your service name
                String requestId = serviceName + "request" + System.currentTimeMillis();  // Unique ID for the request

                myRef.child(requestId).child("form").setValue(formValues);
                myRef.child(requestId).child("docs").setValue(docsValues);
            }
        });         */
    }
}