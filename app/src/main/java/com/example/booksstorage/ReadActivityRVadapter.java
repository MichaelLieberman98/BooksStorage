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

public class ReadActivityRVadapter extends RecyclerView.Adapter<ReadActivityRVadapter.ReadActivityRVHolder> {
    private Context ct;

    public ReadActivityRVadapter(Context ct){
        this.ct = ct;
    }

    @NonNull
    @Override
    public ReadActivityRVadapter.ReadActivityRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.ct);
        View v = inflater.inflate(R.layout.read_activity_rv_item, parent, false);

        return new ReadActivityRVadapter.ReadActivityRVHolder(v, ct);
    }

    public static class ReadActivityRVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ReadActivityCover;
        RelativeLayout readContainer;
        Button ReadActivityMore;
        SharedPreferences sharedPreferences;

        Context ct;
        public ReadActivityRVHolder(@NonNull View itemView, Context ct){
            super(itemView);

            this.ReadActivityCover = (ImageView) itemView.findViewById(R.id.ReadActivityCover);
            this.readContainer = (RelativeLayout) itemView.findViewById(R.id.readContainer);
            this.ReadActivityMore = (Button) itemView.findViewById(R.id.ReadActivityMore);
            this.ReadActivityMore.setOnClickListener(this);

            this.ct = ct;
            this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ct);
            boolean lightDark = sharedPreferences.getBoolean("lightdark", true);
            if (lightDark){
                Drawable darkMode = ct.getResources().getDrawable(R.drawable.border_dark_mode);
                this.readContainer.setBackground(darkMode);
            } else {
                Drawable lightMode = ct.getResources().getDrawable(R.drawable.border_light_mode);
                this.readContainer.setBackground(lightMode);
            }
        }

        @Override
        public void onClick(View v){
            Data.getInstance().getActivityStack().push(Data.Activity.READ);

            Button b = (Button) v;
            int chosen = Integer.parseInt(
                    b.getText().toString().substring(
                            b.getText().toString().indexOf(">")+1
                    )
            );
            Data.getInstance().setClickedBook(
                    Data.getInstance().getBooksAlreadyRead().get( //figure out way to create association between button and its book
                            Integer.parseInt(
                                    b.getText().toString().substring(
                                            b.getText().toString().indexOf(">")+1
                                    )
                            )
                    )
            );
            Data.getInstance().setChosenRecyclerViewPosition(chosen);

            Intent show = new Intent(this.ct, BookDetailsActivity.class);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)this.ct, this.ReadActivityCover, "transition");
            this.ct.startActivity(show, options.toBundle());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReadActivityRVadapter.ReadActivityRVHolder holder, int position){
        holder.ReadActivityCover.setImageBitmap(Data.getInstance().getBooksAlreadyRead().get(position).getCover());

        String redString= "<font color="+ ct.getResources().getColor(R.color.button_text_color)+">More ></font>"; //
        String blackString = "<font color="+ ct.getResources().getColor(R.color.blueButton)+">"+position+"</font>";
        holder.ReadActivityMore.setText(Html.fromHtml(redString + blackString));
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksAlreadyRead().size();
    }
}