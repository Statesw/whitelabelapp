package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NewsList extends ArrayAdapter<News> {
    private Activity context;
    List<News> newslist;
    private DatabaseReference newsReference, deleteNewsReference;
    private News news;


    public NewsList (Activity context, List<News> newslist){
        super(context, R.layout.news_layout_list, newslist);
        this.context = context;
        this.newslist = newslist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.news_layout_list, null, true);

        final TextView newsTitleTextView = (TextView) listViewItem.findViewById(R.id.newsTitleTextView);
        final TextView newsBodyTextView = (TextView) listViewItem.findViewById(R.id.newsBodyTextView);
        final ImageView newsItemImageView = (ImageView) listViewItem.findViewById(R.id.newsItemImageView);
        final ImageButton deleteNewsBtn = (ImageButton) listViewItem.findViewById(R.id.deleteNewsBtn);

        final News finalnews = newslist.get(position);

        newsReference = FirebaseDatabase.getInstance().getReference("news");
        newsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                news = dataSnapshot.child(finalnews.getNewsId()).getValue(News.class);
                Glide.with(context)
                        .load(news.getImageUrl())
                        .into(newsItemImageView);
                newsTitleTextView.setText(news.getTitle());
                newsBodyTextView.setText(news.getBody());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        deleteNewsBtn.setTag(position);
        deleteNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                News news = newslist.get(position);
                deleteNewsReference = newsReference.child(news.getNewsId());

                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Confirmation");
                adb.setMessage("Are you sure you want to delete news item?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remove item

                        deleteNewsReference.removeValue();

                    }
                });
                adb.show();
            }
        });


        return listViewItem;
    }

}
