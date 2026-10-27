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
    private static final int DATABASE_VERSION = 1;

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

    // Recipe Ingredients Table Columns (Links ingredients to recipes)
    private static final String KEY_ING_RECIPE_ID = "ing_recipe_id";
    private static final String KEY_ING_NAME = "ing_name";
    private static final String KEY_ING_QUANTITY = "ing_quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Tables
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

        // Pre-load some test recipes (Assignment requires 15-20)
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
        values.put(KEY_NAME, name.toLowerCase().trim()); // Normalize name to lowercase
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

    // --- SEEDING METHOD ---
    private void seedRecipes(SQLiteDatabase db) {
        // We will add the 15-20 recipes here in the next step.
        // Just adding one for testing right now.

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(KEY_RECIPE_NAME, "Tomato Pasta");
        recipeValues.put(KEY_STEPS, "1. Boil pasta. 2. Cook tomatoes. 3. Mix.");
        long recipeId = db.insert(TABLE_RECIPES, null, recipeValues);

        ContentValues ingValues = new ContentValues();
        ingValues.put(KEY_ING_RECIPE_ID, recipeId);
        ingValues.put(KEY_ING_NAME, "pasta");
        ingValues.put(KEY_ING_QUANTITY, 200.0);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);

        ingValues.put(KEY_ING_RECIPE_ID, recipeId);
        ingValues.put(KEY_ING_NAME, "tomato");
        ingValues.put(KEY_ING_QUANTITY, 2.0);
        db.insert(TABLE_RECIPE_INGREDIENTS, null, ingValues);
    }
}