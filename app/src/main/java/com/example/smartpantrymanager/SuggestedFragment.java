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

        loadSuggestedRecipes();
    }

    private void loadSuggestedRecipes() {

        final List<String> recipes = dbHelper.getStrictlyMatchedRecipes();

        if (recipes.isEmpty()) {

            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {

            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            RecipeAdapter adapter = new RecipeAdapter(recipes);
            recyclerView.setAdapter(adapter);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

        private List<String> recipeList;

        public RecipeAdapter(List<String> recipeList) {
            this.recipeList = recipeList;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggested_recipe, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
            final String recipeName = recipeList.get(position);

            holder.textView.setText(recipeName);

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
                textView = itemView.findViewById(R.id.tvRecipeTitle);
            }
        }
    }
}