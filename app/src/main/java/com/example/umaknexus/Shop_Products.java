package com.example.umaknexus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Shop_Products extends AppCompatActivity {

    private static RecyclerView shopProductrecyclerView;
    private static FirebaseFirestore database;

    private List<Categories> categoryItems;
    private static List<Products> productsItems;
    private CategoryAdapter categoryAdapter;
    private static shopProductsAdapter productsAdapter;

    private RelativeLayout searchEditText;
    public static String categoryFilter="";
    public static int filterPosition = 0;
    String page ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);

        //Initialize db
        database = FirebaseFirestore.getInstance();

        //Get intent
        Intent getFilterIntent = getIntent();
        categoryFilter = getFilterIntent.getStringExtra("filter");
        filterPosition = getFilterIntent.getIntExtra("selectedPosition", 0);

        //Initialize variables
        page = "Shop";
        Home.isCurrentShop = true;

        //Bottom Navigation
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

        //Layout Reference
        searchEditText = findViewById(R.id.searchRelativeLayout);

        //Initialize category items to Recyclerview
        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);
        shopProductrecyclerView = findViewById(R.id.shopProductRecyclerView);
        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryItems);

        getCategoryItems();
        getproductsItems(categoryFilter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_RecyclerView.setLayoutManager(layoutManager);
        category_RecyclerView.setAdapter(categoryAdapter);

        //Initialize product items to Recyclerview
        productsItems = new ArrayList<>();
        productsAdapter = new shopProductsAdapter(getApplicationContext(), productsItems);

        GridLayoutManager shopProductLayoutManager = new GridLayoutManager(this, 2);
        shopProductrecyclerView.setLayoutManager(shopProductLayoutManager);
        shopProductrecyclerView.setAdapter(productsAdapter);

        searchEditText.setOnClickListener(view -> {
            Intent intent = new Intent(Shop_Products.this, SearchPage.class);
            intent.putExtra("activity", "Shop_Products");
            startActivity(intent);
        });

    }

    private void getCategoryItems() {
        database.collection("categories")
                .orderBy("Category_name", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore error: ", error.getMessage());
                        return;
                    }

                    categoryItems.clear(); // Clear existing items
                    categoryItems.add(new Categories("All", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Fall_icon.png?alt=media&token=079787e2-b46a-48e9-b9b7-0de4d33ddd0b", page));
                    categoryItems.add(new Categories("Bestsellers", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Fbestsellers_icon.png?alt=media&token=b68027cf-d6a6-4c93-ab1e-709e5b3d6671", page));
                    categoryItems.add(new Categories("Latest", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Flatest_icon.png?alt=media&token=fb75215a-8698-428c-a8aa-3ac144592e9a", page));
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            String categoryName = dc.getDocument().getString("Category_name");
                            String icon = dc.getDocument().getString("Icon");

                            if (categoryName != null && icon != null) {
                                categoryItems.add(new Categories(categoryName, icon, page));
                            } else {
                                Log.e("Firestore error: ", "One or more fields are null.");
                            }
                        }
                    }
                    categoryAdapter.notifyDataSetChanged();
                });
    }

    public static void getproductsItems(String categoryFilter) {
        CollectionReference productsCollection = database.collection("products");

        Query query;
        if ("Bestsellers".equals(categoryFilter)) {
            query = productsCollection.orderBy("sales", Query.Direction.DESCENDING).orderBy("product_name", Query.Direction.DESCENDING);
        } else if ("Latest".equals(categoryFilter)) {
            query = productsCollection.orderBy("product_name", Query.Direction.DESCENDING);
        } else if ("All".equals(categoryFilter) || categoryFilter == null || categoryFilter.isEmpty()) {
            query = productsCollection;
        } else {
            query = productsCollection.whereEqualTo("category", categoryFilter);
        }

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            productsItems.clear(); // Clear existing items
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    String image = dc.getDocument().getString("Image");
                    String productName = dc.getDocument().getString("product_name");
                    String productPrice = dc.getDocument().getString("product_price");
                    String productCategory = dc.getDocument().getString("category");
                    String productID = dc.getDocument().getId();

                    if (productName != null && productPrice != null) {
                        productsItems.add(new Products(productName, productPrice, image, productID, productCategory));
                    } else {
                        Log.e("Firestore error: ", "One or more fields are null.");
                    }
                }
            }
            productsAdapter.notifyDataSetChanged();
        });
    }


}
