package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class AddPantryItemActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText nameInput;
    private EditText quantityInput;
    private EditText expiryInput;
    private Spinner unitSpinner;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pantry_item);

        databaseHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.inputIngredientName);
        quantityInput = findViewById(R.id.inputIngredientQuantity);
        expiryInput = findViewById(R.id.inputIngredientExpiry);
        unitSpinner = findViewById(R.id.spinnerIngredientUnit);

        Button saveButton = findViewById(R.id.buttonSaveIngredient);
        Button cancelButton = findViewById(R.id.buttonCancelIngredient);
        TextView formTitle = findViewById(R.id.textFormTitle);

        itemId = getIntent().getIntExtra("pantry_item_id", -1);

        if (itemId != -1) {
            formTitle.setText("Edit ingredient");
            saveButton.setText("Update ingredient");
            loadIngredient();
        }
        saveButton.setOnClickListener(v -> saveIngredient());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadIngredient() {
        try (Cursor cursor = databaseHelper.getPantryItems()) {
            while (cursor.moveToNext()) {
                int savedId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("id"));

                if (savedId == itemId) {
                    nameInput.setText(cursor.getString(
                            cursor.getColumnIndexOrThrow("name")));

                    double quantity = cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity"));

                    quantityInput.setText(
                            String.format(Locale.getDefault(), "%s", quantity));

                    String savedUnit = cursor.getString(
                            cursor.getColumnIndexOrThrow("unit"));

                    ArrayAdapter<CharSequence> unitAdapter =
                            (ArrayAdapter<CharSequence>) unitSpinner.getAdapter();

                    int unitPosition = unitAdapter.getPosition(savedUnit);

                    if (unitPosition >= 0) {
                        unitSpinner.setSelection(unitPosition);
                    }

                    String expiry = cursor.getString(
                            cursor.getColumnIndexOrThrow("expiry"));

                    expiryInput.setText(expiry == null ? "" : expiry);
                    return;
                }
            }
        }

        Toast.makeText(this, "Ingredient not found", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveIngredient() {
        String ingredientName = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();
        String selectedUnit = unitSpinner.getSelectedItem().toString();
        String expiryDate = expiryInput.getText().toString().trim();

        if (ingredientName.isEmpty()) {
            nameInput.setError("Enter an ingredient name");
            nameInput.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            quantityInput.setError("Enter a quantity");
            quantityInput.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        }
        catch (NumberFormatException e) {
            quantityInput.setError("Enter a valid quantity");
            quantityInput.requestFocus();
            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {
            quantityInput.setError("Quantity must be greater than zero");
            quantityInput.requestFocus();
            return;
        }

        if (!expiryDate.isEmpty() && !expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            expiryInput.setError("Use yyyy-MM-dd format");
            expiryInput.requestFocus();
            return;
        }

        if (itemId == -1) {
            databaseHelper.addPantryItem(
                    ingredientName, quantity, selectedUnit, expiryDate);
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.updatePantryItem(
                    itemId, ingredientName, quantity, selectedUnit, expiryDate);
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}