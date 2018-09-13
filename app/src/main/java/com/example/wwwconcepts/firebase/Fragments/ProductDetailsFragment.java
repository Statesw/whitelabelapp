package com.example.wwwconcepts.firebase.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.POJOs.Item;
import com.example.wwwconcepts.firebase.POJOs.Product;
import com.example.wwwconcepts.firebase.POJOs.Review;
import com.example.wwwconcepts.firebase.POJOs.ReviewList;
import com.example.wwwconcepts.firebase.POJOs.User;
import com.example.wwwconcepts.firebase.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String productId;

    private OnFragmentInteractionListener mListener;
    private TextView itemNameTextView, priceTextView, ratingTextView, descTextView;
    private ImageView prodDetailsImage;

    private EditText reviewEditText, quantityEditText;
    private Button reviewBtn, deleteBtn, editBtn, plusBtn, minusBtn, addToCartBtn;
    private ListView reviewsListView;
    private RatingBar mRatingBar;


    private List<Review> reviews;

    private DatabaseReference databaseReviews, productReference, userReference, cartReference;
    private FirebaseAuth auth;
    private FirebaseAnalytics firebaseAnalytics;

    private String imageUrl, priceString;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetailsFragment newInstance(String param1, String param2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        final String userId = auth.getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser.getAdmin()==true){
                    editBtn.setVisibility(View.VISIBLE);// Add edit button if user is admin
                    deleteBtn.setVisibility(View.VISIBLE);//Add delete button if user is admin
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        itemNameTextView = (TextView) view.findViewById(R.id.itemNameTextView);
        priceTextView = (TextView) view.findViewById(R.id.priceTextView);
        prodDetailsImage = (ImageView) view.findViewById(R.id.prodDetailsImage);
        Bundle bundle = getArguments();
        productId = bundle.getString("productId");
        productReference = FirebaseDatabase.getInstance().getReference().child("products").child(productId);

        if(bundle.getBoolean("refetch")){//refetch data as update product was cancelled
            productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Product product = dataSnapshot.getValue(Product.class);
                    itemNameTextView.setText(product.getTitle());
                    priceString = product.getPrice();
                    priceTextView.setText("$"+priceString);
                    imageUrl = product.getImage();
                    Glide.with(getActivity().getApplicationContext())
                            .load(imageUrl)
                            .into(prodDetailsImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else { //small optimization for loading productDetailsFragment from productsFragment
            itemNameTextView.setText(bundle.getString("title"));
            priceString=bundle.getString("price");
            priceTextView.setText("$"+priceString);
            imageUrl = bundle.getString("image");
            Glide.with(getActivity().getApplicationContext())
                    .load(imageUrl)
                    .into(prodDetailsImage);
        }


        descTextView = (TextView) view.findViewById(R.id.descTextView);

        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                descTextView.setText(product.getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        quantityEditText = (EditText) view.findViewById(R.id.quantityEditText);
        plusBtn = (Button) view.findViewById(R.id.plusBtn);
        minusBtn = (Button) view.findViewById(R.id.minusBtn);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(quantityEditText.getText().toString());
                current++;
                quantityEditText.setText(String.valueOf(current));
            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(quantityEditText.getText().toString());
                if (current != 1){
                    current--;
                    quantityEditText.setText(String.valueOf(current));
                }
            }
        });


        addToCartBtn = (Button) view.findViewById(R.id.addToCartBtn);
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create FirebaseAnalytics Instance
                firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                final Bundle analyticsBundle = new Bundle();


                //read quantity
                final int quantity = Integer.parseInt(quantityEditText.getText().toString());
                cartReference = FirebaseDatabase.getInstance().getReference().child("carts").child(userId).child(productId);

                cartReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Item item = dataSnapshot.getValue(Item.class);
                        if (item != null){ //item exists!
                            Item updatedItem = new Item(productId, item.getQuantity()+quantity);
                            cartReference.setValue(updatedItem);
                        }
                        else{ //item dont exist! create!
                            Item newItem = new Item(productId, quantity);
                            cartReference.setValue(newItem);
                        }


                        productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Product product = dataSnapshot.getValue(Product.class);

                                analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, productId);
                                analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getTitle());
                                analyticsBundle.putInt(FirebaseAnalytics.Param.QUANTITY, quantity);
                                analyticsBundle.putString(FirebaseAnalytics.Param.PRICE, product.getPrice());


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                // NOTE: Event will only show after user base hits 10+, due to privacy issue.
                // Analytics for SELECT_CONTENT was shown with only 1 user though, unsure about this
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, analyticsBundle);

                firebaseAnalytics.setMinimumSessionDuration(5000);
                //Sets whether analytics collection is enabled for this app on this device.
                firebaseAnalytics.setAnalyticsCollectionEnabled(true);

                //Sets the user ID property.
                firebaseAnalytics.setUserId(userId);

                Toast.makeText(getActivity(), "Item Added to Cart!", Toast.LENGTH_LONG).show();

            }
        });


        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        databaseReviews = productReference.child("reviews");
        reviewEditText = (EditText) view.findViewById(R.id.reviewEditText);
        reviewBtn = (Button) view.findViewById(R.id.reviewBtn);
        reviewsListView = (ListView) view.findViewById(R.id.reviewsListView);
        reviews = new ArrayList<>();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview(productId);
            }
        });



        ratingTextView = (TextView) view.findViewById(R.id.ratingTextView);
        //update reviews live
        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviews.clear();
                float overallRating =0;
                int ratingCount =0;

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Review review = postSnapshot.getValue(Review.class);


                    if(userId.equals(review.getUserId())) //for each review: check if user owns review
                        review.setOwner(true);
                    else
                        review.setOwner(false);
                    //adding to the list
                    reviews.add(review);

                    ratingCount++;
                    overallRating+= Float.valueOf(review.getRating());
                }

                if(ratingCount>0) {
                    overallRating = overallRating / ratingCount;
                    ratingTextView.setText(String.format("%.2f", overallRating));
                }
                else
                    ratingTextView.setText("-");
                TextView reviewHeadingTextView = (TextView) view.findViewById(R.id.reviewHeadingTextView);
                reviewHeadingTextView.setText("Reviews ("+ratingCount+")");


                if (getActivity()!=null) {
                    //creating adapter
                    final ReviewList reviewAdapter = new ReviewList(getActivity(), reviews);


                    //attaching adapter to the listview
                    reviewsListView.setAdapter(reviewAdapter);
//              Measure list height after drawn
                    final ViewTreeObserver vto = view.getViewTreeObserver();
                    if (vto.isAlive()) {
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override
                            public void onGlobalLayout() {
                                int totalHeight = 0;
                                for (int i = 0; i < reviewAdapter.getCount(); i++) {
                                    View listItem = reviewAdapter.getView(i, null, reviewsListView);
                                    listItem.measure(0, 0);
                                    totalHeight += listItem.getMeasuredHeight();
                                }
                                setListViewHeightBasedOnChildren(reviewsListView, totalHeight);

                                if (Build.VERSION.SDK_INT < 16) {
                                    vto.removeGlobalOnLayoutListener(this);
                                } else {
                                    vto.removeOnGlobalLayoutListener(this);
                                }
                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        deleteBtn = (Button) view.findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pop up are you sure
                AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
                adb.setTitle("Confirmation");
                adb.setMessage("Are you sure you want to delete this item? All existing data including reviews will be wiped.");
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Yes", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //remove item
                        productReference.removeValue();
                        //screen back to product page
                        ProductsFragment nextFrag = new ProductsFragment();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.frame_container, nextFrag, "remove")
                                .addToBackStack(null)
                                .commit();

                    }
                });
                adb.show();
            }
        });

        editBtn = (Button) view.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditProductFragment nextFrag = new EditProductFragment();
                Bundle args = new Bundle();
                args.putString("title", itemNameTextView.getText().toString());
                args.putString("price", priceString);
                args.putString("image", imageUrl);
                args.putString("productId", productId);
                nextFrag.setArguments(args);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, nextFrag, "edit")
                        .addToBackStack(null)
                        .commit();
            }
        });






        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /**
     * This method is saving a new artist to the
     * Firebase Realtime Database
     * */
    private void addReview(String productId){
        String reviewPost = reviewEditText.getText().toString().trim();
        String email = auth.getCurrentUser().getEmail();
        String username = auth.getCurrentUser().getDisplayName();
        String userId = auth.getCurrentUser().getUid();
        //checking if the value is provided
        if (TextUtils.isEmpty(reviewPost)) {


            //if the value is not given displaying a toast
            Toast.makeText(getActivity(), "Unable to post blank review!", Toast.LENGTH_LONG).show();

        }
        else if(mRatingBar.getRating()==0){
            Toast.makeText(getActivity(), "Please rate the product!", Toast.LENGTH_LONG).show();
        }
        else {
            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Author
            String id = databaseReviews.push().getKey();

            //creating an Review Object
            Review review = new Review(id, username, email, userId, reviewPost, productId, String.valueOf(mRatingBar.getRating()));

            //Saving the review
            databaseReviews.child(id).setValue(review);


            //Saving the review
            userReference = FirebaseDatabase.getInstance().getReference();

            //Adding to user database
            userReference.child("users").child(userId).child("reviews").child(id).setValue(review);

            //setting edittext to blank again
            reviewEditText.setText("");

            //displaying a success toast

            Toast.makeText(getActivity(), "Review added successfully!", Toast.LENGTH_LONG).show();
        }


    }

// to enable listview on scrollview
    public static void setListViewHeightBasedOnChildren(ListView listView, int measuredHeight) {
        // have to pass in measuredheight as unable to measure before view generated
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = measuredHeight;
        //calculation shifted outside to measure height after view is drawn
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += measuredHeight;
//        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }



}
