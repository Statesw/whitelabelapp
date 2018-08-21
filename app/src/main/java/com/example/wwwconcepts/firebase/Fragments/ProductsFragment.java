package com.example.wwwconcepts.firebase.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.Product;
import com.example.wwwconcepts.firebase.R;
import com.example.wwwconcepts.firebase.VolleyApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class ProductsFragment extends Fragment {

    private static final String TAG = ProductsFragment.class.getSimpleName();
    private static final String URL = "https://loginregister-8754b.firebaseio.com/products.json";

    private RecyclerView productsRecyclerView;
    private List<Product> productList;
    private StoreAdapter mAdapter;
    private CardView card_view;

    public interface OnFragmentInteractionListener {
    }


    public ProductsFragment() {
        // Required empty public constructor
    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);


        productsRecyclerView = view.findViewById(R.id.productsRecyclerView);
        productList = new ArrayList<>();
        mAdapter = new StoreAdapter(getActivity(), productList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        productsRecyclerView.setLayoutManager(mLayoutManager);
        productsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsRecyclerView.setAdapter(mAdapter);
        productsRecyclerView.setNestedScrollingEnabled(false);

        fetchStoreItems();

        return view;
    }

    private void fetchStoreItems() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
                        }.getType());

                        productList.clear();
                        productList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleyApplication.getInstance().addToRequestQueue(request);
    }



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
        private Context context;
        private List<Product> itemList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView name, price;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.titleTextView);
                price = view.findViewById(R.id.priceTextView);
                thumbnail = view.findViewById(R.id.thumbnailImageView);
            }
        }


        public StoreAdapter(Context context, List<Product> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shop_item_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Product product = itemList.get(position);
            holder.name.setText(product.getTitle());
            holder.price.setText(product.getPrice());

            Glide.with(context)
                    .load(product.getImage())
                    .into(holder.thumbnail);

            card_view = (CardView) holder.itemView.findViewById(R.id.card_view);

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView title = v.findViewById(R.id.titleTextView);
                    String titleString = title.getText().toString();
                    ProductDetailsFragment nextFrag = new ProductDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("title", titleString);
                    nextFrag.setArguments(args);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, nextFrag, "tag")
                            .addToBackStack(null)
                            .commit();

                }
            });

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

}