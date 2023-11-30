package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainPageEmployee extends AppCompatActivity {

    private ListView listViewBranch;
    private List<String> branchNames;
    private List<String> branchIds; // Store branch IDs
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_employee);

        Button buttonCreateBranch = findViewById(R.id.buttonCreateBranch);
        listViewBranch = findViewById(R.id.listViewBranch);
        branchNames = new ArrayList<>();
        branchIds = new ArrayList<>(); // Initialize the list for branch IDs

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, branchNames);
        listViewBranch.setAdapter(adapter);

        buttonCreateBranch.setOnClickListener(view -> {
            Intent intent = new Intent(MainPageEmployee.this, CreateBranchActivity.class);
            startActivity(intent);
        });

        listViewBranch.setOnItemClickListener((parent, view, position, id) -> {
            // Get the branch ID corresponding to the clicked item
            String selectedBranchId = branchIds.get(position);
            // Create an intent and start the BranchPageActivity
            Intent intent = new Intent(MainPageEmployee.this, BranchPageActivity.class);
            intent.putExtra("BRANCH_ID", selectedBranchId);
            startActivity(intent);
        });

        loadBranches();
    }

    private void loadBranches() {
        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                branchNames.clear();
                branchIds.clear(); // Also clear the branch IDs list
                for (DataSnapshot branchSnapshot : dataSnapshot.getChildren()) {
                    Branch branch = branchSnapshot.getValue(Branch.class);
                    if (branch != null) {
                        branchNames.add(branch.getName());
                        branchIds.add(branchSnapshot.getKey()); // Store the branch ID
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
            }
        });
    }
}