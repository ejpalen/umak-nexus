package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    TextView name_textView;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public void onStart() {
        super.onStart();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), Onboarding_Signin.class);
            startActivity(intent);
            finish();
        } else {
            // Access user-related data here if needed
            String displayName = user.getDisplayName();
            String[] nameParts = displayName.split(" ");
            String firstName = nameParts[0];

            name_textView = findViewById(R.id.welcome_panel_textView);
            name_textView.setText("Hi, " + firstName + "!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.bottom_home:
//                    return true;
//                case R.id.bottom_shop:
//                    startActivity(new Intent(getApplicationContext(),Home.class));
//                    finish();
//                    return true;
//                case R.id.bottom_cart:
//                    startActivity(new Intent(getApplicationContext(),Home.class));
//                    finish();
//                    return true;
//                case R.id.bottom_notifications:
//                    startActivity(new Intent(getApplicationContext(),Home.class));
//                    finish();
//                    return true;
//                case R.id.bottom_profile:
//                    startActivity(new Intent(getApplicationContext(),Home.class));
//                    finish();
//                    return true;
//            }
//            return false;
//        });

        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);

        List<Categories>  categoryItems= new ArrayList<Categories>();
        categoryItems.add(new Categories("All", R.drawable.all_icon));
        categoryItems.add(new Categories("Lace", R.drawable.lace_icon));
        categoryItems.add(new Categories("Books", R.drawable.books_icon));
        categoryItems.add(new Categories("Uniform", R.drawable.uniform_icon));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_RecyclerView.setLayoutManager(layoutManager);
        category_RecyclerView.setAdapter(new CategoryAdapter(getApplicationContext(), categoryItems));

        List<Products> productsItems=new ArrayList<Products>();
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));


        RecyclerView newArrivals_RecyclerView = findViewById(R.id.NewArrivalsRecyclerView);

        LinearLayoutManager NewArrivalslayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newArrivals_RecyclerView.setLayoutManager(NewArrivalslayoutManager);
        newArrivals_RecyclerView.setAdapter(new NewArrivals_Products_Adapter(getApplication(), productsItems));


        RecyclerView bestSellers_RecyclerView = findViewById(R.id.BestSellersRecyclerView);

        LinearLayoutManager bestSellers_NewlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bestSellers_RecyclerView.setLayoutManager(bestSellers_NewlayoutManager);
        bestSellers_RecyclerView.setAdapter(new NewArrivals_Products_Adapter(getApplication(), productsItems));
    }
}