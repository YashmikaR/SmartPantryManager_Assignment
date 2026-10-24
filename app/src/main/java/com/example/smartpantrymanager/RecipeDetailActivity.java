package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView recipeName = findViewById(R.id.textDetailRecipeName);
        TextView recipeIngredients = findViewById(R.id.textDetailIngredients);
        TextView recipeMethod = findViewById(R.id.textDetailMethod);

        String recipeJson = getIntent().getStringExtra("recipe_json");

        if (recipeJson == null || recipeJson.trim().isEmpty()) {
            Toast.makeText(this, "Recipe details unavailable",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        try {
            JSONObject recipe = new JSONObject(recipeJson);

            recipeName.setText(recipe.optString("name", "Recipe details"));

            // extract the ingredients from the JSON stored in the recipe
            JSONArray ingredients = new JSONArray(
                    recipe.optString("ingredients_json", "[]"));

            StringBuilder ingredientText = new StringBuilder();

            for (int i = 0; i < ingredients.length(); i++) {
                JSONObject ingredient = ingredients.getJSONObject(i);

                String name = ingredient.optString("name", "Ingredient");
                double quantity = ingredient.optDouble("quantity", 0);
                String unit = ingredient.optString("unit", "");

                // display whole numbers without decimal places
                if (quantity == Math.floor(quantity)) {
                    ingredientText.append(String.format(
                            java.util.Locale.getDefault(),
                            "%.0f", quantity));
                } else {
                    ingredientText.append(quantity);
                }

                ingredientText.append(" ")
                        .append(unit)
                        .append(" ")
                        .append(name)
                        .append("\n");
            }

            recipeIngredients.setText(ingredientText.toString().trim());

            // display the preparation instructions
            String method = recipe.optString(
                    "method", "Preparation instructions unavailable");

            // place each numbered step on a separate line
            method = method.replaceAll("\\s+(?=\\d+\\.\\s)", "\n");

            recipeMethod.setText(method);
        }
        catch (Exception e) {
            Toast.makeText(this, "Could not load recipe details",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}