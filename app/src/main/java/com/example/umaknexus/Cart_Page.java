package com.example.umaknexus;

import android.content.ClipData;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Cart_Page extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_cart);
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
                return true;
            } else if (item.getItemId() == R.id.bottom_notifications) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            }

            return false;
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Cart_Item> items = new ArrayList<Cart_Item>();
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CartAdapter(getApplicationContext(), items));
    }
}
