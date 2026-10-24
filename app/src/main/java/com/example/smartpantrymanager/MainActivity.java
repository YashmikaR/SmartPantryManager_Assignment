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

        // this creates the database helper and opens the local database
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.getWritableDatabase();

        // this prepares the list and its adapter
        pantryItems = new ArrayList<>();

        pantryAdapter = new PantryAdapter(pantryItems, item -> {
            Toast.makeText(
                    MainActivity.this,
                    "Selected: " + item.getName(),
                    Toast.LENGTH_SHORT
            ).show();
        });

        pantryRecycler.setLayoutManager(new LinearLayoutManager(this));
        pantryRecycler.setAdapter(pantryAdapter);

        // this loads saved ingredients from sqlite
        loadPantryItems();

        // this temporarily confirms the add screen is our next step
        addIngredientButton.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        "The add ingredient screen is coming next",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    // this reads pantry records from the database and refreshes the list
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

                savedItems.add(
                        new PantryItem(id, name, quantity, unit, expiry)
                );
            }
        }

        pantryAdapter.updateItems(savedItems);

        // this shows a helpful message when no ingredients are saved
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

        // this refreshes the list when returning to the pantry screen
        if (databaseHelper != null && pantryAdapter != null) {
            loadPantryItems();
        }
    }
}