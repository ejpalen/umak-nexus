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

public class OrderHistory extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);

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

        RecyclerView recyclerView = findViewById(R.id.orderHistory);

        List<OrderHistory_Item> items = new ArrayList<OrderHistory_Item>();
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));
        items.add(new OrderHistory_Item("Order #521144", "Completed", "1X UNIFORM (XL)", "₱ 300.00", "₱ 300.00"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new OrderHistory_Adapter(getApplicationContext(), items));
    }
}
