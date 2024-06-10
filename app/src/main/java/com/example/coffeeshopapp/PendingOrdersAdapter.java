package com.example.coffeeshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.OrderViewHolder> {

    private Context context;
    private List<DocumentSnapshot> orderList;
    private FirebaseFirestore db;

    public PendingOrdersAdapter(Context context, List<DocumentSnapshot> orderList) {
        this.context = context;
        this.orderList = orderList;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        DocumentSnapshot document = orderList.get(position);

        // Fetch item details
        String itemId = document.getString("itemId");
        db.collection("menuItems").document(itemId).get().addOnSuccessListener(menuItemDoc -> {
            if (menuItemDoc.exists()) {
                holder.textViewOrderItemName.setText(menuItemDoc.getString("name"));
            } else {
                holder.textViewOrderItemName.setText("Item Name Unavailable");
            }
        }).addOnFailureListener(e -> holder.textViewOrderItemName.setText("Error loading item name"));

        // Set total bill
        Long totalBill = document.getLong("totalBill");
        holder.textViewOrderTotalBill.setText(totalBill != null ? "Total Bill: " + totalBill + " OMR" : "Total Bill: Unavailable");

        // Handle Mark Complete button
        holder.buttonMarkComplete.setOnClickListener(v -> {
            document.getReference().update("preparationStatus", "Prepared").addOnSuccessListener(aVoid -> {
                holder.buttonMarkComplete.setEnabled(false);
                holder.buttonMarkComplete.setText("Completed");
            }).addOnFailureListener(e -> holder.buttonMarkComplete.setText("Error"));
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderItemName, textViewOrderTotalBill;
        Button buttonMarkComplete;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderItemName = itemView.findViewById(R.id.textViewOrderItemNameAdmin);
            textViewOrderTotalBill = itemView.findViewById(R.id.textViewOrderTotalBillAdmin);
            buttonMarkComplete = itemView.findViewById(R.id.buttonMarkCompleteAdmin);
        }
    }
}
