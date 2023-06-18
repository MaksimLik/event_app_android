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

public class EventDetail extends AppCompatActivity {
    FirebaseUser user;
    FirebaseAuth auth;

    TextView name_of_event;
    TextView description_of_event;
    TextView age_of_event;
    TextView type_of_event;

    FloatingActionButton repostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        name_of_event = findViewById(R.id.name_event);
        description_of_event = findViewById(R.id.description_event);
        age_of_event = findViewById(R.id.age_event);
        type_of_event = findViewById(R.id.event_type);
        repostButton = findViewById(R.id.repostButton);

        // Retrieve data from the intent extras sent by BarAdapter.java
        Intent intent = getIntent();
        String eventName = intent.getStringExtra("EVENT_NAME");
        String eventDescription = intent.getStringExtra("EVENT_DESCRIPTION");
        String eventAge = intent.getStringExtra("EVENT_AGE");
        String eventType = intent.getStringExtra("EVENT_TYPE"); //dodane

        // Set the TextViews with the data from BarAdapter.java
        name_of_event.setText(eventName);
        type_of_event.setText(eventType);
        description_of_event.setText(eventDescription);
        age_of_event.setText(eventAge);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent1 = new Intent(getApplicationContext(), LoginWindow.class);
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
                String eventAge = age_of_event.getText().toString();
                String eventType = type_of_event.getText().toString();

                // Создайте текстовое сообщение, которое будет отправлено в Facebook
                String message = "I recommend attending the following event: " + eventTitle + "\nWho is this event for? " + eventAge +
                    "\nType of event: " + eventType +  "\nDescription: " + eventDescription;

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
                    Toast.makeText(EventDetail.this, "The app is not installed in your phone", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}