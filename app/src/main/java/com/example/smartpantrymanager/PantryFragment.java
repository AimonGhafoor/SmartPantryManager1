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
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        dbHelper = new DatabaseHelper(getContext());

        etName = view.findViewById(R.id.etItemName);
        etQuantity = view.findViewById(R.id.etQuantity);
        etUnit = view.findViewById(R.id.etUnit);
        Button btnAdd = view.findViewById(R.id.btnAdd);
        recyclerView = view.findViewById(R.id.recyclerViewPantry);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadPantryItems();

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

            // Reload the list
            loadPantryItems();
            Toast.makeText(getContext(), "Item added!", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void loadPantryItems() {
        List<String> items = dbHelper.getAllPantryItems();
        adapter = new PantryAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}