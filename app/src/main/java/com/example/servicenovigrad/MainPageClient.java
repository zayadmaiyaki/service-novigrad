package com.example.servicenovigrad;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainPageClient extends AppCompatActivity {

    private TextView textClientPage;
    private TextView textView;
    private ListView ongoingRequestsListView;
    private Button newRequestButton;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> ongoingRequests;

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

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("requests");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ongoingRequests.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String serviceName = snapshot.getKey();
                    ongoingRequests.add(serviceName);
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
    private void showLongClickDialog(String selectedItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request Status")
                .setMessage("Status of request '" + selectedItem + "': In Progress")
                .setPositiveButton("Go to Request", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MainPageClient.this, NewRequestPageActivity.class);
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
    }

}

