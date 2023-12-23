package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart_Page extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private List<Cart_Item> cartItems;
    private CartAdapter cartAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        Button confirm = findViewById(R.id.btn_confirm);
        Button clear = findViewById(R.id.clear);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

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
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                finish();
                return true;
            }

            return false;
        });

        cartRecyclerView = findViewById(R.id.Cartrecyclerview);
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Call the method to get cart items
        getCartItems();

        confirm.setOnClickListener(view -> {
            // Retrieve details from the first item in the cart (you might need to modify this logic based on your use case)
            if (!cartItems.isEmpty()) {
                Cart_Item firstCartItem = cartItems.get(0);
                String product = firstCartItem.getProdName();
                int quantity = Integer.parseInt(firstCartItem.getQty_item());
                String price = firstCartItem.getProdPrice();

                Map<String, Object> orderData = new HashMap<>();
                orderData.put("product_name", product);
                orderData.put("product_quantity", quantity);
                orderData.put("product_subtotal", price);
                // Add other fields as needed

                Map<String, Object> orderProducts = new HashMap<>();
                orderProducts.put("products", orderData);

                db.collection("orders").add(orderProducts)
                        .addOnSuccessListener(documentReference ->
                                Toast.makeText(getApplicationContext(), "Checked out successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show());

                startActivity(new Intent(getApplicationContext(), Order_Confirmation.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Cart is empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a click listener for the "Clear Cart" button
        clear.setOnClickListener(v -> clearCart());
    }

    private void clearCart() {
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
    }

    private void getCartItems() {
        // Replace "cart" with your actual Firestore collection name
        db.collection("cart")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore error: ", error.getMessage());
                        return;
                    }

                    cartItems.clear(); // Clear existing items
                    for (DocumentSnapshot document : value.getDocuments()) {
                        String imageUrl = document.getString("product_image");
                        String productName = document.getString("product_name");
                        String productPrice = document.getString("product_subtotal");

                        // Check if the "product_quantity" field exists before attempting to retrieve it
                        Integer productQty = document.getLong("product_quantity") != null
                                ? Math.toIntExact(document.getLong("product_quantity"))
                                : null;

                        String productQtyString = String.valueOf(productQty);

                        if (productName != null && productPrice != null && productQty != null) {
                            cartItems.add(new Cart_Item(productName, productPrice, productQtyString, R.drawable.delete_btn, imageUrl, productQty));
                            Log.e("Firestore title: ", productName + productPrice + productQty + imageUrl);
                        } else {
                            Log.e("Firestore error: ", "Missing fields in document.");
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                });
    }

}