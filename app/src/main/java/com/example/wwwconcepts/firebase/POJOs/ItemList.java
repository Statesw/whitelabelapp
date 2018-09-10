package com.example.wwwconcepts.firebase.POJOs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ItemList extends ArrayAdapter<Item> {
    private Activity context;
    List<Item> items;
    private DatabaseReference productReference, cartReference;
    private FirebaseAuth auth;
    private Product product;
    private float itemCost;
    public Boolean editMode=false;
    public ImageButton deleteCartBtn;
    private Button qAddBtn, qMinusBtn;

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
        final TextView numberTextView = (TextView) listViewItem.findViewById(R.id.numberTextView);
        final TextView costTextView = (TextView) listViewItem.findViewById(R.id.costTextView);
        final ImageView cartItemImageView = (ImageView) listViewItem.findViewById(R.id.cartItemImageView);

        deleteCartBtn = (ImageButton) listViewItem.findViewById(R.id.deleteCartBtn);
        qAddBtn = (Button) listViewItem.findViewById(R.id.qAddBtn);
        qMinusBtn = (Button) listViewItem.findViewById(R.id.qMinusBtn);
        deleteCartBtn.setVisibility( (editMode ? View.VISIBLE : View.GONE));
        qAddBtn.setVisibility(editMode ? View.VISIBLE : View.GONE);
        qMinusBtn.setVisibility(editMode ? View.VISIBLE : View.GONE);

        Item item = items.get(position);
        final int finalQuantity = item.getQuantity();
        final Item finalItem = items.get(position);

        productReference = FirebaseDatabase.getInstance().getReference("products");
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                product = dataSnapshot.child(finalItem.getProductId()).getValue(Product.class);
                Glide.with(context)
                        .load(product.getImage())
                        .into(cartItemImageView);
                cartItemTextView.setText(product.getTitle());
                numberTextView.setText(String.valueOf(finalItem.getQuantity()));


                itemCost = Float.valueOf(product.getPrice()) * Float.valueOf(finalItem.getQuantity());
                costTextView.setText(String.format("%.2f", itemCost));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        auth = FirebaseAuth.getInstance();
        final String uid = auth.getCurrentUser().getUid();

        deleteCartBtn.setTag(position);
        deleteCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Item item = items.get(position);
                cartReference = FirebaseDatabase.getInstance().getReference("carts").child(uid).child(item.getProductId());

                AlertDialog.Builder adb = new AlertDialog.Builder(context);
                adb.setTitle("Confirmation");
                adb.setMessage("Are you sure you want to delete this item from your cart?");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remove item

                        cartReference.removeValue();

                    }
                });
                adb.show();

            }
        });
        qAddBtn.setTag(position);
        qAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Item item = items.get(position);
                cartReference = FirebaseDatabase.getInstance().getReference("carts").child(uid).child(item.getProductId());
                int quantity = finalQuantity;
                quantity++;
                cartReference.child("quantity").setValue(quantity);

//
//                int quantity = Integer.valueOf(numberTextView.getText().toString());
//                quantity++;
//                numberTextView.setText(String.valueOf(quantity));
            }
        });

        qMinusBtn.setTag(position);
        qMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int)v.getTag();
                Item item = items.get(position);
                cartReference = FirebaseDatabase.getInstance().getReference("carts").child(uid).child(item.getProductId());
                int quantity = finalQuantity;
                if(quantity!=1)
                    quantity--;
                cartReference.child("quantity").setValue(quantity);
            }
        });

        return listViewItem;
    }


    public void setEditMode(Boolean state){
        this.editMode = state;
        this.notifyDataSetChanged();
    }

}