package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddEmployeeActivity extends AppCompatActivity {


    private EditText editTextName, editTextAddress, editTextPhone, editTextEmail, editTextPassword;
    private Button btnAddEmployee;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmailRegister);
        editTextPassword = findViewById(R.id.editTextPasswordRegister);
        btnAddEmployee = findViewById(R.id.btnAddEmployee);


        btnAddEmployee.setOnClickListener(v -> addEmployee());

    }


    private boolean validateForm(String name, String address, String phone, String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Required.");
            valid = false;
        } else {
            editTextName.setError(null);
        }

        if (TextUtils.isEmpty(address)) {
            editTextAddress.setError("Required.");
            valid = false;
        } else {
            editTextAddress.setError(null);
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Required.");
            valid = false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            editTextPhone.setError("Please enter a valid phone number.");
            valid = false;
        } else {
            editTextPhone.setError(null);
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email address.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            valid = false;
        } else if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters long.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }


    private void addEmployee() {
        final String name = editTextName.getText().toString().trim();
        final String address = editTextAddress.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Input validation
        if (!validateForm(name, address, phone, email, password)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration in Firebase Authentication was successful
                        Log.d("AddEmployeeActivity", "Authentication was successful");

                        // Now, let's save additional user details in Firestore "employees" collection
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("name", name);
                        userMap.put("address", address);
                        userMap.put("phone", phone);
                        userMap.put("email", email);


                        db.collection("employees").document(task.getResult().getUser().getUid())
                                .set(userMap)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(AddEmployeeActivity.this, "Registration done!", Toast.LENGTH_SHORT).show();
                                    Log.d("AddEmployeeActivity", "Employee details saved in Firestore!");
                                    Intent intent = new Intent(AddEmployeeActivity.this, UserTypeActivity.class);
                                    startActivity(intent);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(AddEmployeeActivity.this, "Failed to save employee details in Firestore.", Toast.LENGTH_SHORT).show();
                                    Log.w("AddEmployeeActivity", "Failed to save user details in Firestore.", e);
                                });
                    } else {
                        Toast.makeText(AddEmployeeActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                        Log.w("AddEmployeeActivity", "Authentication failed!", task.getException());
                    }
                });
    }
}