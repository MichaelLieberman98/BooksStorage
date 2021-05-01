package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class BookDetailsActivity extends AppCompatActivity {
    /*
    BookDetailsCover

    BookDetailsTitle
    BookDetailsAuthors
    BookDetailsPublishDate
    BookDetailsPublisher

    BookDetailsDescription

    BookDetailsReadButton
    BookDetailsToReadButton
    BookDetailsShareButton
    BookDetailsBuyButton

     */
    ImageView BookDetailsCover;

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

        System.out.println("activity stack");
        System.out.println(Data.getInstance().getActivityStack().toString().replaceAll("\\[", "").replaceAll("]", ""));

        loadContent();
    }

    public void loadContent(){
        this.BookDetailsCover = (ImageView) findViewById(R.id.BookDetailsCover);
        this.BookDetailsCover.setImageBitmap(Data.getInstance().getClickedBook().getCover());

        this.BookDetailsTitle = (TextView) findViewById(R.id.BookDetailsTitle);
        this.BookDetailsTitle.setText(Data.getInstance().getClickedBook().getTitle());

        this.BookDetailsAuthors = (TextView) findViewById(R.id.BookDetailsAuthors);
        this.BookDetailsAuthors.setText(Data.getInstance().getClickedBook().getAuthors().get(0));

        this.BookDetailsPublishDate = (TextView) findViewById(R.id.BookDetailsPublishDate);
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


        this.BookDetailsPublishDate.setText(Data.getInstance().getClickedBook().getPublishDate().numsDate()); //MAKE DATE CLASS AND MAKE THIS LOOK BETTER

        this.BookDetailsPublisher = (TextView) findViewById(R.id.BookDetailsPublisher);
        this.BookDetailsPublisher.setText(Data.getInstance().getClickedBook().getPublisher());

        this.BookDetailsDescription = (TextView) findViewById(R.id.BookDetailsDescription);
        this.BookDetailsDescription.setText(Data.getInstance().getClickedBook().getDescription());

        this.BookDetailsReadButton = (Button) findViewById(R.id.BookDetailsReadButton);
        this.BookDetailsToReadButton = (Button) findViewById(R.id.BookDetailsToReadButton);
        this.BookDetailsShareButton = (Button) findViewById(R.id.BookDetailsShareButton);
        this.BookDetailsBuyButton = (Button) findViewById(R.id.BookDetailsBuyButton);


        loadButtons();
    }

    public void loadButtons(){
//        System.out.println(Data.getInstance().getClickedBook().getTitle() + " status = " + Data.getInstance().getClickedBook().getStatus());
        switch(Data.getInstance().getClickedBook().getStatus()){ //https://stackoverflow.com/questions/5019857/how-to-set-a-button-gray-and-unclickable
            case UNKNOWN:

                break;
            case TOREAD:
                this.BookDetailsToReadButton.setEnabled(false);
//                this.BookDetailsToReadButton.setBackgroundColor(getResources().getColor(R.color.disabledButton)); //https://stackoverflow.com/questions/5271387/how-can-i-get-color-int-from-color-resource
                break;
            case READ:
                this.BookDetailsReadButton.setEnabled(false);
                this.BookDetailsToReadButton.setEnabled(false); //book in "read" shouldn't be able to return to "to read"
//                this.BookDetailsReadButton.setBackgroundColor(getResources().getColor(R.color.disabledButton));
                break;
        }
    }







    public void BookDetailsReadAction(View v){

        switch(Data.getInstance().getClickedBook().getStatus()){
            case UNKNOWN:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.READ);
                Data.getInstance().getBooksAlreadyRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooksAlphabetically(Data.getInstance().getBooksAlreadyRead()); //SORTING

                loadButtons();
                break;
            case TOREAD:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.READ);
                Data.getInstance().getBooksAlreadyRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooksAlphabetically(Data.getInstance().getBooksAlreadyRead()); //SORTING

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
//        Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.READ); //set the current books status to "user already read this"
//        Data.getInstance().getBooksAlreadyRead().add(Data.getInstance().getClickedBook()); //add book to "already read" global list


//        if (this.BookDetailsToReadButton.isClickable()){ //clicked book exists inside "toRead", must remove
//            for (int i = 0; i < Data.getInstance().getBooksToRead().size(); i++){
//                if (Data.getInstance().getBooksToRead().get(i).getId().equals(Data.getInstance().getClickedBook().getId())){
//                    Data.getInstance().getBooksToRead().remove(i);
//                }
//            }
//
//            loadButtons();
//        }

        loadButtons(); //reload buttons so they update

        Data.getInstance().printAllBooks();
    }

    public void BookDetailsToReadAction(View v){
        switch(Data.getInstance().getClickedBook().getStatus()){
            case UNKNOWN:
                Data.getInstance().getClickedBook().setStatus(Data.BookReadStatus.TOREAD);
                Data.getInstance().getBooksToRead().add(Data.getInstance().getClickedBook());

                Data.getInstance().sortBooksAlphabetically(Data.getInstance().getBooksAlreadyRead()); //SORTING

                loadButtons();
                break;
            case TOREAD:
            case READ:
                break;
        }

        Data.getInstance().printAllBooks();
    }

    public void BookDetailsShareAction(View v){
        //NEED MP2/3 SHARE CODE

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Check out this book! \n " + Data.getInstance().getClickedBook().getPurchaseLink(); //the content we are actually sharing
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void BookDetailsBuyAction(View v){
        //https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
        String url = Data.getInstance().getClickedBook().getPurchaseLink();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
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
        startActivity(back);
    }
}