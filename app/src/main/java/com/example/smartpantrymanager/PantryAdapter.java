package com.example.smartpantrymanager;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        String fullString = pantryList.get(position);

        String[] parts = fullString.split(" - ");
        String itemName = parts[0];
        String itemQuantityAndUnit = parts.length > 1 ? parts[1] : "";

        String[] qtyUnitParts = itemQuantityAndUnit.split(" ");
        String itemQuantity = qtyUnitParts[0];
        String itemUnit = qtyUnitParts.length > 1 ? qtyUnitParts[1] : "";

        String capitalizedName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        holder.tvItemName.setText(capitalizedName);
        holder.tvItemQuantity.setText(itemQuantityAndUnit);

        holder.itemView.setOnClickListener(v -> {
            showEditDialog(v.getContext(), itemName, itemQuantity, itemUnit);
        });

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

    private void showEditDialog(Context context, String oldName, String oldQty, String oldUnit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit " + oldName.substring(0, 1).toUpperCase() + oldName.substring(1));

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 40);

        final EditText etNewName = new EditText(context);
        etNewName.setText(oldName);
        layout.addView(etNewName);

        final EditText etNewQty = new EditText(context);
        etNewQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        etNewQty.setText(oldQty);
        layout.addView(etNewQty);

        final EditText etNewUnit = new EditText(context);
        etNewUnit.setText(oldUnit);
        layout.addView(etNewUnit);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = etNewName.getText().toString().trim();
            String newQtyStr = etNewQty.getText().toString().trim();
            String newUnit = etNewUnit.getText().toString().trim();

            if (!newName.isEmpty() && !newQtyStr.isEmpty()) {
                double newQty = Double.parseDouble(newQtyStr);
                dbHelper.updatePantryItem(oldName, newName, newQty, newUnit);
                onDeleteCallback.run(); // Refresh the list
                Toast.makeText(context, "Item Updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Name and quantity cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
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