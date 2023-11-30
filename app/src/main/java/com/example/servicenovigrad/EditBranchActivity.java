package com.example.servicenovigrad;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditBranchActivity extends AppCompatActivity {
    private EditText branchNameEditText, branchPhoneNumberEditText, branchAddressEditText;
    private Map<String, String> workingTimes = new HashMap<>();
    private List<Service> servicesOffered = new ArrayList<>();
    private String branchId;
    private Map<Integer, Button> dayButtons = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_branch);

        branchId = getIntent().getStringExtra("BRANCH_ID");

        branchNameEditText = findViewById(R.id.editTextBranchName);
        branchPhoneNumberEditText = findViewById(R.id.branchPhoneNumberText2);
        branchAddressEditText = findViewById(R.id.branchAddressText2);
        Button buttonSaveChanges = findViewById(R.id.buttonSaveChanges);
        Button editOfferedServices=findViewById(R.id.editOfferedServices);

        setupDayButtons();
        updateDayButtonColors();

        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBranchChanges();
            }
        });

        editOfferedServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditBranchActivity.this, EditOfferedServicesActivity.class);
                intent.putExtra("BRANCH_ID", branchId);
                startActivity(intent);
            }
        });

        loadBranchDetails();
    }



    private void setupDayButtons() {
        int[] dayButtonIds = {
                R.id.buttonMonday2, R.id.buttonTuesday2, R.id.buttonWednesday2,
                R.id.buttonThursday2, R.id.buttonFriday2, R.id.buttonSaturday2, R.id.buttonSunday2
        };
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < dayButtonIds.length; i++) {
            Button dayButton = findViewById(dayButtonIds[i]);
            dayButtons.put(dayButtonIds[i], dayButton);
            final String day = days[i];
            dayButton.setOnClickListener(v -> showWorkingHoursDialog(day)
            );
        }
    }
    private void showWorkingHoursDialog(final String dayKey) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_working_hours); // make sure you have this layout

        final EditText editTextWorkingHours = dialog.findViewById(R.id.editTextWorkingHours);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

        // Pre-populate the dialog if hours were already set
        String existingHours = workingTimes.get(dayKey);
        if (existingHours != null) {
            editTextWorkingHours.setText(existingHours);
        }

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonOk.setOnClickListener(v -> {
            String hours = editTextWorkingHours.getText().toString();

            workingTimes.put(dayKey, hours);

            updateDayButtonColors();
            dialog.dismiss();
        });

        dialog.show();
    }
    private void updateDayButtonColors() {
        for (Map.Entry<Integer, Button> entry : dayButtons.entrySet()) {
            Button dayButton = entry.getValue();
            // Extract day key from button ID
            String dayKey = getResources().getResourceEntryName(entry.getKey())
                    .replace("button", "");

            // Remove any numbers at the end of the dayKey (if your IDs are like buttonMonday2, buttonTuesday2, etc.)
            dayKey = dayKey.replaceAll("\\d", "");

            String hours = workingTimes.getOrDefault(dayKey, "Closed");
            if (!hours.equals("Closed") && !hours.isEmpty()) {
                dayButton.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                dayButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }
    }




    private void saveBranchChanges() {
        String name = branchNameEditText.getText().toString().trim();
        String phone = branchPhoneNumberEditText.getText().toString().trim();
        String address = branchAddressEditText.getText().toString().trim();

        // Validation
        // ...

        Branch updatedBranch = new Branch(branchId, name, phone, address, workingTimes, servicesOffered);

        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.child(branchId).setValue(updatedBranch).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(EditBranchActivity.this, "Branch updated", Toast.LENGTH_LONG).show();
                    finish(); // Go back to the previous activity
                } else {
                    Toast.makeText(EditBranchActivity.this, "Failed to update branch", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void loadBranchDetails() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Branch branch = dataSnapshot.getValue(Branch.class);
                if (branch != null) {
                    branchNameEditText.setText(branch.getName());
                    branchPhoneNumberEditText.setText(branch.getPhoneNumber());
                    branchAddressEditText.setText(branch.getAddress());
                    workingTimes = branch.getWorkingHours();
                    servicesOffered = branch.getServiceOfferred();
                    updateDayButtonColors();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditBranchActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        loadBranchDetails(); // Refresh data when returning to this activity
    }
}
