package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Find the switch by its ID
        SwitchMaterial switchExpiry = view.findViewById(R.id.switchExpiry);

        // Listen for when the user clicks the switch
        switchExpiry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getContext(), "Expiry alerts enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Expiry alerts disabled", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}