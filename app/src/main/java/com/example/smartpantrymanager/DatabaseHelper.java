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
    // IMPORTANT: We changed this to 4 so it forces the database to recreate and add the new recipes!
    private static final int DATABASE_VERSION = 4;

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

    // Create (Add)
    public void addPantryItem(String name, double quantity, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name.toLowerCase().trim());
        values.put(KEY_QUANTITY, quantity);
        values.put(KEY_UNIT, unit);
        db.insert(TABLE_PANTRY, null, values);
        db.close();
    }

    // Read (Get all)
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

    // Delete
    public void deletePantryItem(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, KEY_NAME + " = ?", new String[]{name.toLowerCase().trim()});
        db.close();
    }

    // --- SEEDING METHOD (Now includes 16 recipes) ---
    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Tomato Pasta", "1. Boil pasta. 2. Cook tomatoes. 3. Mix.", new String[]{"pasta", "tomato"});
        insertRecipe(db, "Omelette", "1. Beat eggs. 2. Cook in pan with butter.", new String[]{"egg", "butter"});
        insertRecipe(db, "Cheese Sandwich", "1. Put cheese on bread. 2. Toast.", new String[]{"bread", "cheese"});
        insertRecipe(db, "Salad", "1. Chop everything. 2. Mix.", new String[]{"lettuce", "tomato", "cucumber"});
        insertRecipe(db, "Scrambled Eggs", "1. Whisk eggs. 2. Cook in butter.", new String[]{"egg", "butter", "milk"});
        insertRecipe(db, "French Toast", "1. Dip bread in egg. 2. Fry.", new String[]{"bread", "egg", "milk"});
        insertRecipe(db, "Garlic Pasta", "1. Boil pasta. 2. Fry garlic in oil. 3. Mix.", new String[]{"pasta", "garlic", "oil"});
        insertRecipe(db, "Grilled Cheese", "1. Butter bread. 2. Grill with cheese.", new String[]{"bread", "cheese", "butter"});
        insertRecipe(db, "Tomato Soup", "1. Boil tomatoes. 2. Blend.", new String[]{"tomato", "water", "salt"});
        insertRecipe(db, "Tuna Sandwich", "1. Mix tuna and mayo. 2. Put on bread.", new String[]{"bread", "tuna", "mayonnaise"});
        insertRecipe(db, "Pancakes", "1. Mix flour, milk, egg. 2. Fry.", new String[]{"flour", "milk", "egg"});
        insertRecipe(db, "Chicken Stir-fry", "1. Fry chicken. 2. Add veggies.", new String[]{"chicken", "oil", "vegetables"});
        insertRecipe(db, "Rice and Beans", "1. Boil rice. 2. Heat beans.", new String[]{"rice", "beans", "water"});
        insertRecipe(db, "Peanut Butter Toast", "1. Toast bread. 2. Add peanut butter.", new String[]{"bread", "peanut butter"});
        insertRecipe(db, "Fruit Salad", "1. Chop fruit. 2. Mix.", new String[]{"apple", "banana", "orange"});
        insertRecipe(db, "Beef Burger", "1. Grill beef. 2. Put in bun.", new String[]{"beef", "bun", "cheese"});
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