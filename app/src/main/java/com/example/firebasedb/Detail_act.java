package com.example.firebasedb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Detail_act extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;

    TextView name_of_event;
    TextView description_of_event;
    TextView rating_of_event;
    FloatingActionButton repostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name_of_event = findViewById(R.id.name_event);
        description_of_event = findViewById(R.id.description_event);
        rating_of_event = findViewById(R.id.rating_event);
        repostButton = findViewById(R.id.repostButton);

        // Retrieve data from the intent extras sent by BarAdapter.java
        Intent intent = getIntent();
        String barName = intent.getStringExtra("BAR_NAME");
        String barDescription = intent.getStringExtra("BAR_DESCRIPTION");
        String barRating = intent.getStringExtra("BAR_RATING");

        // Set the TextViews with the data from BarAdapter.java
        name_of_event.setText(barName);
        description_of_event.setText(barDescription);
        rating_of_event.setText(barRating);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent1 = new Intent(getApplicationContext(), Login.class);
            startActivity(intent1);
            finish();
        }
        else {
            String appName = user.getEmail();
            setTitle(appName);
        }

        repostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventTitle = name_of_event.getText().toString();
                String eventDescription = description_of_event.getText().toString();

                // Создайте текстовое сообщение, которое будет отправлено в Facebook
                String message = "I recommend attending the following event: " + eventTitle + "\nDescription: " + eventDescription;

                // Создайте Intent для отправки сообщения в Facebook
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, message);

                // Проверяем, есть ли устройстве приложение Facebook
                PackageManager packageManager = getPackageManager();
                List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

                if (resolveInfoList.size() > 0) {
                    // Найдено приложение Facebook, запускаем Intent
                    startActivity(Intent.createChooser(intent, "Send via"));
                } else {
                    // Приложение Facebook не найдено, выводим сообщение об ошибке или предложение установить Facebook
                    Toast.makeText(Detail_act.this, "The app is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}