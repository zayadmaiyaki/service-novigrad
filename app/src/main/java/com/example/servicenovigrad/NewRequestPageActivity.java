/*I want that when the location spinner is clicked the user can enter it location and it
will display branches by location, when the working times spinner is selected user can check
days from monday to  sunday and select an hour interval and it will display branches in order
according to that and finally when we select the offered services the user can check one or plus
services that are in the firebase and it will display the branches who offer the selected services
for the working times and the services offered services filters if none of the existing branches
respect the filter it display a message to say that no branches are found with your selectionned
 filters
 */
package com.example.servicenovigrad;

import android.os.Bundle;
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
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        branchesLinearLayout.addView(textView);
    }

    // Implement your filterBranches method as before
}

