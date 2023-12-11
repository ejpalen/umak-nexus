package com.example.umaknexus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity {

    RecyclerView productWishlist;
    FirebaseFirestore db;
    CollectionReference wishlistRef;
    FrameLayout backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        productWishlist=findViewById(R.id.productWishList);
        db = FirebaseFirestore.getInstance();
        wishlistRef = db.collection("wishlist");

        wishlistRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<WishlistItems> items = new ArrayList<>();

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String product_name = documentSnapshot.get("products.product_name").toString();
                String product_subtotal = documentSnapshot.get("products.product_subtotal").toString();

                WishlistItems wishlistItem = new WishlistItems(product_name, product_subtotal, R.drawable.unif_sample);
                items.add(wishlistItem);
            }

            productWishlist.setAdapter(new WishlistAdapter(getApplicationContext(), items));
        });

//        List<WishlistItems> items = new ArrayList<>();
//
//        items.add(new WishlistItems("UNIFORM (Female)", "P300.00", R.drawable.unif_sample));
//        items.add(new WishlistItems("UNIFORM (Female)", "P300.00", R.drawable.unif_sample));
//        items.add(new WishlistItems("UNIFORM (Female)", "P300.00", R.drawable.unif_sample));
//
//        productWishlist.setAdapter(new WishlistAdapter(getApplicationContext(), items));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
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
                return true;
            }

            return false;
        });

        backBtn = findViewById(R.id.backButtonFrame);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });
    }
}
