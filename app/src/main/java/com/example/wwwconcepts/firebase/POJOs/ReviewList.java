package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ReviewList extends ArrayAdapter<Review> {
    private Activity context;
    List<Review> reviews;
    private DatabaseReference reviewReference, userRecordReference, adminCheckReference;

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

        Review review = reviews.get(position);

        userRecordReference = FirebaseDatabase.getInstance().getReference().child("users").child(review.getUserId()).child("reviews").child(review.getReviewId());

        textViewName.setText("by " + review.getReviewAuthorName() +" (" + review.getReviewAuthorEmail() + ")");
        textViewGenre.setText(review.getReviewPost());

        final ImageButton reviewDeleteBtn = (ImageButton) listViewItem.findViewById(R.id.reviewDeleteBtn);

        reviewDeleteBtn.setTag(position);
        reviewDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Review review = reviews.get(position);
                reviewReference = FirebaseDatabase.getInstance().getReference().child("products").child(review.getProductId()).child("reviews").child(review.getReviewId());
                //pop up are you sure
                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Confirmation");
                adb.setMessage("Are you sure you want to delete this comment?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remove item

                        reviewReference.removeValue();
                        userRecordReference.removeValue();

                    }
                });
                adb.show();
            }
        });


        if(review.getOwner()==true)
            reviewDeleteBtn.setVisibility(View.VISIBLE);
        else
            reviewDeleteBtn.setVisibility(View.GONE);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        adminCheckReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        adminCheckReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.getAdmin()==true){
                    reviewDeleteBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        return listViewItem;
    }
}