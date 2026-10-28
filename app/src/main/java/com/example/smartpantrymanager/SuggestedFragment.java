package com.example.smartpantrymanager;

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
        List<String> recipes = dbHelper.getStrictlyMatchedRecipes();

        if (recipes.isEmpty()) {
            // If no matches, show the error message and hide the list
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            // If there are matches, hide the error message and show the list
            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // We reuse the PantryAdapter.
            // Since we don't want to delete recipes by tapping them, we pass an empty callback "() -> {}"
            PantryAdapter adapter = new PantryAdapter(recipes, dbHelper, () -> {});
            recyclerView.setAdapter(adapter);
        }
    }
}