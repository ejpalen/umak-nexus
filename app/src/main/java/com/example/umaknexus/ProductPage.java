package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpage);

//        public void onSizeButtonClick(View view){
//            // Reset background color for all buttons
//            Button btn_addtocart = findViewById(R.id.btn_addtocart);
//            Button btn_addtowishlist = findViewById(R.id.btn_addtowishlist);
//
//            btn_addtocart.setSelected(R.id.btn_addtocart);
//            btn_addtowishlist.setSelected(R.id.btn_addtowishlist);

//            // Set background color for the clicked button
//            Button clickedButton = (Button) view;
//            clickedButton.setBackgroundResource(R.drawable.item_selector_selected);
//            String buttonText = clickedButton.getText().toString();
//
//            // Handle the button click logic here
//        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_shop);
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