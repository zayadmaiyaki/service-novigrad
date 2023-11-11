package com.example.servicenovigrad;

import androidx.appcompat.app.AppCompatActivity;
import com.example.servicenovigrad.Service;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainPageAdmin extends AppCompatActivity {

    private Spinner spinner;
    private ArrayList<String> services;
    private ArrayAdapter<String> adapter;
    ListView listViewServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page_admin);

        Button createServiceButton = findViewById(R.id.createServiceButton);

        // Initialize your array list and adapter
        services = new ArrayList<>();

        createServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPageAdmin.this, CreateServiceActivity.class);
                startActivity(intent);
            }
        });
   }
}