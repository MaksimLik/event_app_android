package com.example.firebasedb;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class AddReviewActivity extends AppCompatActivity {

    private File img_file;
    private TextInputEditText review_title, review_rating, review_description;
    private ImageView review_image;
    private DatabaseReference databaseReference;
    private Reviews review;

    private double lat;
    private double lng;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else {
            String appName = user.getEmail();
            setTitle(appName);
        }
        review_title = findViewById(R.id.idEdtReviewTitle);
        review_rating = findViewById(R.id.idEdtReviewRating);
        review_description = findViewById(R.id.idEdtReviewDescription);
        FloatingActionButton add_photo_btn = findViewById(R.id.idFABAddImage);
        review_image = findViewById(R.id.idIVReviewImage);

        // Get the latitude and longtiude from the intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            lat = extras.getDouble("lat");
            lng = extras.getDouble("lng");
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://myappmobile-ede7b-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("Reviews");

        // Add functionality to the add_photo_btn
        add_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If the camera permission is not granted
                if ((ContextCompat.checkSelfPermission(AddReviewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) ||
                        (ContextCompat.checkSelfPermission(AddReviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)){
                    // Request the permission
                    ActivityCompat.requestPermissions(AddReviewActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
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
                        Toast.makeText(AddReviewActivity.this, "Image added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AddReviewActivity.this, "File does not exist", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(AddReviewActivity.this, "Invalid file type. Supported are: jpg/gif/img/png", Toast.LENGTH_SHORT).show();
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

    private boolean getValues(){
        String title = Objects.requireNonNull(review_title.getText()).toString();
        String rating = Objects.requireNonNull(review_rating.getText()).toString();
        String description = Objects.requireNonNull(review_description.getText()).toString();
        if (title.isEmpty() || rating.isEmpty() || description.isEmpty() || img_file == null) {
            return false;
        }
        // Convert image file to base64 string
        String img_file_base64 = encodeFileToBase64Binary(img_file);
        review = new Reviews();
        review.setReview_title(title);
        review.setReview_rating(rating);
        review.setReview_description(description);
        review.setReview_image(img_file_base64);
        review.setReview_lat(lat);
        review.setReview_lng(lng);
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
            databaseReference.child(String.valueOf(id)).setValue(review);
            Toast.makeText(AddReviewActivity.this, "Review added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AddReviewActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}