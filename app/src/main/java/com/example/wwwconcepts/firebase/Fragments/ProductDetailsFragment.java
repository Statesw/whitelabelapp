package com.example.wwwconcepts.firebase.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.POJOs.Product;
import com.example.wwwconcepts.firebase.POJOs.Review;
import com.example.wwwconcepts.firebase.POJOs.ReviewList;
import com.example.wwwconcepts.firebase.R;
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

    private OnFragmentInteractionListener mListener;
    private TextView itemNameTextView, priceTextView;
    private ImageView prodDetailsImage;

    private EditText reviewEditText;
    private Button reviewBtn;
    private ListView reviewsListView;

    private Product currentProduct;

    List<Review> reviews;

    DatabaseReference databaseReviews, productReference;
    private FirebaseAuth auth;

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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        itemNameTextView = (TextView) view.findViewById(R.id.itemNameTextView);
        priceTextView = (TextView) view.findViewById(R.id.priceTextView);
        prodDetailsImage = (ImageView) view.findViewById(R.id.prodDetailsImage);
        Bundle bundle = getArguments();
        itemNameTextView.setText(bundle.getString("title"));
        priceTextView.setText(bundle.getString("price"));
        Glide.with(getActivity().getApplicationContext())
                .load(bundle.getString("image"))
                .into(prodDetailsImage);

        String productId = bundle.getString("productId");
        productReference = FirebaseDatabase.getInstance().getReference().child("products").child(productId);



        databaseReviews = productReference.child("reviews");
        reviewEditText = (EditText) view.findViewById(R.id.reviewEditText);
        reviewBtn = (Button) view.findViewById(R.id.reviewBtn);
        reviewsListView = (ListView) view.findViewById(R.id.reviewsListView);
        reviews = new ArrayList<>();

        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview();
            }
        });


        databaseReviews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviews.clear();


                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Review review = postSnapshot.getValue(Review.class);
                    //adding to the list
                    reviews.add(review);
                }

                //creating adapter
                ReviewList reviewAdapter = new ReviewList(getActivity(), reviews);
                //attaching adapter to the listview
                reviewsListView.setAdapter(reviewAdapter);
                setListViewHeightBasedOnChildren(reviewsListView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
    private void addReview(){
        String reviewPost = reviewEditText.getText().toString().trim();
        String username = auth.getCurrentUser().getEmail();
        //checking if the value is provided
        if (!TextUtils.isEmpty(reviewPost)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            String id = databaseReviews.push().getKey();

            //creating an Artist Object
            Review review = new Review(id, username, reviewPost);

            //Saving the Artist
            databaseReviews.child(id).setValue(review);

            //setting edittext to blank again
            reviewEditText.setText("");

            //displaying a success toast

            Toast.makeText(getActivity(), "Review added successfully!", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(getActivity(), "Unable to post blank review!", Toast.LENGTH_LONG).show();
        }

    }

// to enable listview on scrollview
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
        listView.requestLayout();
    }

}
