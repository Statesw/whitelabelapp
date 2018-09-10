package com.example.wwwconcepts.firebase.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wwwconcepts.firebase.Constants;
import com.example.wwwconcepts.firebase.POJOs.Product;
import com.example.wwwconcepts.firebase.POJOs.User;
import com.example.wwwconcepts.firebase.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ProductsFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private List<Product> productList;
    private StoreAdapter mAdapter, filteredAdapter;
    private CardView card_view;
    private EditText titleEditText, priceEditText, searchEditText;
    private Boolean isAdmin;


    private List<Product> products;

    private DatabaseReference databaseReference, userReference;

    private FirebaseAuth auth;
    private FirebaseAnalytics firebaseAnalytics;

    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private Button chooseBtn;
    private Button uploadBtn;
    private ImageView uploadImageView;

    //uri to store file
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;

    //new product: empty review list
    HashMap<String, HashMap<String,String>> reviews;

    //switch to hide add options
    private Switch editSw;


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

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        productsRecyclerView.setLayoutManager(mLayoutManager);
        productsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(8), true));
        productsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        productsRecyclerView.setNestedScrollingEnabled(false);

//        fetchStoreItems();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Product product = postSnapshot.getValue(Product.class);
                    //adding to the list
                    productList.add(product);


                }

                //creating adapter
//                ProductList productAdapter = new ProductList(getActivity(), products);
                StoreAdapter productAdapter = new StoreAdapter(getActivity(), productList);
                //attaching adapter to the listview
                productsRecyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        titleEditText = view.findViewById(R.id.titleEditText);
        priceEditText = view.findViewById(R.id.priceEditText);


        editSw = (Switch) view.findViewById(R.id.editSw);
        uploadBtn = (Button) view.findViewById(R.id.uploadBtn);
        chooseBtn = (Button) view.findViewById(R.id.chooseBtn);
        uploadImageView = (ImageView) view.findViewById(R.id.uploadImageView);
        storageReference = FirebaseStorage.getInstance().getReference();//edit for : get reference to any upload photo folder

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if (currentUser.getAdmin()==true){
                    editSw.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        editSw.setChecked(false); // off by default
        editSw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editSw.isChecked()){
                    titleEditText.setVisibility(View.GONE);
                    priceEditText.setVisibility(View.GONE);
                    chooseBtn.setVisibility(View.GONE);
                    uploadBtn.setVisibility(View.GONE);
                    uploadImageView.setVisibility(View.GONE);
                }
                else{
                    titleEditText.setVisibility(View.VISIBLE);
                    priceEditText.setVisibility(View.VISIBLE);
                    chooseBtn.setVisibility(View.VISIBLE);
                    uploadBtn.setVisibility(View.VISIBLE);
                    uploadImageView.setVisibility(View.VISIBLE);
                }
            }
        });



        searchEditText = (EditText) view.findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString()!=null)
                    filter(s.toString());

            }
        });


        return view;
    }

//    private void fetchStoreItems() {
//        JsonArrayRequest request = new JsonArrayRequest(URL,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        if (response == null) {
//                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        List<Product> items = new Gson().fromJson(response.toString(), new TypeToken<List<Product>>() {
//                        }.getType());
//
//                        productList.clear();
//                        productList.addAll(items);
//
//                        // refreshing recycler view
//                        mAdapter.notifyDataSetChanged();
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // error in getting json
//                Log.e(TAG, "Error: " + error.getMessage());
//                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        VolleyApplication.getInstance().addToRequestQueue(request);
//    }



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
            //Create FirebaseAnalytics Instance
            firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            auth = FirebaseAuth.getInstance();
            final String userId = auth.getCurrentUser().getUid();
            final Bundle analyticsBundle = new Bundle();

            final Product product = itemList.get(position);
            holder.name.setText(product.getTitle());
            holder.price.setText("$"+product.getPrice());

            Glide.with(context)
                    .load(product.getImage())
                    .into(holder.thumbnail);

            card_view = (CardView) holder.itemView.findViewById(R.id.card_view);

            card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ProductDetailsFragment nextFrag = new ProductDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("title", product.getTitle());
                    args.putString("price", product.getPrice());
                    args.putString("image", product.getImage());
                    args.putString("productId", product.getProductId());
                    args.putBoolean("refetch", false);
                    nextFrag.setArguments(args);

                    analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_ID, product.getProductId());
                    analyticsBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, product.getTitle());

                    firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, analyticsBundle);

                    firebaseAnalytics.setMinimumSessionDuration(5000);
                    //Sets whether analytics collection is enabled for this app on this device.
                    firebaseAnalytics.setAnalyticsCollectionEnabled(true);

                    //Sets the user ID property.
                    firebaseAnalytics.setUserId(userId);


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


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                uploadImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(Constants.PRODUCT_STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();

                            //displaying success toast
                            Toast.makeText(getActivity().getApplicationContext(), "Product Added! ", Toast.LENGTH_LONG).show();

                            //adding an upload to firebase database
                            String uploadId = databaseReference.push().getKey();

//                            reviews = new ArrayList<>();

                            //creating the upload object to store uploaded image details
                            Product product = new Product(titleEditText.getText().toString().trim(), taskSnapshot.getDownloadUrl().toString(), priceEditText.getText().toString(), uploadId, reviews);


                            databaseReference.child(uploadId).setValue(product);

                            titleEditText.setText("");
                            priceEditText.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }


    private void filter(final String text){
        List<Product> filteredProducts= new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting
                    Product product = postSnapshot.getValue(Product.class);
                    //adding to the list
                    if(product.getTitle().toLowerCase().contains(text.toLowerCase().trim())) {
                        productList.add(product);
                    }

                }

                //creating adapter
//                ProductList productAdapter = new ProductList(getActivity(), products);
                StoreAdapter productAdapter = new StoreAdapter(getActivity(), productList);
                //attaching adapter to the listview
                productsRecyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }

}