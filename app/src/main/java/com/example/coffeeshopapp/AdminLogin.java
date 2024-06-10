package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminLogin extends AppCompatActivity {

    private EditText editTextAdminUsername;
    private EditText editTextAdminPassword;
    private Button buttonAdminLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextAdminUsername = findViewById(R.id.editTextAdminUsername);
        editTextAdminPassword = findViewById(R.id.editTextAdminPassword);
        buttonAdminLogin = findViewById(R.id.buttonAdminLogin);

        buttonAdminLogin.setOnClickListener(v -> {
            String username = editTextAdminUsername.getText().toString().trim();
            String password = editTextAdminPassword.getText().toString().trim();
            if ("admin".equals(username) && "admin".equals(password)) {
                Intent intent = new Intent(AdminLogin.this, AdminDashboardActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AdminLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
