package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.R;

import java.util.List;


public class ReviewList extends ArrayAdapter<Review> {
    private Activity context;
    List<Review> reviews;

    public ReviewList(Activity context, List<Review> reviews) {
        super(context, R.layout.reviews_layout_list, reviews);
        this.context = context;
        this.reviews = reviews;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.reviews_layout_list, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.authorTextView);
        TextView textViewGenre = (TextView) listViewItem.findViewById(R.id.postTextView);

        Review artist = reviews.get(position);
        textViewName.setText(artist.getReviewAuthorName());
        textViewGenre.setText(artist.getReviewPost());

        return listViewItem;
    }
}