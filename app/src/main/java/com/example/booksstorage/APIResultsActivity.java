
package com.example.booksstorage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.M)
public class APIResultsActivity extends AppCompatActivity {
    private ConstraintLayout mainLayout;
    private RecyclerView temprv;
    private tempAPIAdapter adapter;
    private int orientation;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_p_i_results);

        this.mainLayout = (ConstraintLayout) findViewById(R.id.resultsMainLayout);
        this.temprv = (RecyclerView) findViewById(R.id.temprv);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (!Data.getInstance().getChoseAPIBook()){
            try {
                new FetchBook(this).execute(Data.getInstance().getBookSearch()).get(); //https://stackoverflow.com/questions/14827532/waiting-till-the-async-task-finish-its-work/14827618
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //https://stackoverflow.com/questions/28539666/recyclerview-adapter-and-viewholder-update-dynamically
        //https://stackoverflow.com/questions/9273989/how-do-i-retrieve-the-data-from-asynctasks-doinbackground
        //https://github.com/lalit3686/AsyncTaskReturnValue/blob/master/src/com/example/asynctaskreturnvalue/MainActivity.java

        Data.getInstance().setChoseAPIBook(false);
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

        this.adapter = new tempAPIAdapter(this);
//        adapter.notifyDataSetChanged(); //dynamic adding https://stackoverflow.com/questions/51261653/i-have-to-display-one-item-in-my-recyclerview-after-every-2-seconds
        this.temprv.setAdapter(adapter);

        this.orientation = getResources().getConfiguration().orientation;

        StaggeredGridLayoutManager layoutManager; //https://stackoverflow.com/questions/51499834/how-to-tell-recyclerview-to-start-at-specific-item-position
        this.orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        } else {
            layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        }

        layoutManager.scrollToPosition(
                Data.getInstance().getChosenRecyclerViewPosition()
        );
        this.temprv.setLayoutManager(layoutManager);
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
        //https://www.youtube.com/watch?v=dpgUYoy-Ilk
        overridePendingTransition(R.anim.slide_from_bottom, R.anim.slide_to_top);
    }
}