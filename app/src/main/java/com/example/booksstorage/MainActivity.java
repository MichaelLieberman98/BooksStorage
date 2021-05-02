package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    /*
    searchField
    searchButton
    enterReadActivityButton
    enterToReadActivityButton
     */

    LinearLayout mainLayout;
    private int orientation;
    private SharedPreferences sharedPreferences;

    EditText searchField;
    ImageView bookImage;
    TextView tvuserName;
    Button searchButton;
    Button enterReadActivityButton;
    Button enterToReadActivityButton;
    String mainBookImage = "basketball";
    String userName = "Red Team";
    String contact = "tel:3478512548";



    //MP6
    public static final String TAG = "MainActivity";
    protected static final String ACTION_CUSTOM_BROADCAST = "com.example.NEW_BOOK_NOW";
    private BookStorageReceiver receiver;
    /////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainLayout = (LinearLayout) findViewById(R.id.mainActivitymainLayout);

        this.searchField = (EditText) findViewById(R.id.searchField);
        this.searchButton = (Button) findViewById(R.id.searchButton);
        this.enterReadActivityButton = (Button) findViewById(R.id.enterReadActivityButton);
        this.enterToReadActivityButton = (Button) findViewById(R.id.enterToReadActivityButton);
        this.bookImage = (ImageView) findViewById(R.id.book_image);
        this.tvuserName = (TextView) findViewById(R.id.username);



        loadContent();
    }

    public void loadContent(){
//        getUserPreferences();
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);


        boolean lightDark = this.sharedPreferences.getBoolean("lightdark", true);

        System.out.println("light dark = " + lightDark);
        if (lightDark){
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));

            this.searchField.setBackground(ContextCompat.getDrawable(this, R.drawable.lightmainsearchbarborder));

            this.searchField.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.lightmodesearchbaricon), null,null, null);
            this.searchField.setHintTextColor(getResources().getColor(R.color.light_mode_search_hint));
        } else {
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
        }

        mainBookImage = sharedPreferences.getString("main_image", "book1");
        userName = sharedPreferences.getString("user_name", "User");
        contact = sharedPreferences.getString("contact", "3478512548");




//        setBookImage(mainBookImage);
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




        tvuserName.setText("Hello " + userName + " Time To Read Some Books!");

        //MP6
        this.receiver = new BookStorageReceiver();
        registerReceiver(this.receiver, addIntentFilters()); //when not using LocalBroadcastManager
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ACTION_CUSTOM_BROADCAST));
        /////


    }



    public void getUserPreferences(){

    }

    public void setBookImage(String mainBookImage) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "inside onRestart");
        getUserPreferences();
        setBookImage(mainBookImage);
        tvuserName.setText("Hello " + userName + " Time To Read Some Books!");
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