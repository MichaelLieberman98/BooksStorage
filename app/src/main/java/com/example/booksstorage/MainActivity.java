package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    LinearLayout mainBG;
    EditText searchField;
    ImageView bookImage;
    TextView tvuserName;
    Button searchButton;
    Button enterReadActivityButton;
    Button enterToReadActivityButton;
    String mainBookImage = "basketball";
    String userName = "Red Team";
    String contact = "tel:3478512548";


    public static final String TAG = "MainActivity";
    protected static final String ACTION_CUSTOM_BROADCAST = "com.example.NEW_BOOK_NOW";
    private BookStorageReceiver receiver;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.searchField = (EditText) findViewById(R.id.searchField);
        this.searchButton = (Button) findViewById(R.id.searchButton);
        this.enterReadActivityButton = (Button) findViewById(R.id.enterReadActivityButton);
        this.enterToReadActivityButton = (Button) findViewById(R.id.enterToReadActivityButton);
        this.bookImage = (ImageView) findViewById(R.id.book_image);
        this.tvuserName = (TextView) findViewById(R.id.username);
        this.mainBG = (LinearLayout) findViewById(R.id.main_bg);

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        getUserPreferences();
        setBookImage(mainBookImage);
        tvuserName.setText("Hello " + userName + " Time To Read Some Books!");


        this.receiver = new BookStorageReceiver();
        registerReceiver(this.receiver, addIntentFilters());
    }

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

    public void getUserPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mainBookImage = sharedPreferences.getString("main_image", "book1");
        userName = sharedPreferences.getString("user_name", "User");
        contact = sharedPreferences.getString("contact", "3478512548");
        boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
        if (lightDark){
            this.mainBG.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
            this.searchField.setBackground(getResources().getDrawable(R.drawable.border_dark_mode));
            this.searchField.setHintTextColor(getResources().getColor(R.color.dark_mode_hint_color));
            this.searchField.setTextColor(getResources().getColor(R.color.button_text_color));
            this.tvuserName.setTextColor(getResources().getColor(R.color.button_text_color));
        } else {
            this.mainBG.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
            this.searchField.setBackground(getResources().getDrawable(R.drawable.border_light_mode));
            this.searchField.setTextColor(getResources().getColor(R.color.textColor));
            this.tvuserName.setTextColor(getResources().getColor(R.color.textColor));
        }

        Data.getInstance().sortBooks(Data.getInstance().getBooksToRead(), sharedPreferences.getString("sorting_choice", "natural"));
        Data.getInstance().sortBooks(Data.getInstance().getBooksAlreadyRead(), sharedPreferences.getString("sorting_choice", "natural"));
    }

    public void setBookImage(String mainBookImage) {
        bookImage = findViewById(R.id.book_image);
        switch (mainBookImage){
            case "book1":
                bookImage.setImageResource(R.drawable.book_icon_1);
                break;
            case "book2":
                bookImage.setImageResource(R.drawable.book_icon_2);
                break;
            case "book3":
                bookImage.setImageResource(R.drawable.book_icon_3);
                break;
            case "book4":
                bookImage.setImageResource(R.drawable.book_icon_4);
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "inside onRestart");
        getUserPreferences();
        setBookImage(mainBookImage);
        tvuserName.setText("Hello " + userName + " Time To Read Some Books!");
    }

    @Override
    protected void onDestroy(){
        this.unregisterReceiver(receiver);
        super.onDestroy();
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}