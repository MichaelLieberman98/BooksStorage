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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class tempAPIAdapter extends RecyclerView.Adapter<tempAPIAdapter.APIBookItemHolder> {
    private Context ct;
    private int highestBookCount = 0;

    private tempAPIAdapter.APIBookItemHolder holder;
    public tempAPIAdapter(Context ct){
        System.out.println("apiadapter FB API SIZE RN = " + Data.getInstance().getBooksFromAPI().size());
        this.ct = ct;
    }
    @NonNull
    @Override
    public tempAPIAdapter.APIBookItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View v = inflater.inflate(R.layout.temp_api_food_holder_xml, parent, false);

        return new APIBookItemHolder(v, ct);
    }

    public static class APIBookItemHolder extends RecyclerView.ViewHolder{
        ImageView tempAPIcover;
        RelativeLayout resultsContainer;
        TextView tempAPItitle;
        TextView tempAPIauthors;
        TextView tempAPIpublishDate;
        TextView tempAPIpublishers;
        Button tempAPImoreButton;
        SharedPreferences sharedPreferences;

        Context ct;
        public APIBookItemHolder(@NonNull View itemView, Context ct) {
            super(itemView);

            this.tempAPIcover = itemView.findViewById(R.id.tempAPIcover);
            this.resultsContainer = itemView.findViewById(R.id.resultsContainer);
            this.tempAPItitle = itemView.findViewById(R.id.tempAPItitle);
            this.tempAPIauthors = itemView.findViewById(R.id.tempAPIauthors);
            this.tempAPIpublishDate = itemView.findViewById(R.id.tempAPIpublishDate);
            this.tempAPIpublishers = itemView.findViewById(R.id.tempAPIpublishers);
            this.tempAPImoreButton = itemView.findViewById(R.id.tempAPImoreButton);

            this.ct = ct;
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ct);
            boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
            if (lightDark){
                Drawable darkMode = ct.getResources().getDrawable(R.drawable.border_dark_mode);
                this.resultsContainer.setBackground(darkMode);
                int textColor = ct.getResources().getColor(R.color.button_text_color);
                this.tempAPItitle.setTextColor(textColor);
                this.tempAPIauthors.setTextColor(textColor);
                this.tempAPIpublishDate.setTextColor(textColor);
                this.tempAPIpublishers.setTextColor(textColor);

            } else {
                Drawable lightMode = ct.getResources().getDrawable(R.drawable.border_light_mode);
                this.resultsContainer.setBackground(lightMode);
            }
        }

//        @Override
//        public void onClick(View v) {                                   //https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
//            Data.getInstance().setChoseAPIBook(true);
//            Data.getInstance().getActivityStack().push(Data.Activity.APIRESULTS);
//
//            Button b = (Button) v;
//            int chosen = Integer.parseInt(
//                    b.getText().toString().substring(
//                            b.getText().toString().indexOf(">")+1
//                    )
//            );
//            Data.getInstance().setClickedBook(
//                    Data.getInstance().getBooksFromAPI().get( //figure out way to create association between button and its book from API
//                            b.
//                    )
//            );
//
//            Data.getInstance().setChosenRecyclerViewPosition(chosen);
//
//            Intent show = new Intent(this.ct, BookDetailsActivity.class);
//            //https://guides.codepath.com/android/shared-element-activity-transition#3-start-activity
//            //https://stackoverflow.com/questions/40999694/cannot-use-activityoptionscompat-in-onclick-method
//            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)this.ct, this.tempAPIcover, "transition");
//            ct.startActivity(show, options.toBundle());
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull tempAPIAdapter.APIBookItemHolder holder, int position) {
        this.holder = holder;

        System.out.println("adpater bind FB API SIZE RN = " + Data.getInstance().getBooksFromAPI().size());

        holder.tempAPIcover.setImageBitmap(Data.getInstance().getBooksFromAPI().get(position).getCover());

        holder.tempAPItitle.setText(Data.getInstance().getBooksFromAPI().get(position).getTitle());

        ArrayList<String> authors = Data.getInstance().getBooksFromAPI().get(position).getAuthors();

        String authorsAsString = "";
        switch (authors.size()){
            case 0:
                authorsAsString = "no authors available";
                break;
            case 1:
                authorsAsString = authors.get(0);
                break;
            case 2:
                authorsAsString = authors.get(0) + "\n" + authors.get(1);
                break;
            default:
                for (int i = 0; i < authors.size()-1; i++){
                    authorsAsString += (authors.get(i) + "\n");
                }
                authorsAsString += authors.get(authors.size()-1);
                break;
        }
        holder.tempAPIauthors.setText(authorsAsString);


        if (Data.getInstance().getBooksFromAPI().get(position).getPublishDate().getDay() == -1 &&
                Data.getInstance().getBooksFromAPI().get(position).getPublishDate().getMonth() == -1){
            holder.tempAPIpublishDate.setText(Data.getInstance().getBooksFromAPI().get(position).getPublishDate().numsDateJustYear());
        } else if (Data.getInstance().getBooksFromAPI().get(position).getPublishDate().getDay() == -1){
            holder.tempAPIpublishDate.setText(Data.getInstance().getBooksFromAPI().get(position).getPublishDate().numsDateNoDay());
        } else {
            holder.tempAPIpublishDate.setText(Data.getInstance().getBooksFromAPI().get(position).getPublishDate().numsDate());
        }

        holder.tempAPIpublishers.setText(Data.getInstance().getBooksFromAPI().get(position).getPublisher());

        //next 3 lines were used for when we click on button and need to know which item from the adapter we actually clicked on,
        //we no longer need this code
//        String redString= "<font color="+ ct.getResources().getColor(R.color.button_text_color)+">More ></font>";
//        String blackString = "<font color="+ ct.getResources().getColor(R.color.blueButton)+">"+position+"</font>";
//        holder.tempAPImoreButton.setText(Html.fromHtml(redString + blackString)); //we aren't really changing the text here are we...

        holder.tempAPImoreButton.setText("More >");
        holder.tempAPImoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Data.getInstance().setChoseAPIBook(true);
                Data.getInstance().getActivityStack().push(Data.Activity.APIRESULTS);

//                https://stackoverflow.com/questions/48735221/onclick-for-each-button-inside-recyclerview-items/48735328
                int chosen = holder.getAdapterPosition();
                Data.getInstance().setClickedBook(
                        Data.getInstance().getBooksFromAPI().get( //figure out way to create association between button and its book from API
                                chosen
                        )
                );

                Data.getInstance().setChosenRecyclerViewPosition(chosen);

                Intent show = new Intent(ct, BookDetailsActivity.class);
                //https://guides.codepath.com/android/shared-element-activity-transition#3-start-activity
                //https://stackoverflow.com/questions/40999694/cannot-use-activityoptionscompat-in-onclick-method
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)ct, (ImageView)holder.tempAPIcover, "transition");
                ct.startActivity(show, options.toBundle());
            }
        });

        //https://stackoverflow.com/questions/45998111/displaying-buttons-text-with-two-different-colors

        highestBookCount++;
        if (position == this.highestBookCount - 1){
//        setFadeAnimation(holder.itemView);
            setScaleAnimation(holder.itemView);
        }
    }

    public void setFadeAnimation(View view){ //https://stackoverflow.com/questions/26724964/how-to-animate-recyclerview-items-when-they-appear
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) { //https://stackoverflow.com/questions/26724964/how-to-animate-recyclerview-items-when-they-appear
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    //DYNAMIC ADDING https://stackoverflow.com/questions/51261653/i-have-to-display-one-item-in-my-recyclerview-after-every-2-seconds
    public void addItemToList(Book item){
        Data.getInstance().getBooksFromAPI().add(item);
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksFromAPI().size();
    }
}