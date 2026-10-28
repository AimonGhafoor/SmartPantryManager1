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
    private static final int DATABASE_VERSION = 3;

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

    // --- SEEDING METHOD ---
    private void seedRecipes(SQLiteDatabase db) {
        // Recipe 1: Tomato Pasta
        insertRecipe(db, "Tomato Pasta", "1. Boil pasta. 2. Cook tomatoes. 3. Mix.", new String[]{"pasta", "tomato"});

        // Recipe 2: Omelette
        insertRecipe(db, "Omelette", "1. Beat eggs. 2. Cook in pan.", new String[]{"egg", "butter"});

        // Recipe 3: Cheese Sandwich
        insertRecipe(db, "Cheese Sandwich", "1. Put cheese on bread. 2. Toast.", new String[]{"bread", "cheese"});

        // Recipe 4: Salad
        insertRecipe(db, "Salad", "1. Chop everything. 2. Mix.", new String[]{"lettuce", "tomato", "cucumber"});
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
}