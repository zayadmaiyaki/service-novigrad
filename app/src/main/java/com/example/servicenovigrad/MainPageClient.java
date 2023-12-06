package com.example.servicenovigrad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class MainPageClient extends AppCompatActivity {

    private TextView textClientPage;
    private TextView textView;
    private ListView ongoingRequestsListView;
    private Button newRequestButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> ongoingRequests;
    private double averageRating1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_client);

        // Views initialisation
        textClientPage = findViewById(R.id.textClientPage);
        textView = findViewById(R.id.textView);
        ongoingRequestsListView = findViewById(R.id.ongoingRequestsListView);
        newRequestButton = findViewById(R.id.newRequestButton);

        textClientPage.setText("Client Page");
        textView.setText("Ongoing Requests");

        ongoingRequests = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ongoingRequests);
        ongoingRequestsListView.setAdapter(adapter);

        String requestId = getIntent().getStringExtra("REQUEST_ID");

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("requests");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ongoingRequests.clear();
                String username=getIntent().getStringExtra("username");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String serviceName = snapshot.getKey();
                    String requestUsername = snapshot.child("username").getValue(String.class);
                    if(username != null && username.equals(requestUsername)){
                        ongoingRequests.add(serviceName);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainPageClient.this, "Failed to load requested services.", Toast.LENGTH_LONG).show();
            }
        });

        newRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageClient.this, NewRequestPageActivity.class);
                String username=getIntent().getStringExtra("username");
                intent.putExtra("username",username);
                intent.putExtra("RATING",averageRating1);

                startActivity(intent);
            }
        });

        ongoingRequestsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Show status
                showLongClickDialog(ongoingRequests.get(position));
                return true;
            }
        });
    }


    // Show Dialog
    private void showLongClickDialog(final String selectedItem) {
        DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("requests").child(selectedItem).child("status");

        // Retrieve the status from Firebase
        statusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                String statusMessage = "Status of request '" + selectedItem + "': " + status;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainPageClient.this);
                builder.setTitle("Request Status")
                        .setMessage(statusMessage)
                        .setPositiveButton("Go to Request", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Create an intent for NewRequestPageActivity
                                Intent intent = new Intent(MainPageClient.this, DisplayRequest.class);
                                // Put the selectedItem as an extra in the intent
                                intent.putExtra("REQUEST_ID", selectedItem);
                                intent.putExtra("BRANCH_ID", getIntent().getStringExtra("BRANCH_ID"));
                                startActivity(intent);
                                dialog.dismiss(); // Close the dialog
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss(); // Close the dialog
                            }
                        });
                builder.create().show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainPageClient.this, "Failed to retrieve request status.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("MyApp", MODE_PRIVATE);
        boolean showPopup = prefs.getBoolean("showRatingPopup", false);

        if (showPopup) {
            showRatingDialog();
            prefs.edit().putBoolean("showRatingPopup", false).apply(); // Reset the flag
        }
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate the Branch");

        // Add rating input (e.g., a RatingBar)
        final RatingBar ratingBar = new RatingBar(this);
        builder.setView(ratingBar);

        // Add action buttons
        builder.setPositiveButton("Submit", (dialog, id) -> {
            // User clicked Submit button
            float rating = ratingBar.getRating();
            submitRating(getIntent().getStringExtra("BRANCH_ID"),rating); // Implement this method to handle rating submission
        });
        builder.setNegativeButton("Cancel", (dialog, id) -> {
            // User cancelled the dialog
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void submitRating(String branchId, float newRating) {
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId);

        branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long totalRatings = 0;
                double sumOfRatings = 0.0;

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("totalRatings") && dataSnapshot.child("totalRatings").getValue() != null) {
                        totalRatings = (long) dataSnapshot.child("totalRatings").getValue();
                    }
                    if (dataSnapshot.hasChild("sumOfRatings") && dataSnapshot.child("sumOfRatings").getValue() != null) {
                        sumOfRatings = (double) dataSnapshot.child("sumOfRatings").getValue();
                    }
                }

                // Update the ratings
                totalRatings += 1;
                sumOfRatings += newRating;
                double newAverage = totalRatings > 0 ? sumOfRatings / totalRatings : 0;
                averageRating1=newAverage;

                // Update Firebase
                updateBranchRating(branchId, totalRatings, sumOfRatings, newAverage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    private void updateBranchRating(String branchId, long totalRatings, double sumOfRatings, double newAverage) {
        DatabaseReference branchRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("totalRatings", totalRatings);
        updates.put("sumOfRatings", sumOfRatings);
        updates.put("averageRating", newAverage);

        branchRef.updateChildren(updates);
    }

}

