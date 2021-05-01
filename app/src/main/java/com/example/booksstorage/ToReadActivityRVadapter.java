package com.example.booksstorage;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ToReadActivityRVadapter extends RecyclerView.Adapter<ToReadActivityRVadapter.ToReadActivityRVHolder> {
    private Context ct;
    public ToReadActivityRVadapter(Context ct){
        this.ct = ct;
    }

    @NonNull
    @Override
    public ToReadActivityRVadapter.ToReadActivityRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        System.out.println("meal item size = " + Data.getInstance().getMealItems().size());
        LayoutInflater inflater = LayoutInflater.from(this.ct);
        View v = inflater.inflate(R.layout.to_read_activity_rv_item, parent, false);

        return new ToReadActivityRVadapter.ToReadActivityRVHolder(v, ct);
    }

    public static class ToReadActivityRVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ToReadActivityCover;

        Button ToReadActivityMore;

        Context ct;
        public ToReadActivityRVHolder(@NonNull View itemView, Context ct){
            super(itemView);

            this.ToReadActivityCover = (ImageView) itemView.findViewById(R.id.ToReadActivityCover);

            this.ToReadActivityMore = (Button) itemView.findViewById(R.id.ToReadActivityMore);
            this.ToReadActivityMore.setOnClickListener(this);

            this.ct = ct;
        }

        @Override
        public void onClick(View v){
            Data.getInstance().getActivityStack().push(Data.Activity.TOREAD);

            Button b = (Button) v;
            Data.getInstance().setClickedBook(
                    Data.getInstance().getBooksToRead().get(         //figure out way to create association between button and its book from API
                            Integer.parseInt(
                                    b.getText().toString().substring(
                                            b.getText().toString().indexOf(">")+1
                                    )
                            )
                    )
            );

            Intent show = new Intent(this.ct, BookDetailsActivity.class);
            this.ct.startActivity(show);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ToReadActivityRVadapter.ToReadActivityRVHolder holder, int position){
        holder.ToReadActivityCover.setImageBitmap(Data.getInstance().getBooksToRead().get(position).getCover());

        String redString= "<font color="+ ct.getResources().getColor(R.color.allMoreWords)+">More ></font>"; //
        String blackString = "<font color="+ ct.getResources().getColor(R.color.allRecyclerViewMoreButtonColors)+">"+position+"</font>";

        holder.ToReadActivityMore.setText(Html.fromHtml(redString + blackString));
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksToRead().size();
    }
}