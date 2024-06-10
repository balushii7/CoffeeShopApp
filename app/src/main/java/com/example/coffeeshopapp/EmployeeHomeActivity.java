package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmployeeHomeActivity extends AppCompatActivity {

    private Button buttonManageMenuItems, buttonPendingOrders, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_home);

        buttonPendingOrders = findViewById(R.id.buttonPendingOrders);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonPendingOrders.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, PendingOrdersActivity.class);
            startActivity(intent);
        });
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeHomeActivity.this, MainActivity.class);
            startActivity(intent);
        });


    }
}