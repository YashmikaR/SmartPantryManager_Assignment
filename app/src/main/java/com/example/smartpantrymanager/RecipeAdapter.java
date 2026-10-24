package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private final ArrayList<JSONObject> recipes;

    public RecipeAdapter(ArrayList<JSONObject> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        JSONObject recipe = recipes.get(position);
        holder.recipeName.setText(recipe.optString("name", "Untitled recipe"));

        try {
            JSONArray ingredients = new JSONArray(
                    recipe.optString("ingredients_json", "[]"));

            StringBuilder ingredientNames = new StringBuilder();

            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);

                if (ingredientNames.length() > 0) {
                    ingredientNames.append(", ");
                }

                ingredientNames.append(
                        ingredient.optString("name", "ingredient"));
            }

            String ingredientText = "Ingredients: " + ingredientNames;

            JSONArray missingIngredients =
                    recipe.optJSONArray("missing_ingredients");

            if (missingIngredients != null && missingIngredients.length() > 0) {
                StringBuilder missingText = new StringBuilder();

                for (int i = 0; i < missingIngredients.length(); i++) {
                    JSONObject missing = missingIngredients.getJSONObject(i);

                    if (missingText.length() > 0) {
                        missingText.append(", ");
                    }

                    missingText.append(missing.optString("name", "ingredient"));
                    missingText.append(" (");
                    missingText.append(missing.optString("quantity", ""));
                    missingText.append(" ");
                    missingText.append(missing.optString("unit", ""));
                    missingText.append(")");
                }

                ingredientText += "\nMissing: " + missingText;
            }

            holder.recipeIngredients.setText(ingredientText);

        } catch (Exception e) {
            holder.recipeIngredients.setText("Ingredients unavailable");
        }

        // open the selected recipe
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    v.getContext(), RecipeDetailActivity.class);
            intent.putExtra("recipe_json", recipe.toString());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView recipeName;
        TextView recipeIngredients;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.textRecipeName);
            recipeIngredients = itemView.findViewById(R.id.textRecipeIngredients);
        }
    }
}