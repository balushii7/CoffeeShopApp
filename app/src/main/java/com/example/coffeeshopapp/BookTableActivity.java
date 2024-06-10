package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class BookTableActivity extends AppCompatActivity {

    private EditText editTextBookingDate, editTextBookingTime, editTextBookingSeats;
    private Button buttonConfirmBooking, buttonHome;
    private TextView textViewBookingMessage;
    private FirebaseFirestore db;
    private String coffeeShopId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_table);

        db = FirebaseFirestore.getInstance();

        editTextBookingDate = findViewById(R.id.editTextBookingDate);
        editTextBookingTime = findViewById(R.id.editTextBookingTime);
        editTextBookingSeats = findViewById(R.id.editTextBookingSeats);
        buttonConfirmBooking = findViewById(R.id.buttonConfirmBooking);
        textViewBookingMessage = findViewById(R.id.textViewBookingMessage);
        buttonHome = findViewById(R.id.buttonBackToHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookTableActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        coffeeShopId = getIntent().getStringExtra("coffeeShopId");

        buttonConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateBookingForm()) {
                    confirmBooking();
                }
            }
        });
    }

    private boolean validateBookingForm() {
        boolean valid = true;

        if (TextUtils.isEmpty(editTextBookingDate.getText().toString())) {
            editTextBookingDate.setError("Required.");
            valid = false;
        } else {
            editTextBookingDate.setError(null);
        }

        if (TextUtils.isEmpty(editTextBookingTime.getText().toString())) {
            editTextBookingTime.setError("Required.");
            valid = false;
        } else {
            editTextBookingTime.setError(null);
        }

        if (TextUtils.isEmpty(editTextBookingSeats.getText().toString())) {
            editTextBookingSeats.setError("Required.");
            valid = false;
        } else {
            editTextBookingSeats.setError(null);
        }

        return valid;
    }

    private void confirmBooking() {
        Map<String, Object> booking = new HashMap<>();
        booking.put("date", editTextBookingDate.getText().toString());
        booking.put("time", editTextBookingTime.getText().toString());
        booking.put("seats", editTextBookingSeats.getText().toString());
        booking.put("coffeeShopId", coffeeShopId);

        // Get user email from FirebaseAuth
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            booking.put("userEmail", currentUser.getEmail());
        }

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    textViewBookingMessage.setText("Booking confirmed!");
                    textViewBookingMessage.setVisibility(View.VISIBLE);

                })
                .addOnFailureListener(e -> {
                    textViewBookingMessage.setText("Failed to confirm booking.");
                    textViewBookingMessage.setVisibility(View.VISIBLE);
                });
    }
}
