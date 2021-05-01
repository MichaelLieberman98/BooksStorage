package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    /*
    searchField
    searchButton
    enterReadActivityButton
    enterToReadActivityButton
     */

    private EditText searchField;
    private Button searchButton;
    private Button enterReadActivityButton;
    private Button enterToReadActivityButton;




    //MP6
    public static final String TAG = "MainActivity";
    protected static final String ACTION_CUSTOM_BROADCAST = "com.example.NEW_BOOK_NOW";
    private BookStorageReceiver receiver;
    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.searchField = (EditText) findViewById(R.id.searchField);
        this.searchButton = (Button) findViewById(R.id.searchButton);
        this.enterReadActivityButton = (Button) findViewById(R.id.enterReadActivityButton);
        this.enterToReadActivityButton = (Button) findViewById(R.id.enterToReadActivityButton);


        //MP6
        this.receiver = new BookStorageReceiver();
        registerReceiver(this.receiver, addIntentFilters()); //when not using LocalBroadcastManager
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ACTION_CUSTOM_BROADCAST));
        /////
    }

    //MP6
    protected IntentFilter addIntentFilters(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_CUSTOM_BROADCAST);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW);
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY);
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        return intentFilter;
    }
    /////



    public void searchAction(View v){
        if (!(this.searchField.getText().length() == 0)){
            Data.getInstance().getActivityStack().push(Data.Activity.MAIN);
            Data.getInstance().setBooksFromAPI(new ArrayList<>()); //reseting the books from api list

            String[] allWords = this.searchField.getText().toString().split(" ");

            String completeSearch = allWords[0];

            if (allWords.length > 1){
                for (int i = 1; i < allWords.length; i++){
                    completeSearch += ("+"+allWords[i]);
                }
            }
            Data.getInstance().setBookSearch(Data.getInstance().bookSearchURL + completeSearch);
//        System.out.println("search = " + Data.getInstance().getBookSearch());
            Intent results = new Intent(this, APIResultsActivity.class);
            startActivity(results);
        } else {

        }
    }

    public void readAction(View v){
        Data.getInstance().getActivityStack().push(Data.Activity.MAIN);
        Intent toRead = new Intent(this, ReadActivity.class);
        startActivity(toRead);
    }

    public void toReadAction(View v){
        Data.getInstance().getActivityStack().push(Data.Activity.MAIN);
        Intent read = new Intent(this, ToReadActivity.class);
        startActivity(read);
    }

    @Override
    public void onBackPressed(){
        //notify user that they can't go back from main screen
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
        }
        return true;
    }
}