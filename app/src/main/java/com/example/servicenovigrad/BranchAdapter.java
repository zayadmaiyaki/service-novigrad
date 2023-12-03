package com.example.servicenovigrad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchViewHolder> {

    private List<Branch> branchList;

    public static class BranchViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewBranchName;
        public TextView textViewBranchServices;

        public BranchViewHolder(View itemView) {
            super(itemView);
            textViewBranchName = itemView.findViewById(R.id.search_branch_name);
            textViewBranchServices = itemView.findViewById(R.id.offered_services);
        }
    }

    public BranchAdapter(List<Branch> branchList) {
        this.branchList = branchList;
    }

    @NonNull
    @Override
    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_new_request_page, parent, false);
        return new BranchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchViewHolder holder, int position) {
        Branch currentBranch = branchList.get(position);
        holder.textViewBranchName.setText(currentBranch.getName());

        // Concatenate names of services offered for display
        StringBuilder services = new StringBuilder();
        for (Service service : currentBranch.getServiceOfferred()) {
            if (services.length() > 0) services.append(", ");
            services.append(service.getName());
        }
        holder.textViewBranchServices.setText(services.toString());
    }

    @Override
    public int getItemCount() {
        return branchList.size();
    }

    // Method to update the dataset inside the adapter
    public void setBranchList(List<Branch> branchList) {
        this.branchList = branchList;
        notifyDataSetChanged();
    }
}

