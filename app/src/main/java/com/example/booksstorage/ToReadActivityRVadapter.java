package com.example.booksstorage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

public class ToReadActivityRVadapter extends RecyclerView.Adapter<ToReadActivityRVadapter.ToReadActivityRVHolder> {
    private Context ct;
    public ToReadActivityRVadapter(Context ct){
        this.ct = ct;
    }

    @NonNull
    @Override
    public ToReadActivityRVadapter.ToReadActivityRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.ct);
        View v = inflater.inflate(R.layout.to_read_activity_rv_item, parent, false);
        return new ToReadActivityRVadapter.ToReadActivityRVHolder(v, ct);
    }

    public static class ToReadActivityRVHolder extends RecyclerView.ViewHolder{
        ImageView ToReadActivityCover;
        RelativeLayout toReadContainer;
        Button ToReadActivityMore;
        SharedPreferences sharedPreferences;

        Context ct;

        ImageView imageView;
        public ToReadActivityRVHolder(@NonNull View itemView, Context ct){
            super(itemView);

            this.ToReadActivityCover = (ImageView) itemView.findViewById(R.id.ToReadActivityCover);
            this.toReadContainer = (RelativeLayout) itemView.findViewById(R.id.toReadContainer);
            this.ToReadActivityMore = (Button) itemView.findViewById(R.id.ToReadActivityMore);
//            this.ToReadActivityMore.setOnClickListener(this);

            this.ct = ct;
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ct);
            boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
            if (lightDark){
                Drawable darkMode = ct.getResources().getDrawable(R.drawable.border_dark_mode);
                this.toReadContainer.setBackground(darkMode);
            } else {
                Drawable lightMode = ct.getResources().getDrawable(R.drawable.border_light_mode);
                this.toReadContainer.setBackground(lightMode);
            }

            this.imageView = imageView;
        }

        public ImageView getImageViewFromHolder(){
            return this.imageView;
        }
    }







    @Override
    public void onBindViewHolder(@NonNull ToReadActivityRVadapter.ToReadActivityRVHolder holder, int position){
        holder.ToReadActivityCover.setImageBitmap(Data.getInstance().getBooksToRead().get(position).getCover());

        //next 3 lines were used for when we click on button and need to know which item from the adapter we actually clicked on,
        //we no longer need this code
//        String redString= "<font color="+ ct.getResources().getColor(R.color.button_text_color)+">More ></font>";
//        String blackString = "<font color="+ ct.getResources().getColor(R.color.blueButton)+">"+position+"</font>";
//        holder.ToReadActivityMore.setText(Html.fromHtml(redString + blackString));

        holder.ToReadActivityMore.setText("More >");
        holder.ToReadActivityMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Data.getInstance().getActivityStack().push(Data.Activity.TOREAD);

//                https://stackoverflow.com/questions/48735221/onclick-for-each-button-inside-recyclerview-items/48735328
                int chosen = holder.getAdapterPosition();
                Data.getInstance().setClickedBook(
                        Data.getInstance().getBooksToRead().get( //figure out way to create association between button and its book from API
                                chosen
                        )
                );

                Data.getInstance().setChosenRecyclerViewPosition(chosen);

                Intent show = new Intent(ct, BookDetailsActivity.class);
                //https://guides.codepath.com/android/shared-element-activity-transition#3-start-activity
                //https://stackoverflow.com/questions/40999694/cannot-use-activityoptionscompat-in-onclick-method
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)ct, (ImageView)holder.ToReadActivityCover, "transition");
                ct.startActivity(show, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksToRead().size();
    }
}