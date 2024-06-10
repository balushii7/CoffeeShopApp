package com.example.coffeeshopapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewShopsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewShops;
    private CoffeeShopAdapter adapter;
    private FirebaseFirestore db;
    private List<DocumentSnapshot> coffeeShopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shops);

        db = FirebaseFirestore.getInstance();

        recyclerViewShops = findViewById(R.id.recyclerViewShops);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CoffeeShopAdapter(this, coffeeShopList);
        recyclerViewShops.setAdapter(adapter);

        fetchCoffeeShops();
    }

    private void fetchCoffeeShops() {
        db.collection("coffeeShops")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            coffeeShopList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("ViewShopsActivity", "Error getting documents.", task.getException());
                    }
                });
    }
}
