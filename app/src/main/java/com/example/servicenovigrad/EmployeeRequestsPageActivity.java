package com.example.servicenovigrad;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeRequestsPageActivity extends AppCompatActivity {

    private TextView testRequestPage;
    private ListView RequestsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> ongoingRequests;
    private DatabaseReference dbRef;
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        // Views initialization
        testRequestPage = findViewById(R.id.textRequestsPage);
        RequestsListView = findViewById(R.id.requestsListView);

        testRequestPage.setText("Active requests page");

        ongoingRequests = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ongoingRequests);
        RequestsListView.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference("requests");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ongoingRequests.clear();
                String username = getIntent().getStringExtra("username");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String serviceName = snapshot.getKey();
                    requestId = snapshot.child("requestId").getValue(String.class);
                    String branchId = getIntent().getStringExtra("BRANCH_ID");
                    String actualbranch = snapshot.child("branch").getValue(String.class);
                    if (branchId != null && branchId.equals(actualbranch)) {
                        ongoingRequests.add(serviceName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EmployeeRequestsPageActivity.this, "Failed to load requested services.", Toast.LENGTH_LONG).show();
            }
        });

        // Set long-click listener for ListView items
        RequestsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedServiceName = ongoingRequests.get(position);
                showRequestOptionsDialog(selectedServiceName);
                return true;
            }
        });
    }

    private void showRequestOptionsDialog(final String selectedServiceName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request Options");
        builder.setItems(new CharSequence[]{"Approve", "Decline", "Go to Request"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Handle Approve
                        setRequestStatus(selectedServiceName, "approved");
                        break;
                    case 1:
                        // Handle Decline
                        setRequestStatus(selectedServiceName, "declined");
                        break;
                    case 2:
                        // Handle Go to Request
                        // Start the DisplayRequest activity with the selectedServiceName
                        Intent intent = new Intent(EmployeeRequestsPageActivity.this, DisplayRequest.class);
                        intent.putExtra("REQUEST_ID", requestId); // Assuming REQUEST_ID is the service name
                        startActivity(intent);
                        break;
                }
            }
        });
        builder.create().show();
    }
    private void setRequestStatus(String serviceName, String status) {
        DatabaseReference requestRef = dbRef.child(serviceName).child("status");
        requestRef.setValue(status);
    }
}