package com.example.umaknexus;

import android.content.ClipData;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderHistory extends AppCompatActivity {

    private static FirebaseFirestore database;
    static List<OrderHistory_Item> items;
    private static OrderHistory_Adapter orderHistoryAdapter;
    static String userId;
    private static FirebaseFirestore db;
    private FirebaseAuth auth;
    FirebaseUser user;
    static TextView no_ordersTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderhistory);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        //Layout Reference
        no_ordersTextview = findViewById(R.id.no_orders_tv);

        //Bottom Navigation
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

        //Initialize Recycler View and Item List
        items = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.orderHistory);
        orderHistoryAdapter = new OrderHistory_Adapter(getApplicationContext(), items);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderHistoryAdapter);

        FrameLayout backBtn = findViewById(R.id.backButtonFrame);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });

        getOrderItems();
    }

    public static void getOrderItems() {
        CollectionReference ordersCollection = db.collection("orders");

        Query query;
        query = ordersCollection;

        final StringBuilder productMap = new StringBuilder();

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            no_ordersTextview.setVisibility(View.VISIBLE);

            items.clear(); // Clear existing items
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                    Long orderID = dc.getDocument().getLong("orderID");
                    String orderStatus = dc.getDocument().getString("status");
                    String orderUserID = dc.getDocument().getString("userID");
                    Long totalPrice = dc.getDocument().getLong("total_amount");
                    String orderDate = dc.getDocument().getString("purchase_date");

                    // Retrieve the "products" field as a List of Maps
                    List<Map<String, Object>> productsList = (List<Map<String, Object>>) dc.getDocument().get("products");

                    if(orderUserID.equals(userId)) {

                        if (orderID != null && orderStatus != null && totalPrice != null && orderDate != null) {
                            // Iterate through the productsList and handle each product
                            for (Map<String, Object> product : productsList) {
                                String productName = (String) product.get("product_name");
                                Long productQty = (Long) product.get("product_quantity");

                                // Build your productMap string or process the product data accordingly
                                productMap.append(productName).append(" (").append(productQty).append("x)").append("\n");
                            }

                            // Add the order to the items list
                            items.add(new OrderHistory_Item("Order No. " + String.valueOf(orderID), orderStatus, productMap.toString(), "0", "₱" + String.valueOf(totalPrice), orderDate));
                            productMap.setLength(0);

                            no_ordersTextview.setVisibility(View.GONE);
                        } else {
                            Log.e("Firestore error: ", "One or more fields are null.");
                            no_ordersTextview.setVisibility(View.VISIBLE);
                        }

                    }else{
                        Log.e("No products found", "No products found");
                    }
                }
            }
            orderHistoryAdapter.notifyDataSetChanged();
        });
    }

}
