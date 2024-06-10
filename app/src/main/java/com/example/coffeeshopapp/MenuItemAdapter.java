package com.example.coffeeshopapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private Context context;
    private List<DocumentSnapshot> menuItemList;

    public MenuItemAdapter(Context context, List<DocumentSnapshot> menuItemList) {
        this.context = context;
        this.menuItemList = menuItemList;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int position) {
        DocumentSnapshot document = menuItemList.get(position);
        holder.textViewMenuItemName.setText(document.getString("name"));
        holder.textViewMenuItemDescription.setText(document.getString("description"));
        holder.textViewMenuItemPrice.setText(String.format("Price: %.3f OMR", document.getDouble("price")));

        holder.buttonOrder.setOnClickListener(v -> {
            Intent intent = new Intent(context, PaymentActivity.class);
            intent.putExtra("itemId", document.getId());
            intent.putExtra("itemName", document.getString("name"));
            intent.putExtra("price", document.getDouble("price"));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    static class MenuItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMenuItemName;
        TextView textViewMenuItemDescription;
        TextView textViewMenuItemPrice;
        Button buttonOrder;

        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMenuItemName = itemView.findViewById(R.id.textViewMenuItemName);
            textViewMenuItemDescription = itemView.findViewById(R.id.textViewMenuItemDescription);
            textViewMenuItemPrice = itemView.findViewById(R.id.textViewMenuItemPrice);
            buttonOrder = itemView.findViewById(R.id.buttonOrder);
        }
    }
}
