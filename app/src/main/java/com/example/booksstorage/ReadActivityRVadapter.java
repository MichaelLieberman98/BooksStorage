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

public class ReadActivityRVadapter extends RecyclerView.Adapter<ReadActivityRVadapter.ReadActivityRVHolder> {
    private Context ct;
    public ReadActivityRVadapter(Context ct){
        this.ct = ct;
    }

    @NonNull
    @Override
    public ReadActivityRVadapter.ReadActivityRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        System.out.println("meal item size = " + Data.getInstance().getMealItems().size());
        LayoutInflater inflater = LayoutInflater.from(this.ct);
        View v = inflater.inflate(R.layout.read_activity_rv_item, parent, false);

        return new ReadActivityRVadapter.ReadActivityRVHolder(v, ct);
    }

    public static class ReadActivityRVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ReadActivityCover;

        Button ReadActivityMore;

        Context ct;
        public ReadActivityRVHolder(@NonNull View itemView, Context ct){
            super(itemView);

            this.ReadActivityCover = (ImageView) itemView.findViewById(R.id.ReadActivityCover);

            this.ReadActivityMore = (Button) itemView.findViewById(R.id.ReadActivityMore);
            this.ReadActivityMore.setOnClickListener(this);

            this.ct = ct;
        }

        @Override
        public void onClick(View v){
            Data.getInstance().getActivityStack().push(Data.Activity.READ);

            Button b = (Button) v;
            Data.getInstance().setClickedBook(
                    Data.getInstance().getBooksAlreadyRead().get(         //figure out way to create association between button and its book from API
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
    public void onBindViewHolder(@NonNull ReadActivityRVadapter.ReadActivityRVHolder holder, int position){
        holder.ReadActivityCover.setImageBitmap(Data.getInstance().getBooksAlreadyRead().get(position).getCover());

        String redString= "<font color="+ ct.getResources().getColor(R.color.allMoreWords)+">More ></font>"; //
        String blackString = "<font color="+ ct.getResources().getColor(R.color.allRecyclerViewMoreButtonColors)+">"+position+"</font>";
        holder.ReadActivityMore.setText(Html.fromHtml(redString + blackString));
    }

    @Override
    public int getItemCount() {
        return Data.getInstance().getBooksAlreadyRead().size();
    }
}