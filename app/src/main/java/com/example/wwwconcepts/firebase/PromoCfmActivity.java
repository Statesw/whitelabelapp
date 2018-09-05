package com.example.wwwconcepts.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.POJOs.Promo;
import com.example.wwwconcepts.firebase.POJOs.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PromoCfmActivity extends AppCompatActivity {


    private TextView promoCfmTextView, promoCfmCostPtsTextView, promoCfmBalanceTextView;
    private Button yescfmBtn, cancelCfmBtn;

    private DatabaseReference promoReference, userReference;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_cfm);

        auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();

        Bundle args = getIntent().getExtras();
        String promoId = args.getString("promoId");

        promoCfmTextView = (TextView) findViewById(R.id.promoCfmTextView);
        promoCfmCostPtsTextView = (TextView) findViewById(R.id.promoCfmCostPtsTextView);
        promoCfmBalanceTextView = (TextView) findViewById(R.id.promoCfmBalanceTextView);

        promoReference = FirebaseDatabase.getInstance().getReference("promos").child(promoId);
        promoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Promo promo = dataSnapshot.getValue(Promo.class);
                promoCfmTextView.setText(promo.getDescription());
                promoCfmCostPtsTextView.setText(String.valueOf(promo.getPointsNeeded()) +" points?");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                promoCfmBalanceTextView.setText("(Current Balance: " + String.valueOf(user.getPoints())+" pts)");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        cancelCfmBtn = (Button) findViewById(R.id.cancelCfmBtn);
        cancelCfmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel now brings user to products page based on mainactivity default loading code
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
