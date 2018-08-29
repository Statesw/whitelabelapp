package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


public class ItemList extends ArrayAdapter<Item> {
    private Activity context;
    List<Item> items;
    private DatabaseReference productReference;
    private Product product;
    private float itemCost;

    public ItemList(Activity context, List<Item> items) {
        super(context, R.layout.cart_list_layout, items);
        this.context = context;
        this.items = items;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.cart_list_layout, null, true);

        final TextView cartItemTextView = (TextView) listViewItem.findViewById(R.id.cartItemTextView);
        final TextView quantityTextView = (TextView) listViewItem.findViewById(R.id.quantityTextView);
        final TextView costTextView = (TextView) listViewItem.findViewById(R.id.costTextView);
        final ImageView cartItemImageView = (ImageView) listViewItem.findViewById(R.id.cartItemImageView);

        final Item item = items.get(position);

        productReference = FirebaseDatabase.getInstance().getReference("products");
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                product = dataSnapshot.child(item.getProductId()).getValue(Product.class);
                Glide.with(context)
                        .load(product.getImage())
                        .into(cartItemImageView);
                cartItemTextView.setText(product.getTitle());
                quantityTextView.setText("Quantity: "+ item.getQuantity());


                itemCost = Float.valueOf(product.getPrice()) * Float.valueOf(item.getQuantity());
                costTextView.setText(String.format("%.2f", itemCost));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return listViewItem;
    }
}