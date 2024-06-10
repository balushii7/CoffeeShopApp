package com.example.coffeeshopapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMenuItems;
    private TextView textViewNoItems;
    private MenuItemAdapter adapter;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> menuItemList = new ArrayList<>();
    private String coffeeShopId;
    private Button buttonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);

        db = FirebaseFirestore.getInstance();

        recyclerViewMenuItems = findViewById(R.id.recyclerViewMenuItems);
        textViewNoItems = findViewById(R.id.textViewNoItems);
        recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MenuItemAdapter(this, menuItemList);
        recyclerViewMenuItems.setAdapter(adapter);

        // Get the coffee shop ID from the intent
        coffeeShopId = getIntent().getStringExtra("coffeeShopId");

        buttonHome = findViewById(R.id.buttonBackToHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewItemsActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        fetchMenuItems();
    }

    private void fetchMenuItems() {
        db.collection("menuItems")
                .whereEqualTo("coffeeShopId", coffeeShopId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            menuItemList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                        textViewNoItems.setVisibility(View.GONE);
                    } else {
                        Log.w("ViewItemsActivity", "Error getting documents or no items available.", task.getException());
                        textViewNoItems.setVisibility(View.VISIBLE);
                    }
                });
    }
}
