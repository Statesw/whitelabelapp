package com.example.wwwconcepts.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wwwconcepts.firebase.POJOs.OrderItem;
import com.example.wwwconcepts.firebase.POJOs.OrderItemList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private Button paynowBtn;
    private List<OrderItem> orderitems;
    private DatabaseReference orderReference;
    private FirebaseAuth auth;
    private ListView orderItemListView;

    private TextView testTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        Bundle b = getIntent().getExtras();
        String orderId = b.getString("orderId");
        //populate listview with order>products
        orderitems = new ArrayList<>();
        orderReference = FirebaseDatabase.getInstance().getReference("orders").child(userId).child(orderId).child("products");
        orderReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderitems.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    final OrderItem orderItem = postSnapshot.getValue(OrderItem.class);
                    orderitems.add(orderItem);
                }
                final OrderItemList orderItemAdapter = new OrderItemList(CheckoutActivity.this, orderitems);
                orderItemListView = (ListView) findViewById(R.id.orderItemListView);
                orderItemListView.setAdapter(orderItemAdapter);

                setListViewHeightBasedOnChildren(orderItemListView);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        paynowBtn = (Button) findViewById(R.id.paynowBtn) ;
        paynowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Order Sent Successfully! \nThank you for shopping with White Label.", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);


                CardInputWidget mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
                Card cardToSave = mCardInputWidget.getCard();
                if (cardToSave == null) {
                    Toast.makeText(getApplicationContext(), "Error: Invalid Card Details", Toast.LENGTH_LONG).show();
                }
                else{

                cardToSave.setName("Test Name");


                Stripe stripe = new Stripe(getApplicationContext(), "pk_test_BkXSwBxbl6ddrcIQS5l4GpEz"); // stripe publishable key/ pubkey
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                //-> show that its loading

                                // Send token to your server to handle payment
                                //Somewhat like :MyServer.chargeToken(token);
                                //https://us-central1-loginregister-8754b.cloudfunctions.net/charge
                                sendRequest(token, 12);

                                //show that loading is done before changing to  next activity?
                            }
                            public void onError(Exception error) {
                                // Show localized error message notify user card error
                                Toast.makeText(getApplicationContext(),
                                        "card declined bro",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        });


                }
            }
        });


    }


    private void sendRequest(Token token, float amount){
        String currency = "SGD";
        String request = "https://us-central1-loginregister-8754b.cloudfunctions.net/charge";
        try {
            URL url= new URL("https://us-central1-loginregister-8754b.cloudfunctions.net/charge/");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.connect();

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("token", token);
            jsonParam.put("amount", amount);
            jsonParam.put("currency", currency);

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(jsonParam.toString());

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
