package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private RecyclerView recipesRecycler;
    private TextView emptyRecipesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recipesRecycler = findViewById(R.id.recyclerSuggestedRecipes);
        emptyRecipesMessage = findViewById(R.id.textEmptyRecipes);

        databaseHelper = new DatabaseHelper(this);

        recipesRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadSuggestedRecipes();
    }

    // display recipes that match every required ingredient
    private void loadSuggestedRecipes() {
        ArrayList<JSONObject> matches = databaseHelper.getStrictMatches();

        if (matches.isEmpty()) {
            emptyRecipesMessage.setText(
                    "No recipes match your pantry yet. Add more ingredients and try again.");
            emptyRecipesMessage.setVisibility(View.VISIBLE);
            recipesRecycler.setVisibility(View.GONE);
            return;
        }
        emptyRecipesMessage.setVisibility(View.GONE);
        recipesRecycler.setVisibility(View.VISIBLE);

        // recipe adapter
        recipesRecycler.setAdapter(new RecipeAdapter(matches));
    }
}