package com.example.coffeeshopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<DocumentSnapshot> bookingList;

    public BookingAdapter(Context context, List<DocumentSnapshot> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        DocumentSnapshot document = bookingList.get(position);

        // Safely get the number of persons
        String persons = document.getString("seats");
        String personsText = (persons != null) ? persons + " persons" : "Info unavailable";

        holder.textViewPersons.setText(personsText);

        holder.textViewBookingDate.setText(document.getString("date") != null ? document.getString("date") : "Date unknown");
        holder.textViewBookingTime.setText(document.getString("time") != null ? document.getString("time") : "Time unknown");

        // Fetch coffee shop name using the coffeeShopId
        String coffeeShopId = document.getString("coffeeShopId");
        if (coffeeShopId != null) {
            FirebaseFirestore.getInstance().collection("coffeeShops").document(coffeeShopId)
                    .get().addOnSuccessListener(shopDocument -> {
                        if (shopDocument.exists()) {
                            holder.textViewCoffeeShopName.setText(shopDocument.getString("name"));
                        } else {
                            holder.textViewCoffeeShopName.setText("Shop details unavailable");
                        }
                    }).addOnFailureListener(e -> holder.textViewCoffeeShopName.setText("Error loading shop name"));
        } else {
            holder.textViewCoffeeShopName.setText("Coffee Shop ID missing");
        }
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBookingDate, textViewBookingTime, textViewCoffeeShopName, textViewPersons;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBookingDate = itemView.findViewById(R.id.textViewBookingDate);
            textViewBookingTime = itemView.findViewById(R.id.textViewBookingTime);
            textViewCoffeeShopName = itemView.findViewById(R.id.textViewCoffeeShopName);
            textViewPersons = itemView.findViewById(R.id.textViewPersons);
        }
    }
}
