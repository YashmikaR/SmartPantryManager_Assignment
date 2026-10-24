package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final ArrayList<PantryItem> pantryItems;
    private final OnPantryItemClickListener clickListener;

    // this listener lets the activity respond when an ingredient is tapped
    public interface OnPantryItemClickListener {
        void onPantryItemClick(PantryItem item);
    }

    public PantryAdapter(ArrayList<PantryItem> pantryItems,
                         OnPantryItemClickListener clickListener) {
        this.pantryItems = pantryItems;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this inflates the xml layout for one pantry row
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        // this gets the ingredient for the current row
        PantryItem item = pantryItems.get(position);

        holder.ingredientName.setText(item.getName());

        // this avoids displaying whole quantities as values like 2.0
        String quantityText;
        if (item.getQuantity() == Math.floor(item.getQuantity())) {
            quantityText = String.format(
                    Locale.getDefault(), "%.0f", item.getQuantity());
        } else {
            quantityText = String.valueOf(item.getQuantity());
        }

        holder.ingredientQuantity.setText(quantityText + " " + item.getUnit());

        // this displays the expiry date when one has been entered
        String expiry = item.getExpiryDate();

        if (expiry == null || expiry.trim().isEmpty()) {
            holder.ingredientExpiry.setText("Expiry date: not set");
        } else {
            holder.ingredientExpiry.setText("Expires: " + expiry);
        }

        // this sends the selected ingredient back to the activity
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPantryItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    // this holds the views for one ingredient row
    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.textIngredientName);
            ingredientQuantity = itemView.findViewById(R.id.textIngredientQuantity);
            ingredientExpiry = itemView.findViewById(R.id.textIngredientExpiry);
        }
    }

    // this replaces the displayed ingredients after a database refresh
    public void updateItems(ArrayList<PantryItem> newItems) {
        pantryItems.clear();
        pantryItems.addAll(newItems);
        notifyDataSetChanged();
    }
}