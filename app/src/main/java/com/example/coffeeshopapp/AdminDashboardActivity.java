package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button buttonAddCoffeeShop, buttonAddEmployee;
    private Button buttonManageMenuItems, buttonPendingOrders, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        buttonAddCoffeeShop = findViewById(R.id.buttonAddCoffeeShop);
        buttonManageMenuItems = findViewById(R.id.buttonManageMenuItems);
        buttonPendingOrders = findViewById(R.id.buttonPendingOrders);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonAddEmployee= findViewById(R.id.buttonAddEmployee);

        // Setup the button to open the AddCoffeeShopActivity
        buttonAddCoffeeShop.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddCoffeeShopActivity.class);
            startActivity(intent);
        });

        // Setup the button to open the AddEmployeeActivity
        buttonAddEmployee.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        // Setup the button to open the ManageMenuItemsActivity
        buttonManageMenuItems.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, ManageMenuItemsActivity.class);
            startActivity(intent);
        });

        buttonPendingOrders.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, PendingOrdersActivity.class);
            startActivity(intent);
        });
        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
