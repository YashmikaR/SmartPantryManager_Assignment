package com.example.smartpantrymanager;

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
    private RecyclerView almostThereRecycler;
    private TextView emptyAlmostThereMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        recipesRecycler = findViewById(R.id.recyclerSuggestedRecipes);
        emptyRecipesMessage = findViewById(R.id.textEmptyRecipes);
        almostThereRecycler = findViewById(R.id.recyclerAlmostThere);
        emptyAlmostThereMessage = findViewById(R.id.textEmptyAlmostThere);

        databaseHelper = new DatabaseHelper(this);

        recipesRecycler.setLayoutManager(new LinearLayoutManager(this));
        almostThereRecycler.setLayoutManager(new LinearLayoutManager(this));

        loadSuggestedRecipes();
    }


    // load strict matches and near matches
    private void loadSuggestedRecipes() {
        ArrayList<JSONObject> matches = databaseHelper.getStrictMatches();
        ArrayList<JSONObject> almostThere = databaseHelper.getAlmostThereRecipes();

        if (matches.isEmpty()) {
            emptyRecipesMessage.setVisibility(View.VISIBLE);
            recipesRecycler.setVisibility(View.GONE);
        } else {
            emptyRecipesMessage.setVisibility(View.GONE);
            recipesRecycler.setVisibility(View.VISIBLE);
            recipesRecycler.setAdapter(new RecipeAdapter(matches));
        }

        if (almostThere.isEmpty()) {
            emptyAlmostThereMessage.setVisibility(View.VISIBLE);
            almostThereRecycler.setVisibility(View.GONE);
        } else {
            emptyAlmostThereMessage.setVisibility(View.GONE);
            almostThereRecycler.setVisibility(View.VISIBLE);
            almostThereRecycler.setAdapter(new RecipeAdapter(almostThere));
        }
    }
}