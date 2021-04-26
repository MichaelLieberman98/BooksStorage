package com.example.booksstorage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class BookStorageReceiver extends BroadcastReceiver {
    private Context context;
    protected static final String ACTION_CUSTOM_BROADCAST = "com.example.NEW_BOOK_NOW";

    @Override
    public void onReceive(Context context, Intent intent){
        this.context = context;
        String intentAction = intent.getAction();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        displayToastForIntentAction(intentAction);
    }

    private void displayToastForIntentAction(String intentAction){
        switch (intentAction) {
            case ACTION_CUSTOM_BROADCAST:
                Log.d("Broadcast Received", "CUSTOM BROADCAST");
                Toast.makeText(context, "CUSTOM BROADCAST", Toast.LENGTH_SHORT).show();


                Random rand = new Random();
                int randomIndex = 0;
                if (Data.getInstance().getBooksToRead().size() > 1){
                    randomIndex = rand.nextInt(Data.getInstance().getBooksToRead().size()-1);;
                }

                Book tempBook = Data.getInstance().getBooksToRead().get(randomIndex);

                Data.getInstance().setClickedBook(tempBook);

                Data.getInstance().getActivityStack().push(Data.Activity.MAIN);
                Data.getInstance().getActivityStack().push(Data.Activity.TOREAD);

                Intent showRandom = new Intent(this.context, BookDetailsActivity.class);
                this.context.startActivity(showRandom);


                break;

            case Intent.ACTION_POWER_CONNECTED:
                Log.d("Broadcast Received", "Power Connected");
                Toast.makeText(context, "Power Connected", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                Log.d("Broadcast Received", "Power Disonnected");
                Toast.makeText(context, "Power Disconnected", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_BATTERY_LOW:
                Log.d("Broadcast Received", "Battery Low");
                Toast.makeText(context, "Battery Low", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_BATTERY_OKAY:
                Log.d("Broadcast Received", "Battery OK");
                Toast.makeText(context, "Battery OK", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_AIRPLANE_MODE_CHANGED:
                Log.d("Broadcast Received", "Airplane mode change");
                Toast.makeText(context, "Airplane mode change", Toast.LENGTH_SHORT).show();
                break;
            default:
                Log.d("Broadcast Received", "DUNNO");
                Toast.makeText(context, "DUNNO", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}