package com.example.servicenovigrad;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ClientBranchPageActivity extends AppCompatActivity {
    private TextView branchNameTextView, branchPhoneNumberTextView, branchAddressTextView;
    private String branchId;
    private Map<String, String> workingTimes = new HashMap<>();
    private Map<Integer, Button> dayButtons = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_branch_page);

        // Initialize TextViews and other views
        branchNameTextView = findViewById(R.id.ClientbranchNameText);
        branchPhoneNumberTextView = findViewById(R.id.ClientbranchPhoneNumberText);
        branchAddressTextView = findViewById(R.id.ClientbranchAddressText);
        Button selectedServices = findViewById(R.id.ClientbuttonOfferedServices);

        branchId = getIntent().getStringExtra("BRANCH_ID");

        setupDayButtons();

        selectedServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference branchesRef = database.getReference("branches");
                Intent intent = new Intent(ClientBranchPageActivity.this, OfferedServicesActivity.class);
                intent.putExtra("BRANCH_ID", branchId);
                intent.putExtra("BRANCH_NAME", branchNameTextView.getText().toString().trim());
                startActivity(intent);
            }
        });

        loadBranchDetails();
    }

    private void setupDayButtons() {
        int[] dayButtonIds = {
                R.id.ClientbuttonMonday, R.id.ClientbuttonTuesday, R.id.ClientbuttonWednesday,
                R.id.ClientbuttonThursday, R.id.ClientbuttonFriday, R.id.ClientbuttonSaturday, R.id.ClientbuttonSunday
        };
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        for (int i = 0; i < dayButtonIds.length; i++) {
            Button dayButton = findViewById(dayButtonIds[i]);
            dayButtons.put(dayButtonIds[i], dayButton);
            final String day = days[i];
            dayButton.setOnClickListener(v -> showViewWorkingTimesDialog(day));
        }
    }

    private void updateDayButtonColors() {
        for (Map.Entry<Integer, Button> entry : dayButtons.entrySet()) {
            Button dayButton = entry.getValue();
            String dayKey = getResources().getResourceEntryName(entry.getKey())
                    .replace("Clientbutton", "");
            String hours = workingTimes.getOrDefault(dayKey, "Closed");
            if (!hours.equals("Closed") && !hours.equals("")) {
                dayButton.setBackgroundColor(getResources().getColor(R.color.green));
            } else {
                dayButton.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }
    }

    private void showViewWorkingTimesDialog(final String dayKey) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.view_working_times_dialog);

        TextView textViewWorkingHours = dialog.findViewById(R.id.textViewWorkingHours);
        TextView dayText = dialog.findViewById(R.id.textView7);
        dayText.setText(dayKey);
        Button buttonClose = dialog.findViewById(R.id.buttonClose);

        String hours = workingTimes.getOrDefault(dayKey, "Closed");
        textViewWorkingHours.setText(hours);

        buttonClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void loadBranchDetails() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("branches").child(branchId);
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Branch branch = dataSnapshot.getValue(Branch.class);
                if (branch != null) {
                    branchNameTextView.setText(branch.getName());
                    branchPhoneNumberTextView.setText(branch.getPhoneNumber());
                    branchAddressTextView.setText(branch.getAddress());
                    workingTimes = branch.getWorkingHours();
                    updateDayButtonColors();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ClientBranchPageActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBranchDetails();
    }
}
