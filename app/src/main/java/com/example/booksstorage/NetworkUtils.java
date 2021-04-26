package com.example.booksstorage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils extends AsyncTaskLoader<String> {


    private static final String TAG = "NetworkUtils";

    private static final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";// Base URL for Books API.
    private static final String QUERY_PARAM = "q";// Parameter for the search string

    private static final String MAX_RESULTS = "maxResults";// Parameter that limits search results.
    private static final String MAX_RESULTS_VALUE_AS_STRING = "10";// value that limits search results.

    private static final String PRINT_TYPE = "printType";// Parameter to filter by print type.
    private static final String PRINT_TYPE_VALUE = "books";// value to filter by print type.



    public NetworkUtils(@NonNull Context context) {
        super(context);
    }


    protected static String getBookInfo(String query){
        String bookJSONString = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try{
//            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
//                    .appendQueryParameter(QUERY_PARAM, query)
//                    .appendQueryParameter(MAX_RESULTS, MAX_RESULTS_VALUE_AS_STRING)
//                    .appendQueryParameter(PRINT_TYPE, PRINT_TYPE_VALUE)
//                    .build();

//            Uri builtURI = Uri.parse("http://www.recipepuppy.com/api/?q=tuna&p=1");
            Uri builtURI = Uri.parse(query);

            System.out.println("search key = " + builtURI.toString());
            URL requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inStream));

            StringBuilder builder = new StringBuilder();
            String line = null;
            while(   (line = reader.readLine())   != null   ){
                builder.append(line + "\n");
            }

            if(builder.length()> 0){  //only if there was content returned
                bookJSONString = builder.toString();   // prepare the results to be returned
            }

        }catch (IOException e){
//            System.out.println("oops inside getBookInfo query was "+query);
            e.printStackTrace();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader !=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bookJSONString;
    }


    @Nullable
    @Override
    public String loadInBackground() {
        return null;
    }
}
