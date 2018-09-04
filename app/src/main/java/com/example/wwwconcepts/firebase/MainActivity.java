package com.example.wwwconcepts.firebase;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.wwwconcepts.firebase.Fragments.AllOrdersFragment;
import com.example.wwwconcepts.firebase.Fragments.CartFragment;
import com.example.wwwconcepts.firebase.Fragments.EditProductFragment;
import com.example.wwwconcepts.firebase.Fragments.ProductDetailsFragment;
import com.example.wwwconcepts.firebase.Fragments.ProductsFragment;
import com.example.wwwconcepts.firebase.Fragments.ProfileFragment;
import com.example.wwwconcepts.firebase.Fragments.PromotionsFragment;


public class MainActivity extends AppCompatActivity implements ProductDetailsFragment.OnFragmentInteractionListener,
        ProductsFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        PromotionsFragment.OnFragmentInteractionListener,
        EditProductFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        AllOrdersFragment.OnFragmentInteractionListener{

//    private ActionBar toolbar;
private Toolbar toolbar;

    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Attaching the layout to the toolbar object
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
//        toolbar = getSupportActionBar();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);




        toolbar.setTitle("White Label");
        loadFragment(new ProductsFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
           //cart on click
            Fragment fragment = new CartFragment();
            loadFragment(fragment);
            toolbar.setTitle("My Cart");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemReselectedListener mOnNavigationItemReselectedListener = new BottomNavigationView.OnNavigationItemReselectedListener() {
        @Override
        public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
            productsRecyclerView = (RecyclerView) findViewById(R.id.productsRecyclerView);
            Fragment fragment;
            switch(menuItem.getItemId()){
                case R.id.navigation_products:
                    try{
                    productsRecyclerView.smoothScrollToPosition(0);}
                    catch(Exception e){ //try to scroll to top, reload fragment if in productdetailsfragment
                        fragment = new ProductsFragment();
                        loadFragment(fragment);
                        toolbar.setTitle("White Label");
                    }
                case R.id.navigation_profile:
                case R.id.navigation_promotions:
            }

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;

            switch (item.getItemId()) {
                case R.id.navigation_products:
                    fragment = new ProductsFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("White Label");

                    return true;
                case R.id.navigation_promotions:
                    fragment = new PromotionsFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("Discounts");
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    toolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };




    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }



}