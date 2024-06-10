package com.example.coffeeshopapp;

import android.os.Bundle;
import android.view.View;
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

public class ViewOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderAdapter adapter;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> orderList = new ArrayList<>();
    private Button buttonBackToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        db = FirebaseFirestore.getInstance();
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        buttonBackToHome = findViewById(R.id.buttonBackToHome);

        adapter = new OrderAdapter(this, orderList);
        recyclerViewOrders.setAdapter(adapter);

        buttonBackToHome.setOnClickListener(v -> finish());

        fetchOrders();
    }

    private void fetchOrders() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("orders")
                    .whereEqualTo("userEmail", user.getEmail())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                orderList.add(document);
                            }
                            adapter.notifyDataSetChanged();
                            if (orderList.isEmpty()) {
                                Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
