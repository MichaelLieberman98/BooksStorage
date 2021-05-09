package com.example.booksstorage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FetchBook extends AsyncTask<String, Void, String> {

    Context ct;
    FetchBook(Context ct){
        this.ct = ct;
    }

    @Override
    protected String doInBackground(String[] strings) {
        String s = NetworkUtils.getBookInfo(strings[0]);
        System.out.println("STRING LENGTH RETRIEVED " + s.length());
        return s;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < itemsArray.length(); i++){
                JSONObject JSONbook = (JSONObject) itemsArray.get(i);


                String url;
                Bitmap bitmap = null;
                if (JSONbook.getJSONObject("volumeInfo").has("imageLinks")){
                    url = JSONbook.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail");
                    String pre = url.substring(0, 4);
                    String post = url.substring(4, url.length());

//                System.out.println(pre+"s"+post);
                    try {
                        bitmap = new GetImageFromURL().execute(pre+"s"+post).get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    url = null;
                    bitmap = null;
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
//                System.out.println(publishDateArray.length);

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
                                -2000,
                                -1,
                                -1
                        );
                }




                String publisher = "";
                if (JSONbook.getJSONObject("volumeInfo").has("publisher")){
                    publisher = JSONbook.getJSONObject("volumeInfo").getString("publisher");
                } else {
                    publisher = "no known publisher";
                }



                String buyLink = "";

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

                Book newBook = new Book(
                        JSONbook.getString("id"),
                        bitmap,
                        url,
                        JSONbook.getJSONObject("volumeInfo").getString("title"),
                        description,
                        authors,
                        sPublishDate,
                        date,
                        publisher,
                        buyLink,
                        Data.BookReadStatus.UNKNOWN
                );

                Data.getInstance().getBooksFromAPI().add(newBook);
            }
        }catch (JSONException e){
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
                System.out.println(e);
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {

        }
    }
}