package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.messaging.FirebaseMessaging;

public class UserTypeActivity extends AppCompatActivity {

    private Button btnCustomer, btnAdmin, btnEmployee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_type);

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("new_user_forums");

        btnCustomer = findViewById(R.id.btnCustomer);
        btnEmployee = findViewById(R.id.btnEmployee);
        btnAdmin = findViewById(R.id.btnAdmin);

        btnCustomer.setOnClickListener(v -> {
            // Navigate to the RegisterActivity
            Intent intent = new Intent(UserTypeActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAdmin.setOnClickListener(v -> {
            // Navigate to the RegisterActivity
            Intent intent = new Intent(UserTypeActivity.this, AdminLogin.class);
            startActivity(intent);
        });

        btnEmployee.setOnClickListener(v -> {
            // Navigate to the RegisterActivity
            Intent intent = new Intent(UserTypeActivity.this, EmployeeLogin.class);
            startActivity(intent);
        });

    }
}