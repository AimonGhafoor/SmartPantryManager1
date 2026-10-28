package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private RecyclerView recyclerView;
    private EditText etName, etQuantity, etUnit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Load the layout we created for the pantry screen
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Find the UI elements by their IDs
        etName = view.findViewById(R.id.etItemName);
        etQuantity = view.findViewById(R.id.etQuantity);
        etUnit = view.findViewById(R.id.etUnit);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerViewPantry);

        // Setup the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadPantryItems();

        // What happens when you click "Add to Pantry"
        btnAdd.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String qtyStr = etQuantity.getText().toString().trim();
            String unit = etUnit.getText().toString().trim();

            // Input Validation
            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(getContext(), "Please enter name and quantity", Toast.LENGTH_SHORT).show();
                return;
            }

            double qty = Double.parseDouble(qtyStr);

            // Save to Database
            dbHelper.addPantryItem(name, qty, unit);

            // Clear the text boxes
            etName.setText("");
            etQuantity.setText("");
            etUnit.setText("");

            // Reload the list to show the new item
            loadPantryItems();
            Toast.makeText(getContext(), "Item added!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    // This method loads the list and uses the new Adapter constructor
    private void loadPantryItems() {
        List<String> items = dbHelper.getAllPantryItems();
        // We pass the dbHelper and the callback (this::loadPantryItems) so it refreshes after deleting
        adapter = new PantryAdapter(items, dbHelper, this::loadPantryItems);
        recyclerView.setAdapter(adapter);
    }
}