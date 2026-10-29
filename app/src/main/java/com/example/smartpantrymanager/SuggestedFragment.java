package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SuggestedFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvNoRecipes;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the layout we created for the suggested screen
        View view = inflater.inflate(R.layout.fragment_suggested, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSuggested);
        tvNoRecipes = view.findViewById(R.id.tvNoRecipes);
        dbHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // We use onResume so that every time the user clicks the "Suggested" tab,
        // it refreshes and checks the database again for new matches.
        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {
        // Get the strict matches from the database
        final List<String> recipes = dbHelper.getStrictlyMatchedRecipes();

        if (recipes.isEmpty()) {
            // If no matches, show the error message and hide the list
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // If there are matches, hide the error message and show the list
            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Use our custom adapter to display the clean editorial list
            RecipeAdapter adapter = new RecipeAdapter(recipes);
            recyclerView.setAdapter(adapter);
        }
    }

    // --- CUSTOM ADAPTER FOR THE NEW CLEAN UI ---
    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

        private List<String> recipeList;

        public RecipeAdapter(List<String> recipeList) {
            this.recipeList = recipeList;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate our clean list item layout
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_recipe, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            final String recipeName = recipeList.get(position);

            // Set the recipe name
            holder.textView.setText(recipeName);

            // Make the entire row clickable to open the detail screen
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra("RECIPE_NAME", recipeName);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }

        class RecipeViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public RecipeViewHolder(@NonNull View itemView) {
                super(itemView);
                // Find the TextView from item_suggested_recipe.xml
                textView = itemView.findViewById(R.id.tvRecipeTitle);
            }
        }
    }
}