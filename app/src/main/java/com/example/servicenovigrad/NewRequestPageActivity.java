/*I want that when the location spinner is clicked the user can enter it location and it
will display branches by location, when the working times spinner is selected user can check
days from monday to  sunday and select an hour interval and it will display branches in order
according to that and finally when we select the offered services the user can check one or plus
services that are in the firebase and it will display the branches who offer the selected services
for the working times and the services offered services filters if none of the existing branches
respect the filter it display a message to say that no branches are found with your selectionned
 filters
 */
/*package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class NewRequestPageActivity extends AppCompatActivity {

    private EditText searchBranchNameEditText, locationInputEditText, offeredServicesEditText;
    private LinearLayout branchesLinearLayout;
    private Button applyFilterButton;
    private List<Branch> originalBranchesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_page);

        initializeUI();
        loadBranches();
    }

    private void initializeUI() {
        searchBranchNameEditText = findViewById(R.id.search_branch_name);
        locationInputEditText = findViewById(R.id.location_input);
        offeredServicesEditText = findViewById(R.id.offered_services);
        branchesLinearLayout = findViewById(R.id.branches_linear_layout);
        applyFilterButton = findViewById(R.id.button_filter);

        applyFilterButton.setOnClickListener(view -> applyFilter());
    }

    private void loadBranches() {
        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalBranchesList.clear();
                branchesLinearLayout.removeAllViews(); // Clear all views before adding new ones
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch branch = snapshot.getValue(Branch.class);
                    if (branch != null) {
                        originalBranchesList.add(branch);
                        addBranchNameToView(branch.getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewRequestPageActivity.this, "Failed to load branches.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilter() {
        // Implement your filtering logic here
        // After filtering, call addBranchNameToView for each branch name you want to display
    }

    private void addBranchNameToView(String branchName) {
        TextView textView = new TextView(this);
        textView.setText(branchName);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setOnClickListener(new View.OnClickListener() {
             listViewBranch.setOnItemClickListener((parent, view, position, id) -> {
                // Get the branch ID corresponding to the clicked item
                String selectedBranchId = branchIds.get(position);
                // Create an intent and start the ClientBranchPageActivity
                Intent intent = new Intent(NewRequestPageActivity.this, ClientBranchPageActivity.class);
                intent.putExtra("BRANCH_ID", selectedBranchId);
                startActivity(intent);
            });



        branchesLinearLayout.addView(textView);
    }

    // Implement your filterBranches method as before
}*/
package com.example.servicenovigrad;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewRequestPageActivity extends AppCompatActivity {

    private EditText searchBranchNameEditText, locationInputEditText, offeredServicesEditText;
    private ListView listViewBranches;
    private List<String> branchNames;
    private List<String> branchIds;
    private ArrayAdapter<String> adapter;
    private Button applyFilterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_page);

        initializeUI();
        loadBranches();
    }

    private void initializeUI() {
        searchBranchNameEditText = findViewById(R.id.search_branch_name);
        locationInputEditText = findViewById(R.id.location_input);
        offeredServicesEditText = findViewById(R.id.offered_services);
        listViewBranches = findViewById(R.id.branches_list_view); // Make sure the ID matches in your XML
        applyFilterButton = findViewById(R.id.button_filter);

        branchNames = new ArrayList<>();
        branchIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, branchNames);
        listViewBranches.setAdapter(adapter);

        applyFilterButton.setOnClickListener(view -> applyFilter());

        listViewBranches.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBranchId = branchIds.get(position);
            Intent intent = new Intent(NewRequestPageActivity.this, ClientBranchPageActivity.class);
            intent.putExtra("BRANCH_ID", selectedBranchId);
            startActivity(intent);
        });
    }

    private void loadBranches() {
        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                branchNames.clear();
                branchIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch branch = snapshot.getValue(Branch.class);
                    if (branch != null) {
                        branchNames.add(branch.getName());
                        branchIds.add(snapshot.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(NewRequestPageActivity.this, "Failed to load branches.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyFilter() {
        // Implement your filtering logic here
        // After filtering, update the adapter's data
    }
}


