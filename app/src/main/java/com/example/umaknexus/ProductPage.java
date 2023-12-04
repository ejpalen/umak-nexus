package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductPage extends AppCompatActivity {
    Button btn_s, btn_m, btn_l, btn_addtocart, btn_addtowishlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpage);

//        public void onSizeButtonClick(View view){
////            Reset background color for all buttons
//            Button btn_s = findViewById(R.id.btn_s);
//            Button btn_m = findViewById(R.id.btn_m);
//            Button btn_l = findViewById(R.id.btn_l);
//            Button btn_addtocart = findViewById(R.id.btn_addtocart);
//            Button btn_addtowishlist = findViewById(R.id.btn_addtowishlist);
//
//            btn_s.setSelected(R.id.btn_s);
//            btn_m.setSelected(R.id.btn_m);
//            btn_l.setSelected(R.id.btn_l);
//            btn_addtocart.setSelected(R.id.btn_addtocart);
//            btn_addtowishlist.setSelected(R.id.btn_addtowishlist);
//
//            // Set background color for the clicked button
//            Button clickedButton = (Button) view;
//            clickedButton.setBackgroundResource(R.drawable.item_selector_selected);
//            String buttonText = clickedButton.getText().toString();
//
//            // Handle the button click logic here
//        }

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


    }
}
