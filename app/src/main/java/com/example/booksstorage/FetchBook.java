package com.example.booksstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;


import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class FetchBook extends AsyncTask<String, Void, String> {

//    private static final String TAG = "FetchBook";
//    private WeakReference<TextView> titleText;
//    private WeakReference<TextView> authorText;

    Context ct;
    FetchBook(Context ct){
//        this.titleText = new WeakReference<>(titleText);
//        this.authorText = new WeakReference<>(authorText);
        this.ct = ct;
    }

    @Override
    protected String doInBackground(String[] strings) {

//        Log.d(TAG, "inside method doInBackground");

//        System.out.println("strings[0] = " + strings[0]);
        String s = NetworkUtils.getBookInfo(strings[0]);

//        Log.d(TAG, "STRING RETRIVED "+s);
//        System.out.println("STRING RETRIEVED " + s);
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        Log.d(TAG, "inside onPostExecute");

        try {
//            System.out.println("after try, s length = " + s.length());
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

//            System.out.println(jsonObject.toString(4));

//            System.out.println("items size = " + itemsArray.length());

            for (int i = 0; i < itemsArray.length(); i++){
                JSONObject JSONbook = (JSONObject) itemsArray.get(i);




                String url = JSONbook.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
                String pre = url.substring(0, 4);
                String post = url.substring(4, url.length());

                Bitmap bm = null;
//                System.out.println(pre+"s"+post);
                try {  //TEMP
                    bm = new GetImageFromURL().execute(pre+"s"+post).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                String description = "";
                if (JSONbook.getJSONObject("volumeInfo").has("description")){
                    description = JSONbook.getJSONObject("volumeInfo").getString("description");
                } else {
                    description = "no description found";
                }



                //https://stackoverflow.com/questions/15871309/convert-jsonarray-to-string-array/57092365
                JSONArray JSONauthors = JSONbook.getJSONObject("volumeInfo").getJSONArray("authors");
//                System.out.println("json authors size = " + JSONauthors.length());
                ArrayList<String> authors = new ArrayList<>();

                for (int j = 0; j < JSONauthors.length(); j++){
                    authors.add(JSONauthors.getString(j));
                }
                /////////////////////////////////////







                String sPublishDate = JSONbook.getJSONObject("volumeInfo").getString("publishedDate");
                String[] publishDateArray = sPublishDate.split("-");

                Date date = null;
                System.out.println(publishDateArray.length);

                switch (publishDateArray.length){
                    case 1:
                        date = new Date(
                                Integer.parseInt(publishDateArray[0]),
                                -1,
                                -1
                        );
                        break;

                    case 2:
                        date = new Date(
                                Integer.parseInt(publishDateArray[0]),
                                Integer.parseInt(publishDateArray[1]),
                                -1
                        );
                        break;
                    case 3:
                        date = new Date(
                                Integer.parseInt(publishDateArray[0]),
                                Integer.parseInt(publishDateArray[1]),
                                Integer.parseInt(publishDateArray[2])
                        );
                        break;
                    default:
                        date = new Date(
                                2000,
                                1,
                                1
                        );
                }




                String publisher = "";
                if (JSONbook.getJSONObject("volumeInfo").has("publisher")){
                    publisher = JSONbook.getJSONObject("volumeInfo").getString("publisher");
                } else {
                    publisher = "no known publisher";
                }



                String buyLink = ""; //should change later depending on group's preferences

                if (JSONbook.getJSONObject("volumeInfo").has("previewLink")){
                    buyLink = JSONbook.getJSONObject("volumeInfo").getString("previewLink");
                } else if (JSONbook.getJSONObject("volumeInfo").has("infoLink")){
                    buyLink = JSONbook.getJSONObject("volumeInfo").getString("infoLink");
                } else if (JSONbook.getJSONObject("volumeInfo").has("canonicalVolumeLink")){
                    buyLink = JSONbook.getJSONObject("volumeInfo").getString("canonicalVolumeLink");
                } else if (JSONbook.getJSONObject("saleInfo").has("buyLink")){
                    buyLink = JSONbook.getJSONObject("saleInfo").getString("buyLink");
                } else if (JSONbook.getJSONObject("accessInfo").has("epub")) {
                    if (JSONbook.getJSONObject("accessInfo").getJSONObject("epub").has("acsTokenLink")) {
                        buyLink = JSONbook.getJSONObject("accessInfo").getJSONObject("epub").getString("acsTokenLink");
                    }
                } else if (JSONbook.getJSONObject("accessInfo").has("webReaderLink")){
                    buyLink = JSONbook.getJSONObject("accessInfo").getString("webReaderLink");
                }


//                System.out.println("buy link = " + (JSONbook.getJSONObject("saleInfo").has("buyLink")));

                Book newBook = new Book(
                        JSONbook.getString("id"),
                        bm,
                        JSONbook.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"),
                        JSONbook.getJSONObject("volumeInfo").getString("title"),
                        description,
                        authors,
                        sPublishDate,
                        date,
                        publisher,
                        buyLink,
                        Data.BookReadStatus.UNKNOWN
                );

//                System.out.println("just created new book, authors length = " + newBook.getAuthors().size());

                Data.getInstance().getBooksFromAPI().add(newBook);

//                System.out.println(Data.getInstance().getBooksFromAPI().size());
            }
        }catch (JSONException e){
//            titleText.get().setText(R.string.exception_results);
//            authorText.get().setText(R.string.exception_results);
            e.printStackTrace();
        }


    }

    private class GetImageFromURL extends AsyncTask<String, Void, Bitmap>{ //https://stackoverflow.com/questions/11831188/how-to-get-bitmap-from-a-url-in-android
        Bitmap bm;
        public  GetImageFromURL (){
            this.bm = null;
        }

        public Bitmap getBm() {
            return bm;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap b = null;
            try {
                URL url = new URL(strings[0]);
                b = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                this.bm = b;
            } catch(IOException e) {
//                System.out.println(e);
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {

        }
    }
}