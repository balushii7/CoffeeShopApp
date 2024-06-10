package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail, textViewAddress, textViewPhone;
    private Button buttonBackToHome;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textViewName = findViewById(R.id.textViewNameValue);
        textViewEmail = findViewById(R.id.textViewEmailValue);
        textViewAddress = findViewById(R.id.textViewAddressValue);
        textViewPhone = findViewById(R.id.textViewPhoneValue);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        loadUserProfile();

        buttonBackToHome.setOnClickListener(view -> {
            // Navigate back to HomeActivity
            Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserProfile() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            User user = task.getResult().toObject(User.class);
                            if (user != null) {
                                textViewName.setText(user.getName());
                                textViewEmail.setText(user.getEmail());
                                textViewAddress.setText(user.getAddress());
                                textViewPhone.setText(user.getPhone());
                            }
                        } else {
                            Toast.makeText(UserProfileActivity.this, "Failed to load user profile.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
