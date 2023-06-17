package com.example.firebasedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Detail_act extends AppCompatActivity {
    TextView name_of_event;
    TextView description_of_event;
    TextView rating_of_event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name_of_event = findViewById(R.id.name_event);
        description_of_event = findViewById(R.id.description_event);
        rating_of_event = findViewById(R.id.rating_event);
        // Retrieve data from the intent extras sent by BarAdapter.java
        Intent intent = getIntent();
        String barName = intent.getStringExtra("BAR_NAME");
        String barDescription = intent.getStringExtra("BAR_DESCRIPTION");
        String barRating = intent.getStringExtra("BAR_RATING");

        // Set the TextViews with the data from BarAdapter.java
        name_of_event.setText(barName);
        description_of_event.setText(barDescription);
        rating_of_event.setText(barRating);
    }
}