package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.booksstorage.Data;
import com.example.booksstorage.MainActivity;
import com.example.booksstorage.R;
import com.example.booksstorage.ReadActivityRVadapter;

public class ReadActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private RecyclerView ReadActivityRV;
    private int orientation;
    private SharedPreferences sharedPeferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        this.mainLayout = (ConstraintLayout) findViewById(R.id.readMainLayout);

        this.ReadActivityRV = (RecyclerView) findViewById(R.id.ReadActivityRV);

        this.sharedPeferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void loadContent(){
        boolean lightDark = sharedPeferences.getBoolean("lightdark", true);
        if (lightDark){
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
            this.ReadActivityRV.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
        } else {
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
            this.ReadActivityRV.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
        }

        this.ReadActivityRV.setAdapter(new ReadActivityRVadapter(this));

        this.orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            this.ReadActivityRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            this.ReadActivityRV.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onBackPressed(){
        Intent back = null;
        switch(Data.getInstance().getActivityStack().peek()){
            case MAIN:
                back = new Intent(this, MainActivity.class);
                break;
        }
        Data.getInstance().getActivityStack().pop();
        startActivity(back);
    }
}