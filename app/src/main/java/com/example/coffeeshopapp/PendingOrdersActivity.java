package com.example.coffeeshopapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class PendingOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewPendingOrders;
    private PendingOrdersAdapter adapter;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        db = FirebaseFirestore.getInstance();
        recyclerViewPendingOrders = findViewById(R.id.recyclerViewPendingOrders);
        recyclerViewPendingOrders.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PendingOrdersAdapter(this, orderList);
        recyclerViewPendingOrders.setAdapter(adapter);

        fetchPendingOrders();
    }

    private void fetchPendingOrders() {
        db.collection("orders").whereEqualTo("preparationStatus", "Preparing")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            orderList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                        if (orderList.isEmpty()) {
                            Toast.makeText(this, "No pending orders found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
