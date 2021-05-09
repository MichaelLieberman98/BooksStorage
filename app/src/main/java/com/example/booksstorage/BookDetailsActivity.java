package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BookDetailsActivity extends AppCompatActivity {
    private LinearLayout mainLayout;
    private RelativeLayout detailsContainer;

    ImageView BookDetailsCover;
    private int orientation;
    private SharedPreferences sharedPreferences;

    TextView BookDetailsTitle;
    TextView BookDetailsAuthors;
    TextView BookDetailsPublishDate;
    TextView BookDetailsPublisher;

    TextView BookDetailsDescription;

    Button BookDetailsReadButton;
    Button BookDetailsToReadButton;
    Button BookDetailsShareButton;
    Button BookDetailsBuyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        this.mainLayout = (LinearLayout) findViewById(R.id.bookDetailsMainLayout);
        this.detailsContainer = (RelativeLayout) findViewById(R.id.detailsContainer);

        this.BookDetailsCover = (ImageView) findViewById(R.id.BookDetailsCover);
        this.BookDetailsTitle = (TextView) findViewById(R.id.BookDetailsTitle);
        this.BookDetailsAuthors = (TextView) findViewById(R.id.BookDetailsAuthors);
        this.BookDetailsPublishDate = (TextView) findViewById(R.id.BookDetailsPublishDate);
        this.BookDetailsPublisher = (TextView) findViewById(R.id.BookDetailsPublisher);
        this.BookDetailsDescription = (TextView) findViewById(R.id.BookDetailsDescription);

        this.BookDetailsReadButton = (Button) findViewById(R.id.BookDetailsReadButton);
        this.BookDetailsToReadButton = (Button) findViewById(R.id.BookDetailsToReadButton);
        this.BookDetailsShareButton = (Button) findViewById(R.id.BookDetailsShareButton);
        this.BookDetailsBuyButton = (Button) findViewById(R.id.BookDetailsBuyButton);
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        System.out.println("activity stack");
        System.out.println(Data.getInstance().getActivityStack().toString().replaceAll("\\[", "").replaceAll("]", ""));

        loadContent();
    }

    public void loadContent(){

        boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
        if (lightDark){
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.dark_mode_main_background));
            this.detailsContainer.setBackground(getResources().getDrawable(R.drawable.border_dark_mode));
            int textColor = this.getResources().getColor(R.color.button_text_color);
            this.BookDetailsTitle.setTextColor(textColor);
            this.BookDetailsAuthors.setTextColor(textColor);
            this.BookDetailsPublishDate.setTextColor(textColor);
            this.BookDetailsPublisher.setTextColor(textColor);
            this.BookDetailsDescription.setTextColor(textColor);

        } else {
            this.mainLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_main_background));
            this.detailsContainer.setBackground(getResources().getDrawable(R.drawable.border_light_mode));
        }

        this.BookDetailsCover.setImageBitmap(Data.getInstance().getClickedBook().getCover());

        this.BookDetailsTitle.setText(Data.getInstance().getClickedBook().getTitle());

        this.BookDetailsAuthors.setText(Data.getInstance().getClickedBook().getAuthors().get(0));

        System.out.println(Data.getInstance().getClickedBook().getPublishDate().getDay());
        System.out.println(Data.getInstance().getClickedBook().getPublishDate().getMonth());
        System.out.println(Data.getInstance().getClickedBook().getPublishDate().getYear());
        System.out.println();

        if (Data.getInstance().getClickedBook().getPublishDate().getDay() == -1 &&
                Data.getInstance().getClickedBook().getPublishDate().getMonth() == -1){
            this.BookDetailsPublishDate.setText(Data.getInstance().getClickedBook().getPublishDate().numsDateJustYear());
        } else if (Data.getInstance().getClickedBook().getPublishDate().getDay() == -1){
            this.BookDetailsPublishDate.setText(Data.getInstance().getClickedBook().getPublishDate().numsDateNoDay());
        } else {
            this.BookDetailsPublishDate.setText(Data.getInstance().getClickedBook().getPublishDate().numsDate());
        }

        this.BookDetailsPublishDate.setText(Data.getInstance().getClickedBook().getPublishDate().numsDate());

        this.BookDetailsPublisher.setText(Data.getInstance().getClickedBook().getPublisher());

        this.BookDetailsDescription.setText(Data.getInstance().getClickedBook().getDescription());

        loadButtons();
    }

    public void loadButtons(){
        switch(Data.getInstance().getClickedBook().getStatus()){ //https://stackoverflow.com/questions/5019857/how-to-set-a-button-gray-and-unclickable
            case UNKNOWN:
                break;
            case TOREAD:
                this.BookDetailsToReadButton.setEnabled(false);
                break;
            case READ:
                this.BookDetailsReadButton.setEnabled(false);
                this.BookDetailsToReadButton.setEnabled(false); //book in "read" shouldn't be able to return to "to read"
                break;
        }
    }

    public void BookDetailsReadAction(View v){
        switch(Data.getInstance().getClickedBook().getStatus()){
            case UNKNOWN:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.READ);
                Data.getInstance().getBooksAlreadyRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooks(Data.getInstance().getBooksAlreadyRead(), this.sharedPreferences.getString("sorting_choice", "natural"));

                loadButtons();
                break;
            case TOREAD:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.READ);
                Data.getInstance().getBooksAlreadyRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooks(Data.getInstance().getBooksAlreadyRead(), this.sharedPreferences.getString("sorting_choice", "natural"));

                loadButtons();

                for (int i = 0; i < Data.getInstance().getBooksToRead().size(); i++){
                    if (Data.getInstance().getBooksToRead().get(i).getId().equals(Data.getInstance().getClickedBook().getId())){
                        Data.getInstance().getBooksToRead().remove(i);
                    }
                }
                break;
            case READ:
                break;
        }
        loadButtons();
        Data.getInstance().printAllBooks();
    }

    public void BookDetailsToReadAction(View v){
        switch(Data.getInstance().getClickedBook().getStatus()){
            case UNKNOWN:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.TOREAD);
                Data.getInstance().getBooksToRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooks(Data.getInstance().getBooksToRead(), this.sharedPreferences.getString("sorting_choice", "natural"));
                loadButtons();
                break;
            case TOREAD:
            case READ:
                break;
        }
        Data.getInstance().printAllBooks();
    }

    public void BookDetailsShareAction(View v){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Check out this book! \n " + Data.getInstance().getClickedBook().getPurchaseLink(); //the content we are actually sharing
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void BookDetailsBuyAction(View v){
        //https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
        String url = Data.getInstance().getClickedBook().getPurchaseLink();
        Intent buy = new Intent(Intent.ACTION_VIEW);
        buy.setData(Uri.parse(url));
        startActivity(buy);
    }

    @Override
    public void onBackPressed(){
        Intent back = null;
        System.out.println("details activity stack  = " + Data.getInstance().getActivityStack().peek());
        switch(Data.getInstance().getActivityStack().peek()){
            case READ:
                back = new Intent(this, ReadActivity.class);
                break;
            case TOREAD:
                back = new Intent(this, ToReadActivity.class);
                break;
            case APIRESULTS:
                back = new Intent(this, APIResultsActivity.class);
                break;
        }
        Data.getInstance().getActivityStack().pop();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, this.BookDetailsCover, "transition");
        startActivity(back, options.toBundle());
    }
}