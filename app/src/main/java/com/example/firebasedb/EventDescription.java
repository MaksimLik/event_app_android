package com.example.firebasedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class EventDescription extends AppCompatActivity {

    private File img_file;
    private TextInputEditText name_event, date_event;
    private Spinner first_spinner;
    private Spinner second_spinner;
    private ImageView review_image;
    private DatabaseReference databaseReference;
    private Events event;

    private double ax;
    private double ay;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginWindow.class);
            startActivity(intent);
            finish();
        }
        else {
            String appName = user.getEmail();
            setTitle(appName);
        }

        name_event = findViewById(R.id.nameEventField);
        first_spinner = findViewById(R.id.spinnerAge);
        second_spinner = findViewById(R.id.secondSpinner);
        date_event = findViewById(R.id.setEventData);
        FloatingActionButton add_image = findViewById(R.id.imageEventField);
        review_image = findViewById(R.id.imageEventField2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.rating_values, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //simple_spinner_dropdown_item
        first_spinner.setAdapter(adapter);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.rating_values2, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); //simple_spinner_dropdown_item
        second_spinner.setAdapter(adapter2);

        // Get the latitude and longtiude from the intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ax = extras.getDouble("ax");
            ay = extras.getDouble("ay");
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://myappmobile-ede7b-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("Events");

        // Add functionality to the add_photo_btn
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the camera permission is not granted
                if ((ContextCompat.checkSelfPermission(EventDescription.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(EventDescription.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
                    // Request the permission
                    ActivityCompat.requestPermissions(EventDescription.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    // Let the user take a photo or choose from the gallery
                    // Open the upload image activity
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, 1);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri imageUri = data.getData();

            String filePath = getPath(imageUri);
            String file_extn = filePath.substring(filePath.lastIndexOf(".") + 1);
            if (file_extn.equals("img") || file_extn.equals("jpg") || file_extn.equals("jpeg") || file_extn.equals("gif") || file_extn.equals("png")) {
                try {
                    img_file = new File(filePath);
                    // Update the activity with thumbnail of the image

                    if (img_file.exists()) {
                        // Update the activity with thumbnail of the image
                        review_image.setImageURI(imageUri);
                        review_image.setVisibility(View.VISIBLE);
                        Toast.makeText(EventDescription.this, "Image added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventDescription.this, "File does not exist", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(EventDescription.this, "Invalid file type. Supported are: jpg/gif/img/png", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private boolean getValues() {
        String title = Objects.requireNonNull(name_event.getText()).toString();
        String age = first_spinner.getSelectedItem().toString();
        String type = second_spinner.getSelectedItem().toString();
        String description = Objects.requireNonNull(date_event.getText()).toString();
        if (title.isEmpty() || age.isEmpty() || description.isEmpty() || img_file == null || type.isEmpty()) {
            return false;
        }
        // Convert image file to base64 string
        String img_file_base64 = encodeFileToBase64Binary(img_file);
        event = new Events();
        event.setEvent_title(title);
        event.setEvent_age(age);
        event.setEvent_description(description);
        event.setEvent_type(type);
        event.setEvent_image(img_file_base64);
        event.setEvent_ax(ax);
        event.setEvent_ay(ay);
        return true;
    }

    private String encodeFileToBase64Binary(File img_file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(img_file);
            byte[] bytes = new byte[(int)img_file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedfile;
    }

    public void button_submit(View view) {
       // Add the review to the database
        if (getValues()) {
            // Generate a random id for the review
            Random random = new Random();
            int id = random.nextInt(1000000);
            databaseReference.child(String.valueOf(id)).setValue(event);
            Toast.makeText(EventDescription.this, "Review added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(EventDescription.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), LoginWindow.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}