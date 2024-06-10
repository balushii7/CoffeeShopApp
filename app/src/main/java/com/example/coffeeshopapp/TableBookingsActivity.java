package com.example.coffeeshopapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableBookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBookings;
    private BookingAdapter adapter;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> bookingList = new ArrayList<>();
    private Button buttonBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_bookings);

        db = FirebaseFirestore.getInstance();
        recyclerViewBookings = findViewById(R.id.recyclerViewBookings);
        recyclerViewBookings.setLayoutManager(new LinearLayoutManager(this));
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        adapter = new BookingAdapter(this, bookingList);
        recyclerViewBookings.setAdapter(adapter);

        buttonBackToHome.setOnClickListener(v -> finish());

        fetchBookings();
    }

    private void fetchBookings() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            db.collection("bookings").whereEqualTo("userEmail", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                Log.d("TableBookingsActivity", "No bookings found for email: " + userEmail);
                                Toast.makeText(this, "No bookings found.", Toast.LENGTH_SHORT).show();
                            } else {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    bookingList.add(document);
                                    Log.d("TableBookingsActivity", "Booking loaded: " + document.getId());
                                }
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("TableBookingsActivity", "Error loading bookings", task.getException());
                            Toast.makeText(this, "Failed to load bookings.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.w("TableBookingsActivity", "No user logged in");
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }
    }

}