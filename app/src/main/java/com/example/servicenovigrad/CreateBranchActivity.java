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

public class CreateBranchActivity extends AppCompatActivity {

    private EditText branchNameEditText, branchPhoneNumberEditText, branchAddressEditText;
    private Map<String, String> workingTimes = new HashMap<>();
    private List<Service> servicesOffered = new ArrayList<>();
    private Map<Integer, Button> dayButtons = new HashMap<>();
    private static final int SELECT_SERVICES_REQUEST=1;
    private String branchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_branch);

        branchNameEditText = findViewById(R.id.branchNameEditText);
        branchPhoneNumberEditText = findViewById(R.id.branchPhoneNumberEditText);
        branchAddressEditText = findViewById(R.id.branchAddressEditText);
        Button buttonCreate = findViewById(R.id.buttonCreate);
        Button selectOfferedServices=findViewById(R.id.buttonServicesOffered);


        selectOfferedServices.setOnClickListener(view -> {
            if (branchId == null) {
                branchId = FirebaseDatabase.getInstance().getReference("branches").push().getKey();
            }

            Intent intent = new Intent(CreateBranchActivity.this, SelectOfferedServicesActivity.class);
            intent.putExtra("BRANCH_ID", branchId);
            startActivityForResult(intent, SELECT_SERVICES_REQUEST);
        });

        setupDayButtons(); // A method to set up click listeners for day buttons

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBranch();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_SERVICES_REQUEST && resultCode == RESULT_OK && data != null) {
            // Retrieve the list of selected service IDs from the result
            ArrayList<String> selectedServiceIds = data.getStringArrayListExtra("selectedServiceIds");
            // Update the servicesOffered list with these IDs
            updateServicesOffered(selectedServiceIds);
        }
    }
    private void updateServicesOffered(ArrayList<String> selectedServiceIds) {
        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("services");

        // Clear the old list to prevent duplication
        servicesOffered.clear();

        for (String serviceId : selectedServiceIds) {
            servicesRef.child(serviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Service service = dataSnapshot.getValue(Service.class);
                    if (service != null) {
                        servicesOffered.add(service);
                        // Call a method to update the UI if needed
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(CreateBranchActivity.this, "Failed to load service details.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupDayButtons() {
        int[] dayButtonIds = {
                R.id.buttonMonday, R.id.buttonTuesday, R.id.buttonWednesday,
                R.id.buttonThursday, R.id.buttonFriday, R.id.buttonSaturday, R.id.buttonSunday
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
    private void updateDayButtonColors() {
        for (Map.Entry<Integer, Button> entry : dayButtons.entrySet()) {
            Button dayButton = entry.getValue();
            // Retrieve the single-letter day abbreviation based on button ID
            String dayKey = getResources().getResourceEntryName(entry.getKey())
                    .replace("button", "");

            String hours = workingTimes.getOrDefault(dayKey, "Closed");
            if (!hours.equals("Closed")&&!hours.equals("")) {
                dayButton.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                dayButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
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

    private void createBranch() {
        String name = branchNameEditText.getText().toString().trim();
        String phone = branchPhoneNumberEditText.getText().toString().trim();
        String address = branchAddressEditText.getText().toString().trim();

        // Simple validation
        if (name.isEmpty()) {
            branchNameEditText.setError("Service name required");
            branchNameEditText.requestFocus();
            return;
        }


        if (branchId == null) {
            Toast.makeText(this, "Branch ID not found. Please select services first.", Toast.LENGTH_LONG).show();
            return;
        }

        Branch newBranch = new Branch(branchId, name, phone, address, workingTimes, servicesOffered);
        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.child(branchId).setValue(newBranch).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CreateBranchActivity.this, "Branch created", Toast.LENGTH_LONG).show();
                    // Redirect back to the MainPageAdmin
                    Intent intent = new Intent(CreateBranchActivity.this, BranchPageActivity.class);
                    intent.putExtra("BRANCH_ID", branchId);
                    startActivity(intent);
                } else {
                    Toast.makeText(CreateBranchActivity.this, "Failed to create branch", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}