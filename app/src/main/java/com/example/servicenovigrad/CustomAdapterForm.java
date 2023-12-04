package com.example.servicenovigrad;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class CustomAdapterForm extends ArrayAdapter<String> {
        private Map<String, String> filledItems;

        public CustomAdapterForm(Context context, List<String> items, Map<String, String> filledItems) {
            super(context, 0, items);
            this.filledItems = filledItems;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String item = getItem(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_request_for_service, parent, false);
            }

            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.formStatus);

            // Populate the data into the template view using the data object
            tvName.setText(item);

            // If the item has been filled, change the text color to green
            if (filledItems.containsKey(item)) {
                tvName.setTextColor(Color.GREEN);
            } else {
                tvName.setTextColor(Color.RED);  // Change the color back to black for unfilled items
            }

            // Return the completed view to render on screen
            return convertView;
        }

}
