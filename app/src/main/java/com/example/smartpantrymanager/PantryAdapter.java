package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private List<String> pantryList;
    private DatabaseHelper dbHelper;
    private Runnable onDeleteCallback;

    public PantryAdapter(List<String> pantryList, DatabaseHelper dbHelper, Runnable onDeleteCallback) {
        this.pantryList = pantryList;
        this.dbHelper = dbHelper;
        this.onDeleteCallback = onDeleteCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // The string looks like: "egg - 2.0 pieces"
        String fullString = pantryList.get(position);

        // Split it into the name and the quantity
        String[] parts = fullString.split(" - ");
        String itemName = parts[0];
        String itemQuantity = parts.length > 1 ? parts[1] : "";

        // Set the text (Capitalize the first letter of the name)
        String capitalizedName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        holder.tvItemName.setText(capitalizedName);
        holder.tvItemQuantity.setText(itemQuantity);

        // Handle the delete button click
        holder.btnDelete.setOnClickListener(v -> {
            dbHelper.deletePantryItem(itemName);
            Toast.makeText(v.getContext(), "Deleted " + itemName, Toast.LENGTH_SHORT).show();
            onDeleteCallback.run();
        });
    }

    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemQuantity;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemQuantity = itemView.findViewById(R.id.tvItemQuantity);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}