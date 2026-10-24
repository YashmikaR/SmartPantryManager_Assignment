package com.example.smartpantrymanager;

import android.app.AlertDialog;
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
    private final OnPantryItemDeleteListener deleteListener;

    // handles taps on pantry items so the selected ingredient can be edited
    public interface OnPantryItemClickListener {
        void onPantryItemClick(PantryItem item);
    }

    // handles deletion after the user confirms the action
    public interface OnPantryItemDeleteListener {
        void onPantryItemDelete(PantryItem item);
    }

    public PantryAdapter(ArrayList<PantryItem> pantryItems,
                         OnPantryItemClickListener clickListener,
                         OnPantryItemDeleteListener deleteListener) {
        this.pantryItems = pantryItems;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout used to display each pantry ingredient
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);

        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        // display the ingredient name and format whole-number quantities without decimals
        holder.ingredientName.setText(item.getName());

        String quantityText;
        if (item.getQuantity() == Math.floor(item.getQuantity())) {
            quantityText = String.format(
                    Locale.getDefault(), "%.0f", item.getQuantity());
        } else {
            quantityText = String.valueOf(item.getQuantity());
        }

        holder.ingredientQuantity.setText(quantityText + " " + item.getUnit());

        // show a different message when no expiry date has been entered
        String expiry = item.getExpiryDate();

        if (expiry == null || expiry.trim().isEmpty()) {
            holder.ingredientExpiry.setText("Expiry date: not set");
        } else {
            holder.ingredientExpiry.setText("Expires: " + expiry);
        }

        // open the selected ingredient in the add/edit screen when tapped
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onPantryItemClick(item);
            }
        });

        // ask for confirmation before removing an ingredient from the pantry
        holder.itemView.setOnLongClickListener(v -> {
            if (deleteListener != null) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete ingredient")
                        .setMessage("Delete " + item.getName() + " from your pantry?")
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Delete", (dialog, which) ->
                                deleteListener.onPantryItemDelete(item))
                        .show();
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientExpiry;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            // connect the ingredient details to their views in item_pantry.xml
            ingredientName = itemView.findViewById(R.id.textIngredientName);
            ingredientQuantity = itemView.findViewById(R.id.textIngredientQuantity);
            ingredientExpiry = itemView.findViewById(R.id.textIngredientExpiry);
        }
    }

    public void updateItems(ArrayList<PantryItem> newItems) {
        // refresh the displayed list after pantry data changes
        pantryItems.clear();
        pantryItems.addAll(newItems);
        notifyDataSetChanged();
    }
}