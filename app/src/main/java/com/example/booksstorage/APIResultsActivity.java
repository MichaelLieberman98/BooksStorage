
package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class APIResultsActivity extends AppCompatActivity {
    private RecyclerView temprv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_p_i_results);

        this.temprv = (RecyclerView) findViewById(R.id.temprv); //TEMP

        try {
            new FetchBook(this).execute(Data.getInstance().getBookSearch()).get(); //https://stackoverflow.com/questions/14827532/waiting-till-the-async-task-finish-its-work/14827618
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.temprv.setAdapter(new tempAPIAdapter(this)); //TEMP
        this.temprv.setLayoutManager(new LinearLayoutManager(this)); //TEMP
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