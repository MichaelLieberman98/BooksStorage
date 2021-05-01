package com.example.booksstorage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

public class ToReadActivity extends AppCompatActivity {
    RecyclerView ToReadActivityRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_read);

        this.ToReadActivityRV = (RecyclerView) findViewById(R.id.ToReadActivityRV);

        this.ToReadActivityRV.setAdapter(new ToReadActivityRVadapter(this));
        this.ToReadActivityRV.setLayoutManager(new LinearLayoutManager(this));

        System.out.println("toread books size = " + Data.getInstance().getBooksToRead().size());
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