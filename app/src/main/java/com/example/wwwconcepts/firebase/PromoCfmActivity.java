package com.example.wwwconcepts.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button yescfmBtn, cancelCfmBtn, removePromoBtn;

    private DatabaseReference promoReference, userReference;
    private FirebaseAuth auth;

    private int costPts, currentPts;

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

        removePromoBtn = (Button) findViewById(R.id.removePromoBtn);

        promoReference = FirebaseDatabase.getInstance().getReference("promos").child(promoId);
        promoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Promo promo = dataSnapshot.getValue(Promo.class);
                promoCfmTextView.setText(promo.getDescription());
                costPts = promo.getPointsNeeded();
                promoCfmCostPtsTextView.setText(String.valueOf(costPts) +" points?");
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
                currentPts = user.getPoints();
                promoCfmBalanceTextView.setText("(Current Balance: " + String.valueOf(currentPts)+" pts)");
                if(user.getAdmin()==true){
                    removePromoBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        yescfmBtn = (Button) findViewById(R.id.yesCfmBtn);
        yescfmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPts>=costPts){
                currentPts-=costPts;
                userReference.child("points").setValue(currentPts);
                }
                else{
                    Toast.makeText(PromoCfmActivity.this, "Sorry, you do not have enough points to redeem this!", Toast.LENGTH_SHORT).show();
                }
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


        removePromoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promoReference.removeValue();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
