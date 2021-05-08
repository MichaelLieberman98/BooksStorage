package com.example.booksstorage;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class tempAPIAdapter extends RecyclerView.Adapter<tempAPIAdapter.APIBookItemHolder> {
    private Context ct;
    public tempAPIAdapter(Context ct){
        this.ct = ct;
    }
    @NonNull
    @Override
    public tempAPIAdapter.APIBookItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View v = inflater.inflate(R.layout.temp_api_food_holder_xml, parent, false);

        return new APIBookItemHolder(v, ct);
    }

    public static class APIBookItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

            this.tempAPImoreButton.setOnClickListener(this);

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

        @Override
        public void onClick(View v) {                                   //https://stackoverflow.com/questions/24885223/why-doesnt-recyclerview-have-onitemclicklistener
            Data.getInstance().getActivityStack().push(Data.Activity.APIRESULTS);

            Button b = (Button) v;
            Data.getInstance().setClickedBook(
                    Data.getInstance().getBooksFromAPI().get(         //figure out way to create association between button and its book from API
                            Integer.parseInt(
                                    b.getText().toString().substring(
                                            b.getText().toString().indexOf(">")+1
                                    )
                            )
                    )
            );

            Intent show = new Intent(this.ct, BookDetailsActivity.class);
            ct.startActivity(show);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull tempAPIAdapter.APIBookItemHolder holder, int position) {
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

        String redString= "<font color="+ ct.getResources().getColor(R.color.button_text_color)+">More ></font>";
        String blackString = "<font color="+ ct.getResources().getColor(R.color.blueButton)+">"+position+"</font>";

        holder.tempAPImoreButton.setText(Html.fromHtml(redString + blackString)); //we aren't really changing the text here are we...

        //https://stackoverflow.com/questions/45998111/displaying-buttons-text-with-two-different-colors

    }

    //DYNAMIC ADDING
    public void addItemToList(Book item){
        Data.getInstance().getBooksFromAPI().add(item);
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksFromAPI().size();
    }
}