
package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

public class APIResultsActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private RecyclerView temprv;
    private int orientation;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_p_i_results);

        this.mainLayout = (ConstraintLayout) findViewById(R.id.resultsMainLayout);
        this.temprv = (RecyclerView) findViewById(R.id.temprv);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            new FetchBook(this).execute(Data.getInstance().getBookSearch()).get(); //https://stackoverflow.com/questions/14827532/waiting-till-the-async-task-finish-its-work/14827618
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadContent();

    }

    public void loadContent(){
        boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
        if (lightDark){
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
            this.temprv.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
        } else {
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
            this.temprv.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
        }

        this.temprv.setAdapter(new tempAPIAdapter(this));

        this.orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            this.temprv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        } else {
            this.temprv.setLayoutManager(new LinearLayoutManager(this));
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