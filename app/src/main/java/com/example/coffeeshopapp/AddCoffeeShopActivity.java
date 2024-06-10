package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCoffeeShopActivity extends AppCompatActivity {

    private EditText editTextCoffeeShopName;
    private EditText editTextCoffeeShopAddress;
    private EditText editTextCoffeeShopDescription;
    private Button buttonAddCoffeeShop, backHome;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coffee_shop);

        db = FirebaseFirestore.getInstance();

        editTextCoffeeShopName = findViewById(R.id.editTextCoffeeShopName);
        editTextCoffeeShopAddress = findViewById(R.id.editTextCoffeeShopAddress);
        editTextCoffeeShopDescription = findViewById(R.id.editTextCoffeeShopDescription);
        buttonAddCoffeeShop = findViewById(R.id.buttonAddCoffeeShop);
        backHome = findViewById(R.id.buttonBackToHome);

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddCoffeeShopActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            }
        });

        buttonAddCoffeeShop.setOnClickListener(v -> addCoffeeShop());
    }

    private boolean validateForm(String name, String address, String description) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            editTextCoffeeShopName.setError("Required.");
            valid = false;
        } else {
            editTextCoffeeShopName.setError(null);
        }

        if (TextUtils.isEmpty(address)) {
            editTextCoffeeShopAddress.setError("Required.");
            valid = false;
        } else {
            editTextCoffeeShopAddress.setError(null);
        }

        if (TextUtils.isEmpty(description)) {
            editTextCoffeeShopDescription.setError("Required.");
            valid = false;
        } else {
            editTextCoffeeShopDescription.setError(null);
        }

        return valid;
    }

    private void addCoffeeShop() {
        final String name = editTextCoffeeShopName.getText().toString().trim();
        final String address = editTextCoffeeShopAddress.getText().toString().trim();
        final String description = editTextCoffeeShopDescription.getText().toString().trim();

        // Input validation
        if (!validateForm(name, address, description)) {
            return;
        }

        // Create a new coffee shop object
        Map<String, Object> coffeeShopMap = new HashMap<>();
        coffeeShopMap.put("name", name);
        coffeeShopMap.put("address", address);
        coffeeShopMap.put("description", description);

        // Add a new document with a generated ID
        db.collection("coffeeShops")
                .add(coffeeShopMap)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AddCoffeeShopActivity.this, "Coffee Shop added successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate back to the dashboard or clear the fields
                    editTextCoffeeShopName.setText("");
                    editTextCoffeeShopAddress.setText("");
                    editTextCoffeeShopDescription.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddCoffeeShopActivity.this, "Failed to add Coffee Shop.", Toast.LENGTH_SHORT).show();
                });
    }
}
