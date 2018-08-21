package com.example.wwwconcepts.firebase;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.wwwconcepts.firebase.Fragments.ProductDetailsFragment;
import com.example.wwwconcepts.firebase.Fragments.ProductsFragment;
import com.example.wwwconcepts.firebase.Fragments.ProfileFragment;
import com.example.wwwconcepts.firebase.Fragments.PromotionsFragment;


public class MainActivity extends AppCompatActivity implements ProductDetailsFragment.OnFragmentInteractionListener, ProductsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, PromotionsFragment.OnFragmentInteractionListener{

    private ActionBar toolbar;
    private RecyclerView productsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setOnNavigationItemReselectedListener(mOnNavigationItemReselectedListener);




        toolbar.setTitle("Shop");
        loadFragment(new ProductsFragment());
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
                        toolbar.setTitle("Shop");
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
                    toolbar.setTitle("Shop");

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