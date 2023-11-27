package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation1);
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

    }
}