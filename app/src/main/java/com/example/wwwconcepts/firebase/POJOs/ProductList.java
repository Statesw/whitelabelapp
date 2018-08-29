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

import java.util.List;


public class ProductList extends ArrayAdapter<Product> {
    private Activity context;
    List<Product> products;

    public ProductList(Activity context, List<Product> products) {
        super(context, R.layout.shop_item_row, products);
        this.context = context;
        this.products = products;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.shop_item_row, null, true);

        TextView titleTextView = (TextView) listViewItem.findViewById(R.id.titleTextView);
        TextView priceTextView = (TextView) listViewItem.findViewById(R.id.priceTextView);
        ImageView thumbnailImageView = (ImageView) listViewItem.findViewById(R.id.thumbnailImageView);


        Product product = products.get(position);
        Glide.with(context)
                .load(product.getImage())
                .into(thumbnailImageView);
        titleTextView.setText(product.getTitle());
        priceTextView.setText("$"+product.getPrice());


        return listViewItem;
    }
}