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
        List<String> recipes = dbHelper.getStrictlyMatchedRecipes();

        if (recipes.isEmpty()) {
            tvNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // We can reuse the PantryAdapter because it just takes a List<String>
            PantryAdapter adapter = new PantryAdapter(recipes);
            recyclerView.setAdapter(adapter);
        }
    }
}