package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    //settingsLayouts
    //settingsBackgrounds
    //settingsLanguage
    //settingsFonts
    //settingsDateFormats
    private Button settingsLayouts;
    private Button settingsBackgrounds;
    private Button settingsLanguage;
    private Button settingsFonts;
    private Button settingsDateFormats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.settingsLayouts = (Button) findViewById(R.id.settingsLayouts);
        this.settingsBackgrounds = (Button) findViewById(R.id.settingsBackgrounds);
        this.settingsLanguage = (Button) findViewById(R.id.settingsLanguage);
        this.settingsFonts = (Button) findViewById(R.id.settingsFonts);
        this.settingsDateFormats = (Button) findViewById(R.id.settingsDateFormats);


    }

    public void chooseLayouts(View v){

    }

    public void chooseBackgrounds(View v){

    }

    public void chooseLanguage(View v){

    }

    public void chooseFonts(View v){

    }

    public void chooseDateFormats(View v){

    }


}