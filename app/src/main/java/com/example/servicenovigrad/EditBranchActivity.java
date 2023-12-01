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
    private String branchId;
    private DatabaseReference branchesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_branch);

        branchNameEditText = findViewById(R.id.editTextBranchName);
        branchPhoneNumberEditText = findViewById(R.id.branchPhoneNumberText2);
        branchAddressEditText = findViewById(R.id.branchAddressText2);
        Button saveChangesButton = findViewById(R.id.buttonSaveChanges);
        Button editOfferedServicesButton = findViewById(R.id.editOfferedServices);


        branchId = getIntent().getStringExtra("BRANCH_ID");
        branchesRef = FirebaseDatabase.getInstance().getReference("branches");

        loadBranchData();

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateBranch();

            }
        });

        editOfferedServicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditBranchActivity.this, EditOfferedServicesActivity.class);
                intent.putExtra("BRANCH_ID", branchId);
                startActivity(intent);
            }
        });

        setupDayButtons();
    }

    private void loadBranchData() {
        branchesRef.child(branchId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Branch branch = dataSnapshot.getValue(Branch.class);
                if (branch != null) {
                    branchNameEditText.setText(branch.getName());
                    branchPhoneNumberEditText.setText(branch.getPhoneNumber());
                    branchAddressEditText.setText(branch.getAddress());
                    workingTimes.putAll(branch.getWorkingHours());
                    updateDayButtonColors();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditBranchActivity.this, "Failed to load branch details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBranch() {
        String name = branchNameEditText.getText().toString().trim();
        String phone = branchPhoneNumberEditText.getText().toString().trim();
        String address = branchAddressEditText.getText().toString().trim();

        if (name.isEmpty()) {
            branchNameEditText.setError("Name is required");
            branchNameEditText.requestFocus();
            return;
        }

        branchesRef.child(branchId).child("servicesOffered").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Service> currentServicesOffered = new ArrayList<>();
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    Service service = serviceSnapshot.getValue(Service.class);
                    currentServicesOffered.add(service);
                }


                Branch updatedBranch = new Branch(branchId, name, phone, address, workingTimes, currentServicesOffered);
                Map<String, Object> branchUpdates = new HashMap<>();
                branchUpdates.put("name", updatedBranch.getName());
                branchUpdates.put("phone", updatedBranch.getPhoneNumber());
                branchUpdates.put("address", updatedBranch.getAddress());
                branchUpdates.put("workingTimes", updatedBranch.getWorkingHours());


                branchesRef.child(branchId).updateChildren(branchUpdates).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditBranchActivity.this, "Branch updated", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditBranchActivity.this, BranchPageActivity.class);
                        intent.putExtra("BRANCH_ID", branchId); // if you need to pass the ID
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(EditBranchActivity.this, "Failed to update branch", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EditBranchActivity.this, "Failed to fetch current services", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupDayButtons() {
        int[] dayButtonIds = {
                R.id.buttonMonday2, R.id.buttonTuesday2, R.id.buttonWednesday2,
                R.id.buttonThursday2, R.id.buttonFriday2, R.id.buttonSaturday2, R.id.buttonSunday2
        };
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < dayButtonIds.length; i++) {
            Button dayButton = findViewById(dayButtonIds[i]);
            final String day = days[i];
            dayButton.setOnClickListener(v -> showWorkingHoursDialog(day));
        }
    }

    private void showWorkingHoursDialog(final String dayKey) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_working_hours);

        final EditText editTextWorkingHours = dialog.findViewById(R.id.editTextWorkingHours);
        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonOk = dialog.findViewById(R.id.buttonOk);

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
        int[] dayButtonIds = {
                R.id.buttonMonday2, R.id.buttonTuesday2, R.id.buttonWednesday2,
                R.id.buttonThursday2, R.id.buttonFriday2, R.id.buttonSaturday2, R.id.buttonSunday2
        };

        for (int buttonId : dayButtonIds) {
            Button dayButton = findViewById(buttonId);
            String dayKey = getResources().getResourceEntryName(buttonId).replace("button", "").replace("2", "");
            String hours = workingTimes.getOrDefault(dayKey, "Closed");

            if (!hours.equals("Closed") && !hours.isEmpty()) {
                dayButton.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                dayButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }
    }
}
