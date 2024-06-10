package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ManageMenuItemsActivity extends AppCompatActivity {

    private Spinner spinnerCoffeeShops;
    private EditText editTextMenuItemName;
    private EditText editTextMenuItemDescription;
    private EditText editTextMenuItemPrice;
    private Button buttonAddMenuItem, buttonHome;
    private TextView textViewMessage;
    private FirebaseFirestore db;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> coffeeShopNames = new ArrayList<>();
    private ArrayList<String> coffeeShopIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_menu_items);

        db = FirebaseFirestore.getInstance();

        spinnerCoffeeShops = findViewById(R.id.spinnerCoffeeShops);
        editTextMenuItemName = findViewById(R.id.editTextMenuItemName);
        editTextMenuItemDescription = findViewById(R.id.editTextMenuItemDescription);
        editTextMenuItemPrice = findViewById(R.id.editTextMenuItemPrice);
        buttonAddMenuItem = findViewById(R.id.buttonAddMenuItem);
        textViewMessage = findViewById(R.id.textViewMessage);
        buttonHome = findViewById(R.id.buttonBackToHome);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, coffeeShopNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCoffeeShops.setAdapter(adapter);

        fetchCoffeeShops();

        buttonAddMenuItem.setOnClickListener(v -> addMenuItem());

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageMenuItemsActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchCoffeeShops() {
        db.collection("coffeeShops").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    coffeeShopNames.add(document.getString("name"));
                    coffeeShopIds.add(document.getId());
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.w("ManageMenuItemsActivity", "Error getting documents.", task.getException());
            }
        });
    }

    private boolean validateForm(String menuItemName, String menuItemDescription, String menuItemPrice) {
        boolean valid = true;

        if (TextUtils.isEmpty(menuItemName)) {
            editTextMenuItemName.setError("Required.");
            valid = false;
        } else {
            editTextMenuItemName.setError(null);
        }

        if (TextUtils.isEmpty(menuItemDescription)) {
            editTextMenuItemDescription.setError("Required.");
            valid = false;
        } else {
            editTextMenuItemDescription.setError(null);
        }

        if (TextUtils.isEmpty(menuItemPrice)) {
            editTextMenuItemPrice.setError("Required.");
            valid = false;
        } else {
            editTextMenuItemPrice.setError(null);
        }

        return valid;
    }

    private void addMenuItem() {
        String coffeeShopName = spinnerCoffeeShops.getSelectedItem().toString();
        String coffeeShopId = coffeeShopIds.get(spinnerCoffeeShops.getSelectedItemPosition());
        String menuItemName = editTextMenuItemName.getText().toString().trim();
        String menuItemDescription = editTextMenuItemDescription.getText().toString().trim();
        String menuItemPrice = editTextMenuItemPrice.getText().toString().trim();

        // Input validation
        if (!validateForm(menuItemName, menuItemDescription, menuItemPrice)) {
            return;
        }

        // Create a new menu item object
        Map<String, Object> menuItemMap = new HashMap<>();
        menuItemMap.put("coffeeShopId", coffeeShopId);
        menuItemMap.put("name", menuItemName);
        menuItemMap.put("description", menuItemDescription);
        menuItemMap.put("price", Double.parseDouble(menuItemPrice));

        // Add a new document with a generated ID
        db.collection("menuItems")
                .add(menuItemMap)
                .addOnSuccessListener(documentReference -> {
                    textViewMessage.setText("Menu Item added successfully!");
                    textViewMessage.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                    // Clear the fields
                    editTextMenuItemName.setText("");
                    editTextMenuItemDescription.setText("");
                    editTextMenuItemPrice.setText("");
                })
                .addOnFailureListener(e -> {
                    textViewMessage.setText("Failed to add Menu Item.");
                    textViewMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                });
    }
}
