package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class OrderItemList extends ArrayAdapter<OrderItem> {
    private Activity context;
    List<OrderItem> orderitems;
    private DatabaseReference productReference;
    private String itemName;

    private TextView orderItemNameTextView, orderItemPriceTextView, orderQuantityTextView, orderSubtotalTextView;

    public OrderItemList(Activity context, List<OrderItem> orderitems) {
        super(context, R.layout.cart_list_layout, orderitems);
        this.context = context;
        this.orderitems = orderitems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order_list_layout, null, true);

        final TextView orderItemNameTextView = (TextView) listViewItem.findViewById(R.id.orderItemNameTextView);
        orderItemPriceTextView = (TextView) listViewItem.findViewById(R.id.orderItemPriceTextView);
        orderQuantityTextView = (TextView) listViewItem.findViewById(R.id.orderQuantityTextView);
        orderSubtotalTextView = (TextView) listViewItem.findViewById(R.id.orderSubtotalTextView);
        OrderItem orderitem = orderitems.get(position);

        productReference = FirebaseDatabase.getInstance().getReference("products").child(orderitem.getProductId());
        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemName = dataSnapshot.child("title").getValue().toString();
                // not sure why: non final textview only changes last item to itemname
                orderItemNameTextView.setText(itemName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        String price = orderitem.getPrice();
        String quantity = orderitem.getQuantity();

        orderItemPriceTextView.setText("Price per unit: $"+price);
        orderQuantityTextView.setText(quantity+" unit(s) ordered");

        Float subtotal = Float.valueOf(price) * Float.valueOf(quantity);
        orderSubtotalTextView.setText("$"+String.format("%.2f", subtotal));



        return listViewItem;
    }




}