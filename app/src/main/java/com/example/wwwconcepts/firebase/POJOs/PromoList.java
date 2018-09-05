package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.R;

import java.util.List;


public class PromoList extends ArrayAdapter<Promo> {
    private Activity context;
    List<Promo> promos;

    public PromoList(Activity context, List<Promo> promos) {
        super(context, R.layout.promo_list_layout, promos);
        this.context = context;
        this.promos = promos;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.promo_list_layout, null, true);

        final TextView promoDescTextView = listViewItem.findViewById(R.id.promoDescTextView);
        final TextView pointsTextView = listViewItem.findViewById(R.id.ptCostTextView);

        Promo promo = promos.get(position);
        promoDescTextView.setText(promo.getDescription());
        pointsTextView.setText(String.valueOf(promo.getPointsNeeded())+" pts");


        return listViewItem;
    }

    public Promo getItem(int position){
        return promos.get(position);
    }

}