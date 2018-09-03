package com.example.wwwconcepts.firebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

public class CheckoutActivity extends AppCompatActivity {

    private Button paynowBtn;

    private TextView testTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

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


                Stripe stripe = new Stripe(getApplicationContext(), "pk_test_BkXSwBxbl6ddrcIQS5l4GpEz");
                stripe.createToken(
                        cardToSave,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server to handle payment
                                testTV = (TextView) findViewById(R.id.testTV);
                                testTV.setText(token.toString());


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
}
