package com.example.coffeeshopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button buttonViewProfile;
    private Button buttonLogout, buttonViewShops, butonViewOrders, buttnTableBookings, buttonChatbot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        buttonViewProfile = findViewById(R.id.buttonViewProfile);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonViewShops = findViewById(R.id.buttonViewShop);
        butonViewOrders = findViewById(R.id.buttonViewOrders);
        buttnTableBookings = findViewById(R.id.buttonTableBookings);
        buttonChatbot = findViewById(R.id.buttonChatbot);


        buttonViewProfile.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        buttonLogout.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonViewShops.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, ViewShopsActivity.class);
            startActivity(intent);
        });

        butonViewOrders.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, ViewOrdersActivity.class);
            startActivity(intent);
        });
        buttnTableBookings.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, TableBookingsActivity.class);
            startActivity(intent);
        });

         buttonChatbot.setOnClickListener(v -> {

            Intent intent = new Intent(HomeActivity.this, UserChatActivity.class);
            startActivity(intent);
        });


    }
}