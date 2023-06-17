package com.example.firebasedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    LinearLayout noInternet;
    Button retryButton;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        noInternet = findViewById(R.id.no_internet_layout);
        retryButton = findViewById(R.id.retry_button);
        user = auth.getCurrentUser();

        if (!NetworkAdapter.isNetworkAvailable(this)) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            noInternet.setVisibility(View.VISIBLE);
        }

        Fragment fragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }
    public void retryInternetConnectionButton(View view) {
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NetworkAdapter.isNetworkAvailable(MainActivity.this)) {
                    noInternet.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
