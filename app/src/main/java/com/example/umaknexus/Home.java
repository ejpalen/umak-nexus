package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
    }
}