package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.R;

import java.util.List;


public class OrderList extends ArrayAdapter<Order> {
    private Activity context;
    List<Order> orders;


    public OrderList(Activity context, List<Order> orders) {
        super(context, R.layout.order_history_layout, orders);
        this.context = context;
        this.orders = orders;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.order_history_layout, null, true);

        Order order = orders.get(position);

        final TextView orderIdTextView = (TextView) listViewItem.findViewById(R.id.orderIdTextView);
        final TextView orderDateTextView = (TextView) listViewItem.findViewById(R.id.orderDateTextView);
        final TextView payStatusTextView = (TextView) listViewItem.findViewById(R.id.payStatusTextView);


        orderIdTextView.setText("Order #: " + order.getOrderId());
        orderDateTextView.setText(order.getDate());
        payStatusTextView.setText(order.getPayStatus().toUpperCase());



        return listViewItem;
    }


    public Order getItem(int position){
        return orders.get(position);
    }


}