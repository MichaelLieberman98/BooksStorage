package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

public class ToReadActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private RecyclerView ToReadActivityRV;
    private int orientation;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_read);

        this.mainLayout = findViewById(R.id.toReadMainLayout);

        this.ToReadActivityRV = (RecyclerView) findViewById(R.id.ToReadActivityRV);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        System.out.println("toread books size = " + Data.getInstance().getBooksToRead().size());
        loadContent();
    }

    public void loadContent(){
        boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
        if (lightDark){
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
            this.ToReadActivityRV.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
        } else {
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
            this.ToReadActivityRV.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
        }

        this.ToReadActivityRV.setAdapter(new ToReadActivityRVadapter(this));

//        this.orientation = getResources().getConfiguration().orientation;
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
//            this.ToReadActivityRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        } else {
//            this.ToReadActivityRV.setLayoutManager(new LinearLayoutManager(this));
//        }

        StaggeredGridLayoutManager layoutManager;
        this.orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }
        layoutManager.scrollToPosition(
                Data.getInstance().getChosenRecyclerViewPosition()
        );
        this.ToReadActivityRV.setLayoutManager(layoutManager);
    }

    @Override
    public void onBackPressed(){
        Data.getInstance().setChosenRecyclerViewPosition(0);
        Intent back = null;
        switch(Data.getInstance().getActivityStack().peek()){
            case MAIN:
                back = new Intent(this, MainActivity.class);
                break;
        }
        Data.getInstance().getActivityStack().pop();
        startActivity(back);
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}