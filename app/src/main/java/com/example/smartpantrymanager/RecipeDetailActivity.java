package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvName = findViewById(R.id.tvRecipeName);
        TextView tvSteps = findViewById(R.id.tvRecipeSteps);

        // Get the recipe name passed from the previous screen
        String recipeName = getIntent().getStringExtra("RECIPE_NAME");

        if (recipeName != null) {
            tvName.setText(recipeName);

            // Get the steps from the database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            String steps = dbHelper.getRecipeSteps(recipeName);
            tvSteps.setText(steps);
        }
    }
}