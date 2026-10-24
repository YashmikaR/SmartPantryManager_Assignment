
package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE pantry (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL, " +
                        "quantity REAL NOT NULL, " +
                        "unit TEXT NOT NULL, " +
                        "expiry TEXT)"
        );

        db.execSQL(
                "CREATE TABLE recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "name TEXT NOT NULL UNIQUE, " +
                        "ingredients_json TEXT NOT NULL, " +
                        "method TEXT NOT NULL)"
        );

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        seedRecipes(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
                {"Avocado Toast",
                        "bread|2|slice;avocado|1|piece;lemon|0.5|piece",
                        "1. Toast the bread. 2. Mash the avocado with a little lemon juice. 3. Spread over the toast."},

                {"Creamy Spinach Pasta",
                        "pasta|120|g;spinach|60|g;cream|80|ml;garlic|1|piece",
                        "1. Cook the pasta. 2. Gently fry chopped garlic. 3. Add spinach and cream. 4. Mix with the pasta."},

                {"Sweet Potato Wedges",
                        "sweet potato|2|piece;oil|10|ml;paprika|2|g",
                        "1. Cut the sweet potato into wedges. 2. Toss with oil and paprika. 3. Bake at 200 degrees C until tender and golden."},

                {"Apple Cinnamon Oats",
                        "oats|50|g;milk|200|ml;apple|1|piece;cinnamon|2|g",
                        "1. Add oats and milk to a pot. 2. Grate or chop the apple. 3. Simmer together and add cinnamon."},

                {"Bean and Corn Wrap",
                        "tortilla|1|piece;beans|100|g;corn|50|g;cheese|25|g",
                        "1. Mix the beans and corn. 2. Spoon onto the tortilla. 3. Add cheese, fold, and toast in a pan."},

                {"Lemon Garlic Chicken",
                        "chicken|150|g;lemon|1|piece;garlic|1|piece;oil|10|ml",
                        "1. Mix lemon juice, chopped garlic, and oil. 2. Coat the chicken. 3. Cook thoroughly in a pan."},

                {"Carrot Fritters",
                        "carrot|2|piece;egg|1|piece;flour|40|g;oil|10|ml",
                        "1. Grate the carrots. 2. Mix with egg and flour. 3. Fry spoonfuls in a little oil until cooked on both sides."},

                {"Chickpea Curry",
                        "chickpeas|150|g;tomato|1|piece;onion|1|piece;curry powder|5|g",
                        "1. Chop the onion and tomato. 2. Cook the onion until soft. 3. Add tomato, curry powder, and chickpeas. 4. Simmer until hot."},

                {"Berry Yoghurt Bowl",
                        "yoghurt|150|g;berries|80|g;honey|10|g;oats|20|g",
                        "1. Spoon yoghurt into a bowl. 2. Add berries and oats. 3. Drizzle with honey."},

                {"Mushroom Rice Bowl",
                        "rice|150|g;mushroom|120|g;soy sauce|10|ml;oil|5|ml",
                        "1. Cook the rice. 2. Slice and fry the mushrooms in oil. 3. Add soy sauce and serve over rice."},

                {"Tuna Cucumber Sandwich",
                        "bread|2|slice;tuna|1|can;cucumber|0.5|piece;mayonnaise|15|g",
                        "1. Drain the tuna. 2. Mix with mayonnaise. 3. Slice the cucumber and assemble the sandwich."},

                {"Pumpkin Soup",
                        "pumpkin|250|g;onion|1|piece;stock|400|ml;cream|30|ml",
                        "1. Chop pumpkin and onion. 2. Simmer in stock until tender. 3. Blend carefully and stir in cream."},

                {"Broccoli Cheese Bake",
                        "broccoli|200|g;cheese|50|g;milk|100|ml;flour|10|g",
                        "1. Cook the broccoli until just tender. 2. Mix milk and flour in a saucepan until thickened. 3. Stir in cheese, pour over broccoli, and bake until golden."},

                {"Peanut Banana Oat Bites",
                        "banana|1|piece;oats|80|g;peanut butter|20|g",
                        "1. Mash the banana. 2. Mix with oats and peanut butter. 3. Shape into small bites and bake at 180 degrees C for 12–15 minutes."},

                {"Tomato Lentil Stew",
                        "lentils|150|g;tomato|2|piece;onion|1|piece;stock|300|ml",
                        "1. Chop the onion and tomatoes. 2. Add lentils and stock. 3. Simmer until the lentils are tender."},

                {"Herby Potato Salad",
                        "potato|2|piece;yoghurt|60|g;lemon|0.5|piece;parsley|5|g",
                        "1. Boil the potatoes until tender and let them cool. 2. Mix yoghurt with lemon juice and chopped parsley. 3. Toss together."},

                {"Pepper Egg Cups",
                        "egg|2|piece;bell pepper|1|piece;cheese|20|g",
                        "1. Chop the pepper and grate the cheese. 2. Beat the eggs and mix everything together. 3. Bake in greased muffin cups until the eggs are set."},

                {"Garlic Green Beans",
                        "green beans|180|g;garlic|1|piece;butter|10|g",
                        "1. Trim the green beans. 2. Cook until tender. 3. Melt butter in a pan, add chopped garlic, and toss with the beans."},

                {"Corn Fritters",
                        "corn|100|g;egg|1|piece;flour|40|g;milk|30|ml;oil|10|ml",
                        "1. Mix corn, egg, flour, and milk. 2. Heat a little oil in a pan. 3. Fry spoonfuls until golden on both sides."},

                {"Pear Breakfast Smoothie",
                        "pear|1|piece;banana|1|piece;milk|200|ml;yoghurt|50|g",
                        "1. Chop the pear and banana. 2. Add fruit, milk, and yoghurt to a blender. 3. Blend until smooth."}
        };

        for (String[] recipe : data) {
            JSONArray ingredients = new JSONArray();

            for (String item : recipe[1].split(";")) {
                String[] parts = item.split("\\|");

                try {
                    JSONObject ingredient = new JSONObject();
                    ingredient.put("name", parts[0]);
                    ingredient.put("quantity", Double.parseDouble(parts[1]));
                    ingredient.put("unit", parts[2]);
                    ingredients.put(ingredient);
                } catch (Exception e) {
                    // skip an invalid ingredient rather than stopping all seeding
                }
            }

            ContentValues values = new ContentValues();
            values.put("name", recipe[0]);
            values.put("ingredients_json", ingredients.toString());
            values.put("method", recipe[2]);

            int updated = db.update(
                    "recipes",
                    values,
                    "name = ?",
                    new String[]{recipe[0]}
            );

            if (updated == 0) {
                db.insert("recipes", null, values);
            }
        }
    }

    // add a pantry item
    public long addPantryItem(
            String name, double quantity, String unit, String expiry) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry", expiry);

        return db.insert("pantry", null, values);
    }

    // update an existing pantry item
    public int updatePantryItem(
            int id, String name, double quantity, String unit, String expiry) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name.trim());
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry", expiry);

        return db.update(
                "pantry",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // delete a pantry item
    public int deletePantryItem(int id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                "pantry",
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // read all pantry items. Remember to close the Cursor after using it
    public Cursor getPantryItems() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                "pantry",
                null,
                null,
                null,
                null,
                null,
                "name COLLATE NOCASE ASC"
        );
    }

    // read all recipes. remember to close the Cursor after using it
    public Cursor getRecipes() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                "recipes",
                null,
                null,
                null,
                null,
                null,
                "name COLLATE NOCASE ASC"
        );
    }

    // find recipes for which every ingredient is available in the pantry
    public ArrayList<JSONObject> getStrictMatches() {
        return findRecipeMatches(false);
    }

    // find recipes with at most one ingredient missing or insufficient
    public ArrayList<JSONObject> getAlmostThereRecipes() {
        return findRecipeMatches(true);
    }

    private ArrayList<JSONObject> findRecipeMatches(boolean allowOneMissing) {
        ArrayList<JSONObject> matches = new ArrayList<>();
        Map<String, Double> pantryAmounts = new HashMap<>();

        try (Cursor pantry = getPantryItems()) {
            while (pantry.moveToNext()) {
                String name = normalizeName(
                        pantry.getString(pantry.getColumnIndexOrThrow("name"))
                );
                double quantity = pantry.getDouble(
                        pantry.getColumnIndexOrThrow("quantity")
                );
                String unit = pantry.getString(
                        pantry.getColumnIndexOrThrow("unit")
                );

                String key = name + "|" + unitGroup(unit);
                double baseAmount = toBaseQuantity(quantity, unit);

                if (baseAmount >= 0) {
                    pantryAmounts.put(
                            key,
                            pantryAmounts.getOrDefault(key, 0.0) + baseAmount
                    );
                }
            }
        }

        try (Cursor recipes = getRecipes()) {
            while (recipes.moveToNext()) {
                int id = recipes.getInt(recipes.getColumnIndexOrThrow("id"));
                String name = recipes.getString(
                        recipes.getColumnIndexOrThrow("name")
                );
                String ingredientsText = recipes.getString(
                        recipes.getColumnIndexOrThrow("ingredients_json")
                );
                String method = recipes.getString(
                        recipes.getColumnIndexOrThrow("method")
                );

                JSONArray ingredients = new JSONArray(ingredientsText);
                int missing = 0;
                JSONArray missingIngredients = new JSONArray();

                for (int i = 0; i < ingredients.length(); i++) {
                    JSONObject ingredient = ingredients.getJSONObject(i);
                    String ingredientName = normalizeName(
                            ingredient.getString("name")
                    );
                    double needed = ingredient.getDouble("quantity");
                    String unit = ingredient.getString("unit");
                    String key = ingredientName + "|" + unitGroup(unit);
                    double available = pantryAmounts.getOrDefault(key, 0.0);
                    double required = toBaseQuantity(needed, unit);

                    if (required < 0 || available + 0.0001 < required) {
                        missing++;
                        missingIngredients.put(ingredient);
                    }
                }

                boolean qualifies = allowOneMissing
                        ? missing > 0 && missing <= 1
                        : missing == 0;

                if (qualifies) {
                    JSONObject result = new JSONObject();
                    result.put("id", id);
                    result.put("name", name);
                    result.put("ingredients_json", ingredientsText);
                    result.put("method", method);
                    result.put("missing_count", missing);
                    result.put("missing_ingredients", missingIngredients);
                    matches.add(result);
                }
            }
        } catch (Exception e) {
            // return any matches found before a malformed recipe was encountered
        }
        return matches;
    }

    private String normalizeName(String name) {
        String result = name == null
                ? ""
                : name.trim().toLowerCase(Locale.ROOT);

        if (result.endsWith("ies") && result.length() > 3) {
            return result.substring(0, result.length() - 3) + "y";
        }

        if (result.endsWith("s") && !result.endsWith("ss")
                && result.length() > 1) {
            return result.substring(0, result.length() - 1);
        }

        return result;
    }

    private String unitGroup(String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);

        if (u.equals("g") || u.equals("kg") ||
                u.equals("oz") || u.equals("lb")) {
            return "weight";
        }

        if (u.equals("ml") || u.equals("l") ||
                u.equals("cup") || u.equals("tbsp") ||
                u.equals("tsp") || u.equals("fl oz")) {
            return "volume";
        }

        return u;
    }

    // convert compatible measurements to grams or millilitres
    // piece-based units are compared as piece counts
    private double toBaseQuantity(double quantity, String unit) {
        String u = unit.trim().toLowerCase(Locale.ROOT);

        switch (u) {
            case "g": return quantity;
            case "kg": return quantity * 1000.0;
            case "oz": return quantity * 28.3495;
            case "lb": return quantity * 453.592;

            case "ml": return quantity;
            case "l": return quantity * 1000.0;
            case "tsp": return quantity * 4.92892;
            case "tbsp": return quantity * 14.7868;
            case "cup": return quantity * 236.588;
            case "fl oz": return quantity * 29.5735;

            case "piece":
            case "pieces":
            case "slice":
            case "slices":
            case "can":
            case "cans":
                return quantity;

            default:
                return -1;
        }
    }
}