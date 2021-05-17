package com.example.booksstorage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

public class SettingsActivity extends AppCompatActivity {
    FrameLayout settingsContainer;
    SwitchPreference preferenceScreenContainer;
    //https://stackoverflow.com/questions/39152571/how-to-change-switchpreference-title-when-is-clicked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment fragment = new SettingsFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settingsContainer, fragment)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        settingsContainer = findViewById(R.id.settingsContainer);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
        if (lightDark){
            this.settingsContainer.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
        } else {
            this.settingsContainer.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
        }

        this.preferenceScreenContainer = (SwitchPreference) fragment.findPreference("preferenceScreenContainer");
        SwitchPreference pref = (SwitchPreference) fragment.findPreference("lightdark");
    }

    public static class SettingsFragment extends PreferenceFragmentCompat{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
            onBackPressed(); //https://stackoverflow.com/questions/14437745/how-to-override-action-bar-back-button-in-android
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        //https://www.youtube.com/watch?v=dpgUYoy-Ilk
        overridePendingTransition(R.anim.slide_from_top, R.anim.slide_to_bottom);
    }
}