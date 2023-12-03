package com.example.servicenovigrad;

import android.app.Dialog;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class NewRequestPageActivity extends AppCompatActivity {

    private String locationToSearch;

    private String serviceNeeded;

    private String branchNameToSearch;
    private EditText searchBranchNameEditText, locationInputEditText, offeredServicesEditText;
    private ListView listViewBranches;
    private Button applyFilterButton;
    private ArrayAdapter<String> adapter;
    private List<String> branchNames;
    private List<String> branchIds;
    private List<Branch> originalBranchesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_page);

        initializeUI();
        loadBranches();
    }

    private void initializeUI() {
        // Initialize UI components
        searchBranchNameEditText = findViewById(R.id.search_branch_name);
        locationInputEditText = findViewById(R.id.location_input);
        offeredServicesEditText = findViewById(R.id.offered_services);
        listViewBranches = findViewById(R.id.branches_list_view);
        applyFilterButton = findViewById(R.id.button_filter);

        // Setup ListView and its adapter
        branchNames = new ArrayList<>();
        branchIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, branchNames);
        listViewBranches.setAdapter(adapter);

        // Setup listeners
        applyFilterButton.setOnClickListener(view -> applyFilter());
        listViewBranches.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBranchId = branchIds.get(position);
            Intent intent = new Intent(NewRequestPageActivity.this, ClientBranchPageActivity.class);
            intent.putExtra("BRANCH_ID", selectedBranchId);
            startActivity(intent);
        });

        Button buttonSearchByWorkingTime = findViewById(R.id.button_working_times);
        buttonSearchByWorkingTime.setOnClickListener(view -> client_hours_search());

    }

    private void loadBranches() {
        // Load branches from Firebase
        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference("branches");
        branchesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                originalBranchesList.clear();
                branchNames.clear();
                branchIds.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Branch branch = snapshot.getValue(Branch.class);
                    if (branch != null) {
                        originalBranchesList.add(branch);
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


    private void client_hours_search() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.client_hours_search_dialog);

        final EditText clientSearchWorkingHours = dialog.findViewById(R.id.ClientSearchWorkingHours);
        Button buttonCancel = dialog.findViewById(R.id.ClientbuttonCancel);
        Button buttonOk = dialog.findViewById(R.id.ClientbuttonOk);

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonOk.setOnClickListener(v -> {
            String timeSearched = clientSearchWorkingHours.getText().toString();
            filterAndSortBranchesByTime(timeSearched);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applyFilter() {
        branchNameToSearch = searchBranchNameEditText.getText().toString();
        serviceNeeded = offeredServicesEditText.getText().toString();
        locationToSearch = locationInputEditText.getText().toString();

        boolean isNameFilterActive = !branchNameToSearch.isEmpty();
        boolean isServiceFilterActive = !serviceNeeded.isEmpty();
        boolean isLocationFilterActive = !locationToSearch.isEmpty();

        if (!isNameFilterActive && !isServiceFilterActive && !isLocationFilterActive) {
            Toast.makeText(this, "Please enter at least one filter criterion", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Branch> filteredBranches = new ArrayList<>(originalBranchesList);

        if (isNameFilterActive) {
            filteredBranches = filterBranchesByName(filteredBranches, branchNameToSearch);
        }
        if (isServiceFilterActive) {
            filteredBranches = filterBranchesByServices(filteredBranches, serviceNeeded);
        }
        if (isLocationFilterActive) {
            filteredBranches = filterBranchesByLocation(filteredBranches, locationToSearch);
        }

        updateBranchListView(filteredBranches);
    }

    private List<Branch> filterBranchesByName(List<Branch> branches, String name) {
        // Implement filtering logic based on branch name
        List<Branch> filteredList = new ArrayList<>();
        for (Branch branch : branches) {
            if (branch.getName().equalsIgnoreCase(name)) {
                filteredList.add(branch);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No branches found with the name " + name, Toast.LENGTH_SHORT).show();
        }
        return filteredList;
    }

    /*private List<Branch> filterBranchesByServices(List<Branch> branches, String serviceNeeded) {
        // Implement filtering logic based on service
        List<Branch> filteredList = new ArrayList<>();
        for (Branch branch : branches) {
            for (Service service : branch.getServiceOfferred()) {
                if (service.getName().toLowerCase().contains(serviceNeeded.toLowerCase())) {
                    filteredList.add(branch);
                    break;
                }
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No branches found that offer this service.", Toast.LENGTH_SHORT).show();
        }
        return filteredList;
    }*/

    private List<Branch> filterBranchesByServices(List<Branch> branches, String serviceNeeded) {
        String normalizedServiceNeeded = normalizeString(serviceNeeded);
        List<Branch> filteredList = new ArrayList<>();
        for (Branch branch : branches) {
            List<Service> servicesOffered = branch.getServiceOfferred();
            if (servicesOffered != null && !servicesOffered.isEmpty()) {
                for (Service service : servicesOffered) {
                    String normalizedServiceName = normalizeString(service.getName());
                    if (normalizedServiceName.contains(normalizedServiceNeeded)) {
                        filteredList.add(branch);
                        break;
                    }
                }
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No branches found that offer the service " + serviceNeeded, Toast.LENGTH_SHORT).show();
        }
        return filteredList;
    }

    private String normalizeString(String input) {
        return input.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
    }


    private List<Branch> filterBranchesByLocation(List<Branch> branches, String location) {
        // Implement filtering logic based on location
        List<Branch> filteredList = new ArrayList<>();
        for (Branch branch : branches) {
            if (branch.getAddress().equalsIgnoreCase(location)) {
                filteredList.add(branch);
            }
        }
        return filteredList;
    }

    private void updateBranchListView(List<Branch> branches) {
        branchNames.clear();
        branchIds.clear();

        for (Branch branch : branches) {
            branchNames.add(branch.getName());
            branchIds.add(branch.getId());
        }

        adapter.notifyDataSetChanged();
    }



    private void searchAndSortBranchesByName(String branchNameToSearch) {
        Branch foundBranch = null;
        List<Branch> otherBranches = new ArrayList<>();

        for (Branch branch : originalBranchesList) {
            if (branch.getName().equalsIgnoreCase(branchNameToSearch)) {
                foundBranch = branch;
            } else {
                otherBranches.add(branch);
            }
        }

        updateBranchListViewByTimeByName(foundBranch, otherBranches);
    }


    private void updateBranchListViewByTimeByName(Branch foundBranch, List<Branch> otherBranches) {
        branchNames.clear();
        branchIds.clear();

        if (foundBranch != null) {
            branchNames.add(foundBranch.getName());
            branchIds.add(foundBranch.getId());
        }

        for (Branch branch : otherBranches) {
            branchNames.add(branch.getName());
            branchIds.add(branch.getId());
        }

        adapter.notifyDataSetChanged();

        if (foundBranch == null) {
            Toast.makeText(this, "No branch found with the name " + branchNameToSearch, Toast.LENGTH_SHORT).show();
        }
    }

    private void filterAndSortBranchesByTime(String timeSearched) {
        // Split the input into day and hour
        String[] parts = timeSearched.split(" ");
        if (parts.length != 2) {
            Toast.makeText(this, "Invalid time format", Toast.LENGTH_LONG).show();
            return;
        }
        String day = parts[0];
        String inputHour = convertTo24HourFormat(parts[1]);

        // Filter and sort branches
        List<Branch> matchingBranches = new ArrayList<>();
        List<Branch> otherBranches = new ArrayList<>();
        for (Branch branch : originalBranchesList) {
            String workingTime = branch.getWorkingHours().get(day);
            if (workingTime != null && isTimeInRange(inputHour, workingTime)) {
                matchingBranches.add(branch);
            } else {
                otherBranches.add(branch);
            }
        }

        // Update ListView
        updateBranchListViewByTime(matchingBranches, otherBranches);
    }

    private boolean isTimeInRange(String inputHour, String workingTime) {
        // Check if time is in range
        String[] times = workingTime.split("-");
        if (times.length != 2) return false;

        String start = convertTo24HourFormat(times[0]);
        String end = convertTo24HourFormat(times[1]);
        return inputHour.compareTo(start) >= 0 && inputHour.compareTo(end) <= 0;
    }

    private String convertTo24HourFormat(String time) {
        // Convert time to 24-hour format
        try {
            DateFormat twelveHourFormat = new SimpleDateFormat("hh a", Locale.ENGLISH);
            DateFormat twentyFourHourFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            Date date = twelveHourFormat.parse(time);
            return twentyFourHourFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void updateBranchListViewByTime(List<Branch> matchingBranches, List<Branch> otherBranches) {
        // Update ListView with filtered branches
        branchNames.clear();
        branchIds.clear();
        if (matchingBranches.isEmpty()) {
            Toast.makeText(this, "No opened branches found at this time.", Toast.LENGTH_SHORT).show();
            for (Branch branch : otherBranches) {
                branchNames.add(branch.getName());
                branchIds.add(branch.getId());
            }
        }else {

            for (Branch branch : matchingBranches) {
                branchNames.add(branch.getName());
                branchIds.add(branch.getId());
            }
            for (Branch branch : otherBranches) {
                branchNames.add(branch.getName());
                branchIds.add(branch.getId());
            }
        }

        adapter.notifyDataSetChanged();
    }
}
