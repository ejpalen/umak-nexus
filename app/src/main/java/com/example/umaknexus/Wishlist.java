package com.example.umaknexus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Wishlist extends AppCompatActivity {

    RecyclerView productWishlist;
    RecyclerView.LayoutManager wishlistProductLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        productWishlist=findViewById(R.id.productWishList);

        List<Products> wishlistItems=new ArrayList<Products>();
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        wishlistItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));

        wishlistProductLayoutManager = new GridLayoutManager(this, 2);
        productWishlist.setLayoutManager(wishlistProductLayoutManager);
        productWishlist.setAdapter(new shopProductsAdapter(getApplication(), wishlistItems));

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

        FrameLayout backBtn = findViewById(R.id.backButtonFrame);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });
    }
}
