package com.example.wwwconcepts.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    private Button signOutBtn;
    private Button testBtn;
    private FirebaseAuth auth;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        DatabaseReference aDatabase = FirebaseDatabase.getInstance().getReference("products");

//        // Creating new user node, which returns the unique key value: push adds a unique key item on the list
//        // new user node would be /products/$productId/
//                 String productId = aDatabase.push().getKey();
//
//        // creating user object
//                    Product product = new Product("Title", "imageurl", "$2");
//        // pushing product to 'products' node using the userId
//                    aDatabase.child(productId).setValue(product);

//        DatabaseReference testDatabase = aDatabase.child("test").child("2t").child("3t");
//        testDatabase.setValue("item");


        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });


        testBtn = (Button) findViewById(R.id.testBtn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref = db.getReference("users").child("test").child("a"); // Key


                // Attach listener
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Retrieve latest value
                        String test = dataSnapshot.getValue(String.class);
                        EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
                        titleEditText.setText(test);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Error handling
                        EditText titleEditText = (EditText) findViewById(R.id.titleEditText);
                        titleEditText.setText("Error");
                    }
                });

            }
        });




    }

}



