package com.example.coffeeshopapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<DocumentSnapshot> orderList;
    private FirebaseFirestore db;

    public OrderAdapter(Context context, List<DocumentSnapshot> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DocumentSnapshot document = orderList.get(position);
        String itemId = document.getString("itemId");

        // Fetch item details from the 'items' collection
        db.collection("menuItems").document(itemId).get().addOnSuccessListener(itemDocument -> {
            if (itemDocument.exists()) {
                holder.textViewOrderItemName.setText(itemDocument.getString("name"));
            } else {
                holder.textViewOrderItemName.setText("Unknown Item");
            }
        }).addOnFailureListener(e -> holder.textViewOrderItemName.setText("Failed to load item name"));

        String status = document.getString("preparationStatus");
        holder.textViewOrderStatus.setText("Status: " + status);
        holder.textViewOrderTotal.setText(String.format("Total: %.3f OMR", document.getDouble("totalBill")));

        // Set text color based on the status
        if ("Prepared".equals(status)) {
            holder.textViewOrderStatus.setTextColor(Color.BLUE);
        } else {
            holder.textViewOrderStatus.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderItemName, textViewOrderStatus, textViewOrderTotal;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderItemName = itemView.findViewById(R.id.textViewOrderItemName);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewOrderTotal = itemView.findViewById(R.id.textViewOrderTotal);
        }
    }
}
