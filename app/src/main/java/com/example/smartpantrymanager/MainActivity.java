package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private PantryAdapter pantryAdapter;
    private ArrayList<PantryItem> pantryItems;
    private RecyclerView pantryRecycler;
    private TextView emptyPantryMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantryRecycler = findViewById(R.id.recyclerPantry);
        emptyPantryMessage = findViewById(R.id.textEmptyPantry);
        Button addIngredientButton = findViewById(R.id.buttonAddIngredient);

        // create the database helper and open the local database
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        // prepare the list and its adapter
        pantryItems = new ArrayList<>();

        pantryAdapter = new PantryAdapter(pantryItems, item -> {
            Intent intent = new Intent(MainActivity.this,
                    AddPantryItemActivity.class);
            intent.putExtra("pantry_item_id", item.getId());
            startActivity(intent);
        }, item -> {
            databaseHelper.deletePantryItem(item.getId());
            loadPantryItems();
            Toast.makeText(MainActivity.this,
                    "Ingredient deleted", Toast.LENGTH_SHORT).show();
        });

        pantryRecycler.setLayoutManager(new LinearLayoutManager(this));
        pantryRecycler.setAdapter(pantryAdapter);

        // load saved ingredients from sqlite
        loadPantryItems();

        // add ingredient to pantry
        addIngredientButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,
                    AddPantryItemActivity.class);
            startActivity(intent);
        });
    }

    // read pantry records from the database and refresh the list
    private void loadPantryItems() {
        ArrayList<PantryItem> savedItems = new ArrayList<>();

        try (Cursor cursor = databaseHelper.getPantryItems()) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit"));
                String expiry = cursor.getString(
                        cursor.getColumnIndexOrThrow("expiry"));

                savedItems.add(new PantryItem(id, name, quantity, unit, expiry));
            }
        }

        pantryAdapter.updateItems(savedItems);

        // show a helpful message when no ingredients are saved
        if (savedItems.isEmpty()) {
            emptyPantryMessage.setVisibility(TextView.VISIBLE);
            pantryRecycler.setVisibility(RecyclerView.GONE);
        } else {
            emptyPantryMessage.setVisibility(TextView.GONE);
            pantryRecycler.setVisibility(RecyclerView.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // refresh the list when returning to the pantry screen
        if (databaseHelper != null && pantryAdapter != null) {
            loadPantryItems();
        }
    }
}