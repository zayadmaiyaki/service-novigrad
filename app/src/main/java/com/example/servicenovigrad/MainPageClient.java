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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainPageClient extends AppCompatActivity {

    private TextView textClientPage;
    private TextView textView;
    private ListView ongoingRequestsListView;
    private Button newRequestButton;

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

        String[] ongoingRequests = {"Request 1", "Request 2", "Request 3"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, ongoingRequests);
        ongoingRequestsListView.setAdapter(adapter);

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
                showLongClickDialog(ongoingRequests[position]);
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
