package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "PantryManager.db";
    // Changed to 6 to force the database to reload the new detailed recipes
    private static final int DATABASE_VERSION = 6;

    // Table Names
    private static final String TABLE_PANTRY = "pantry";
    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";

    // Pantry Table Columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_UNIT = "unit";

    // Recipe Table Columns
    private static final String KEY_RECIPE_ID = "recipe_id";
    private static final String KEY_RECIPE_NAME = "recipe_name";
    private static final String KEY_STEPS = "steps";

    // Recipe Ingredients Table Columns
    private static final String KEY_ING_RECIPE_ID = "ing_recipe_id";
    private static final String KEY_ING_NAME = "ing_name";
    private static final String KEY_ING_QUANTITY = "ing_quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_QUANTITY + " REAL,"
                + KEY_UNIT + " TEXT" + ")";

        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + KEY_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_RECIPE_NAME + " TEXT,"
                + KEY_STEPS + " TEXT" + ")";

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_ING_RECIPE_ID + " INTEGER,"
                + KEY_ING_NAME + " TEXT,"
                + KEY_ING_QUANTITY + " REAL" + ")";

        db.execSQL(CREATE_PANTRY_TABLE);
        db.execSQL(CREATE_RECIPES_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);

        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

    // --- PANTRY CRUD METHODS ---

    public void addPantryItem(String name, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.toLowerCase().trim());
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        db.insert(TABLE_PANTRY, null, values);
        db.close();
    }

    public List<String> getAllPantryItems() {
        List<String> pantryList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_PANTRY;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(1) + " - " + cursor.getDouble(2) + " " + cursor.getString(3);
                pantryList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return pantryList;
    }

    public void deletePantryItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, KEY_NAME + " = ?", new String[]{name.toLowerCase().trim()});
        db.close();
    }

    // --- SEEDING METHOD (With detailed quantities and instructions) ---
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Tomato Pasta",
                "1. Boil 200g of pasta in salted water for 8-10 minutes.\n\n2. In a pan, heat 1 tbsp of oil and cook 2 chopped tomatoes until soft.\n\n3. Mix the pasta and tomatoes together. Season with salt and serve!",
                new String[]{"pasta", "tomato"});

        insertRecipe(db, "Omelette",
                "1. Crack 2 eggs into a bowl and beat well with a pinch of salt.\n\n2. Melt 1 tbsp of butter in a non-stick pan over medium heat.\n\n3. Pour in the eggs and cook for 2-3 minutes until firm. Fold in half and serve.",
                new String[]{"egg", "butter"});

        insertRecipe(db, "Cheese Sandwich",
                "1. Take 2 slices of bread and spread a little butter on them.\n\n2. Place 2 slices of cheese between the bread.\n\n3. Toast in a sandwich maker for 3 minutes or until golden brown.",
                new String[]{"bread", "cheese"});

        insertRecipe(db, "Fresh Salad",
                "1. Wash and chop 1 cup of lettuce.\n\n2. Dice 1 tomato and half a cucumber.\n\n3. Toss everything in a bowl with a drizzle of olive oil and salt.",
                new String[]{"lettuce", "tomato", "cucumber"});

        insertRecipe(db, "Scrambled Eggs",
                "1. Whisk 2 eggs with 2 tbsp of milk.\n\n2. Melt 1 tbsp of butter in a pan over low heat.\n\n3. Pour in the eggs and stir gently with a spatula until creamy. Serve immediately.",
                new String[]{"egg", "butter", "milk"});

        insertRecipe(db, "French Toast",
                "1. Beat 1 egg with 50ml of milk in a shallow dish.\n\n2. Dip 2 slices of bread into the mixture, coating both sides.\n\n3. Fry in a buttered pan for 2 minutes on each side until golden.",
                new String[]{"bread", "egg", "milk"});

        insertRecipe(db, "Garlic Pasta",
                "1. Boil 150g of pasta until al dente.\n\n2. Mince 2 cloves of garlic and fry in 2 tbsp of oil until fragrant.\n\n3. Toss the pasta in the garlic oil and serve.",
                new String[]{"pasta", "garlic", "oil"});

        insertRecipe(db, "Grilled Cheese",
                "1. Butter 2 slices of bread on one side.\n\n2. Place 2 slices of cheese between the unbuttered sides.\n\n3. Grill in a pan for 3 minutes per side until the cheese melts.",
                new String[]{"bread", "cheese", "butter"});

        insertRecipe(db, "Tomato Soup",
                "1. Boil 4 chopped tomatoes in 2 cups of water for 15 minutes.\n\n2. Blend the mixture until smooth.\n\n3. Add 1 tsp of salt and a pinch of pepper. Serve hot.",
                new String[]{"tomato", "water", "salt"});

        insertRecipe(db, "Tuna Sandwich",
                "1. Drain 1 can of tuna and mix it with 2 tbsp of mayonnaise.\n\n2. Spread the mixture onto 2 slices of bread.\n\n3. Close the sandwich and cut in half.",
                new String[]{"bread", "tuna", "mayonnaise"});

        insertRecipe(db, "Pancakes",
                "1. Mix 1 cup of flour, 1 cup of milk, and 1 egg into a smooth batter.\n\n2. Heat a lightly oiled pan over medium heat.\n\n3. Pour 1/4 cup of batter into the pan and cook for 2 minutes per side.",
                new String[]{"flour", "milk", "egg"});

        insertRecipe(db, "Chicken Stir-fry",
                "1. Slice 200g of chicken breast into strips.\n\n2. Heat 1 tbsp of oil in a wok and fry the chicken for 5 minutes.\n\n3. Add 1 cup of mixed vegetables and stir-fry for another 5 minutes.",
                new String[]{"chicken", "oil", "vegetables"});

        insertRecipe(db, "Rice and Beans",
                "1. Rinse 1 cup of rice and boil in 2 cups of water for 15 minutes.\n\n2. In a separate pot, heat 1 can of beans.\n\n3. Serve the beans over the cooked rice.",
                new String[]{"rice", "beans", "water"});

        insertRecipe(db, "Peanut Butter Toast",
                "1. Toast 2 slices of bread until crispy.\n\n2. Spread 2 tbsp of peanut butter evenly over the toast.\n\n3. Serve immediately.",
                new String[]{"bread", "peanut butter"});

        insertRecipe(db, "Fruit Salad",
                "1. Chop 1 apple, 1 banana, and 1 orange into bite-sized pieces.\n\n2. Place all the fruit into a bowl.\n\n3. Mix gently and chill in the fridge for 10 minutes before serving.",
                new String[]{"apple", "banana", "orange"});

        insertRecipe(db, "Beef Burger",
                "1. Shape 150g of minced beef into a patty and season with salt.\n\n2. Grill the patty for 4 minutes on each side.\n\n3. Place the beef in a bun and top with a slice of cheese.",
                new String[]{"beef", "bun", "cheese"});
    }

    private long insertRecipe(SQLiteDatabase db, String name, String steps, String[] ingredients) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put(KEY_RECIPE_NAME, name);
        recipeValues.put(KEY_STEPS, steps);
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        for (String ing : ingredients) {
            ContentValues ingValues = new ContentValues();
            ingValues.put(KEY_ING_RECIPE_ID, recipeId);
            ingValues.put(KEY_ING_NAME, ing);
            ingValues.put(KEY_ING_QUANTITY, 1.0);
            db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
        }
        return recipeId;
    }

    // --- STRICT MATCHING LOGIC ---
    public List<String> getStrictlyMatchedRecipes() {
        List<String> matchedRecipes = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT r." + KEY_RECIPE_ID + ", r." + KEY_RECIPE_NAME +
                " FROM " + TABLE_RECIPES + " r " +
                " WHERE r." + KEY_RECIPE_ID + " NOT IN (" +
                "   SELECT ri." + KEY_ING_RECIPE_ID + " FROM " + TABLE_RECIPE_INGREDIENTS + " ri " +
                "   LEFT JOIN " + TABLE_PANTRY + " p ON LOWER(ri." + KEY_ING_NAME + ") = LOWER(p." + KEY_NAME + ") " +
                "   WHERE p." + KEY_NAME + " IS NULL " +
                "   OR p." + KEY_QUANTITY + " < ri." + KEY_ING_QUANTITY +
                ")";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                matchedRecipes.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return matchedRecipes;
    }

    // --- GET RECIPE STEPS FOR DETAIL SCREEN ---
    public String getRecipeSteps(String recipeName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String steps = "";

        Cursor cursor = db.rawQuery("SELECT " + KEY_STEPS + " FROM " + TABLE_RECIPES +
                " WHERE " + KEY_RECIPE_NAME + " = ?", new String[]{recipeName});

        if (cursor.moveToFirst()) {
            steps = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return steps;
    }
}