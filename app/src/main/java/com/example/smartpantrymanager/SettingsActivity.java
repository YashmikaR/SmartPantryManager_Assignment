package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    private EditText profileNameInput;
    private SharedPreferences preferences;
    private SwitchCompat darkModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileNameInput = findViewById(R.id.editProfileName);
        Button saveButton = findViewById(R.id.buttonSaveSettings);
        Button backButton = findViewById(R.id.buttonCancelSettings);
        darkModeSwitch = findViewById(R.id.switchDarkMode);

        preferences = getSharedPreferences(
                "SmartPantrySettings", MODE_PRIVATE);

        profileNameInput.setText(
                preferences.getString("profile_name", ""));

        // load the saved appearance preference
        darkModeSwitch.setChecked(
                preferences.getBoolean("dark_mode", false));

        // apply dark mode immediately when the switch changes
        darkModeSwitch.setOnCheckedChangeListener((button, isChecked) -> {
            preferences.edit()
                    .putBoolean("dark_mode", isChecked)
                    .apply();

            AppCompatDelegate.setDefaultNightMode(
                    isChecked
                            ? AppCompatDelegate.MODE_NIGHT_YES
                            : AppCompatDelegate.MODE_NIGHT_NO);
        });

        saveButton.setOnClickListener(v -> saveProfile());
        backButton.setOnClickListener(v -> finish());
    }

    // save the profile name locally
    private void saveProfile() {
        String name = profileNameInput.getText().toString().trim();

        if (name.isEmpty()) {
            profileNameInput.setError("Please enter your name");
            profileNameInput.requestFocus();
            return;
        }

        boolean saved = preferences.edit()
                .putString("profile_name", name)
                .commit();

        if (saved) {
            Toast.makeText(this, "Profile saved",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Could not save profile",
                    Toast.LENGTH_SHORT).show();
        }
    }
}