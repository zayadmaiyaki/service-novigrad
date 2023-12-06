package com.example.servicenovigrad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestsPageActivity extends AppCompatActivity {

    private TextView testRequestPage;
    private ListView RequestsListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> ongoingRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_page);

        // Views initialisation
        testRequestPage = findViewById(R.id.textRequestsPage);
        RequestsListView = findViewById(R.id.requestsListView);

        testRequestPage.setText("Active requests page");

        ongoingRequests = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ongoingRequests);
        RequestsListView.setAdapter(adapter);

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("requests");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ongoingRequests.clear();
                String username = getIntent().getStringExtra("username");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String serviceName = snapshot.getKey();
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
                Toast.makeText(RequestsPageActivity.this, "Failed to load requested services.", Toast.LENGTH_LONG).show();
            }
        });


        /*RequestsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Show status
                showLongClickDialog(ongoingRequests.get(position));
                return true;
            }
        });
    }


    // Show Dialog
    private void showLongClickDialog(String selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request Status")
                .setMessage("Status of request '" + selectedItem + "': In Progress")
                .setPositiveButton("Go to Request", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(RequestsPageActivity.this, NewRequestPageActivity.class);
                        startActivity(intent);
                        dialog.dismiss(); // Ferme le dialogue
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Ferme le dialogue
                    }
                });
        builder.create().show();
    }*/
    }
}