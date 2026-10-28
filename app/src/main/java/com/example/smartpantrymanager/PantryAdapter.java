package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<String> pantryList;
    private DatabaseHelper dbHelper;
    private Runnable onDeleteCallback; // This tells the fragment to refresh the list

    // WE CHANGED THE CONSTRUCTOR HERE TO ACCEPT DBHELPER AND CALLBACK
    public PantryAdapter(List<String> pantryList, DatabaseHelper dbHelper, Runnable onDeleteCallback) {
        this.pantryList = pantryList;
        this.dbHelper = dbHelper;
        this.onDeleteCallback = onDeleteCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String fullString = pantryList.get(position); // e.g. "tomato - 5.0 pieces"
        holder.textView.setText(fullString + " (tap to delete)");

        holder.itemView.setOnClickListener(v -> {
            // Extract just the name (everything before the " - ")
            String itemName = fullString.split(" - ")[0];

            // Delete from database
            dbHelper.deletePantryItem(itemName);
            Toast.makeText(v.getContext(), "Deleted " + itemName, Toast.LENGTH_SHORT).show();

            // Refresh the list
            onDeleteCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}