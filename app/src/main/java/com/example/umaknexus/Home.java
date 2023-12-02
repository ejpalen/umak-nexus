package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    TextView name_textView;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;
    List<Categories>  categoryItems;
    private CategoryAdapter categoryAdapter;

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

        database = FirebaseFirestore.getInstance();

        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.clearFocus();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                return true;
            } else if (item.getItemId() == R.id.bottom_shop) {
                startActivity(new Intent(getApplicationContext(), Shop_Products.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_cart) {
                startActivity(new Intent(getApplicationContext(), Cart_Page.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_notifications) {
                startActivity(new Intent(getApplicationContext(), Notifications.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            }

            return false;
        });

        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);

        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryItems);

        getCategoryItems();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_RecyclerView.setLayoutManager(layoutManager);
        category_RecyclerView.setAdapter(categoryAdapter);

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

    private void getCategoryItems(){
        database.collection("categories")
                .orderBy("Category_name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error: ", error.getMessage());
                            return; // Stop processing if there's an error
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String categoryName = dc.getDocument().getString("Category_name");
                                String icon = dc.getDocument().getString("Icon");

                                if (categoryName != null && icon != null) {
                                    categoryItems.add(new Categories(categoryName, icon));
                                } else {
                                    Log.e("Firestore error: ", "One or more fields are null.");
                                }
                            }
                        }
                        // Notify the adapter after adding items
                        categoryAdapter.notifyDataSetChanged();

                    }
                });
    }
}