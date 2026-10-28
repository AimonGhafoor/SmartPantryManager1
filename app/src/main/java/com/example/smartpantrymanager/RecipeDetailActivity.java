package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView tvName = findViewById(R.id.tvRecipeName);
        TextView tvSteps = findViewById(R.id.tvRecipeSteps);

        // Set up a simple back button in the toolbar
        Toolbar toolbar = new Toolbar(this);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Closes this activity and goes back
            }
        });

        // Get the recipe name passed from the previous screen
        String recipeName = getIntent().getStringExtra("RECIPE_NAME");

        if (recipeName != null) {
            tvName.setText(recipeName);

            // Get the steps from the database
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            String steps = dbHelper.getRecipeSteps(recipeName);

            // If the steps are empty, show a default message
            if (steps == null || steps.isEmpty()) {
                tvSteps.setText("No steps available for this recipe.");
            } else {
                tvSteps.setText(steps);
            }
        }
    }
}