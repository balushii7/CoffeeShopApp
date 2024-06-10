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

public class CoffeeShopAdapter extends RecyclerView.Adapter<CoffeeShopAdapter.CoffeeShopViewHolder> {

    private Context context;
    private List<DocumentSnapshot> coffeeShopList;

    public CoffeeShopAdapter(Context context, List<DocumentSnapshot> coffeeShopList) {
        this.context = context;
        this.coffeeShopList = coffeeShopList;
    }

    @NonNull
    @Override
    public CoffeeShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coffee_shop, parent, false);
        return new CoffeeShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeShopViewHolder holder, int position) {
        DocumentSnapshot document = coffeeShopList.get(position);
        holder.textViewCoffeeShopName.setText(document.getString("name"));
        holder.textViewCoffeeShopAddress.setText(document.getString("address"));
        holder.textViewCoffeeShopDescription.setText(document.getString("description"));

        holder.buttonViewItems.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewItemsActivity.class);
            intent.putExtra("coffeeShopId", document.getId());
            context.startActivity(intent);
        });

        holder.buttonBookTable.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookTableActivity.class);
            intent.putExtra("coffeeShopId", document.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return coffeeShopList.size();
    }

    static class CoffeeShopViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCoffeeShopName;
        TextView textViewCoffeeShopAddress;
        TextView textViewCoffeeShopDescription;
        Button buttonViewItems;
        Button buttonBookTable;

        public CoffeeShopViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCoffeeShopName = itemView.findViewById(R.id.textViewCoffeeShopName);
            textViewCoffeeShopAddress = itemView.findViewById(R.id.textViewCoffeeShopAddress);
            textViewCoffeeShopDescription = itemView.findViewById(R.id.textViewCoffeeShopDescription);
            buttonViewItems = itemView.findViewById(R.id.buttonViewItems);
            buttonBookTable = itemView.findViewById(R.id.buttonBookTable);
        }
    }
}
