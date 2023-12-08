package com.example.umaknexus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Shop_Products extends AppCompatActivity {

    private RecyclerView shopProductrecyclerView;
    FirebaseFirestore database;

    List<Categories>  categoryItems;
    RelativeLayout searchEditText;
    private CategoryAdapter categoryAdapter;
    RecyclerView.LayoutManager layoutManager, shopProductLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);


        database = FirebaseFirestore.getInstance();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_shop);


        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_shop) {
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
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                finish();
                return true;
            }

            return false;
        });

        searchEditText= findViewById(R.id.searchRelativeLayout);
        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);
        shopProductrecyclerView=findViewById(R.id.shopProductRecyclerView);

        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryItems);

        getCategoryItems();


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

        shopProductLayoutManager = new GridLayoutManager(this, 2);
        shopProductrecyclerView.setLayoutManager(shopProductLayoutManager);
        shopProductrecyclerView.setAdapter(new shopProductsAdapter(getApplication(), productsItems));


        searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shop_Products.this, SearchPage.class);
                intent.putExtra("activity", "Shop_Products");
                startActivity(intent);
            }
        });

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