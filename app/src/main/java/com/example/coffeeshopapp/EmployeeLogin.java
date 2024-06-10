package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class EmployeeLogin extends AppCompatActivity {

    private EditText editTextEmployeeEmail;
    private EditText editTextEmployeePassword;
    private Button buttonEmployeeLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_login);

        mAuth = FirebaseAuth.getInstance();

        editTextEmployeeEmail = findViewById(R.id.editTextEmployeeEmail);
        editTextEmployeePassword = findViewById(R.id.editTextEmployeePassword);
        buttonEmployeeLogin = findViewById(R.id.buttonEmployeeLogin);

        buttonEmployeeLogin.setOnClickListener(v -> loginEmployee());
    }

    private void loginEmployee() {
        String email = editTextEmployeeEmail.getText().toString().trim();
        String password = editTextEmployeePassword.getText().toString().trim();

        if (!validateForm(email, password)) {
            return;
        }

        // Authenticate the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login is successful
                        Toast.makeText(EmployeeLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EmployeeLogin.this, EmployeeHomeActivity.class);
                        startActivity(intent);
                    } else {
                        // Login failed
                        Toast.makeText(EmployeeLogin.this, "Authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            editTextEmployeeEmail.setError("Required.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmployeeEmail.setError("Please enter a valid email address.");
            valid = false;
        } else {
            editTextEmployeeEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            editTextEmployeePassword.setError("Required.");
            valid = false;
        } else if (password.length() < 6) {
            editTextEmployeePassword.setError("Password must be at least 6 characters.");
            valid = false;
        } else {
            editTextEmployeePassword.setError(null);
        }

        return valid;
    }


}
