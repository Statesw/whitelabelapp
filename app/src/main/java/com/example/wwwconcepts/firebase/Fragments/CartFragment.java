package com.example.wwwconcepts.firebase.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wwwconcepts.firebase.POJOs.Item;
import com.example.wwwconcepts.firebase.POJOs.ItemList;
import com.example.wwwconcepts.firebase.POJOs.Product;
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
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView cartItemListView;
    private Button checkoutBtn;
    private DatabaseReference cartsReference, productReference;
    private FirebaseAuth auth;
    private List<Item> items;
    private float subtotalCost;
    private TextView subtotalTextView, editCartTextView;
    private Boolean editModeStateTracker = false;
    int tempQuantity;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);

        auth = FirebaseAuth.getInstance();
        cartItemListView = (ListView) view.findViewById(R.id.cartItemListView);
        checkoutBtn = (Button) view.findViewById(R.id.checkoutBtn);
        items = new ArrayList<>();

        String userId = auth.getCurrentUser().getUid();
        productReference = FirebaseDatabase.getInstance().getReference("products");
        cartsReference = FirebaseDatabase.getInstance().getReference("carts").child(userId);
        productReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        items.clear();
                        int count =0;
                        subtotalCost = (float) 0.0;
//                int tempQuantity;


                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            count++;
                            //getting item reference in carts database
                            final Item item = postSnapshot.getValue(Item.class);
                            tempQuantity=item.getQuantity();
                            //getting the actual price in products database
                            final int finalTempQuantity = tempQuantity;

                            productReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    subtotalTextView = (TextView) view.findViewById(R.id.subtotalTextView);
//                            subtotalCost = Float.valueOf(subtotalTextView.getText().toString());
                                    Product product = dataSnapshot.child(item.getProductId()).getValue(Product.class);
                                    float tempCost = Float.valueOf(product.getPrice());
                                    subtotalCost+= (tempCost * (finalTempQuantity));
                                    subtotalTextView.setText(String.format("%.2f", subtotalCost));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //adding to the list
                            items.add(item);
                        }


                        if (getActivity()!=null){
                            final ItemList itemAdapter = new ItemList(getActivity(), items);

                            editCartTextView = (TextView) view.findViewById(R.id.editCartTextView);
                            if(editModeStateTracker)
                                itemAdapter.setEditMode(true);
                            else
                                itemAdapter.setEditMode(false);

                            editCartTextView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(editModeStateTracker){
                                        itemAdapter.setEditMode(false);
                                        editModeStateTracker=!editModeStateTracker;
                                        editCartTextView.setText("Edit");
                                    }
                                    else {
                                        itemAdapter.setEditMode(true);
                                        editModeStateTracker=!editModeStateTracker;
                                        editCartTextView.setText("Done");
                                    }
                                }
                            });

                            cartItemListView.setAdapter(itemAdapter);}

                            if (count ==0) {// if no items exist in cart
                                subtotalTextView = (TextView) view.findViewById(R.id.subtotalTextView);
                                subtotalTextView.setText("0.0");
                                Button checkoutBtn = (Button) view.findViewById(R.id.checkoutBtn);
                                checkoutBtn.setClickable(false);
                            }
                            else{
                                Button checkoutBtn = (Button) view.findViewById(R.id.checkoutBtn);
                                checkoutBtn.setClickable(true);
                            }
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









        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
}
