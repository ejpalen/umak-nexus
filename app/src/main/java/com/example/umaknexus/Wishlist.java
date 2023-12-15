package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity {

    RecyclerView productWishlist;
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    CollectionReference wishlistRef;
    FrameLayout backBtn;
    List<WishlistItems> items;
    WishlistAdapter wishlistAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        productWishlist=findViewById(R.id.productWishList);
        db = FirebaseFirestore.getInstance();
        wishlistRef = db.collection("wishlist");
        
//        getProductImage();

//        // Initialize FirebaseAuth
//        auth = FirebaseAuth.getInstance();
//        user = auth.getCurrentUser();
//
//        String userId = user.getUid();

        wishlistRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            items = new ArrayList<>();

            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String product_name = documentSnapshot.get("products.product_name").toString();
                String product_subtotal = documentSnapshot.get("products.product_subtotal").toString();
                String product_image = documentSnapshot.get("products.product_image").toString();

                WishlistItems wishlistItem = new WishlistItems(product_name, product_subtotal, product_image);
                items.add(wishlistItem);
            }

            productWishlist.setAdapter(new WishlistAdapter(getApplicationContext(), items));
        });

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

//    private void getProductImage() {
//        db.collection("wishlist")
//
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.e("Firestore error: ", error.getMessage());
//                            return; // Stop processing if there's an error
//                        }
//
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                String image = dc.getDocument().getString("product_image" );
//                                String name = dc.getDocument().getString("product_name");
//                                String price = dc.getDocument().getString("product_price");
//
//                                if (name != null && price != null ) {
//                                    items.add(new WishlistItems(name, price, image));
//                                } else {
//                                    Log.e("Firestore error: ", "One or more fields are null.");
//                                }
//                            }
//                        }
//                        // Notify the adapter after adding items
//                        wishlistAdapter.notifyDataSetChanged();
//
//                    }
//                });
//    }
}
