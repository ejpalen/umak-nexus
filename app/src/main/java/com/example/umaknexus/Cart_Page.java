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
            // ... (unchanged)
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
                // Use Glide or another library to load the image
                // Example: ImageView productImage = findViewById(R.id.imageView);
                // Glide.with(this).load(firstCartItem.getImg_product()).into(productImage);

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
                    for (DocumentChange dc : value.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.ADDED) {
                            Cart_Item cartItem = dc.getDocument().toObject(Cart_Item.class);

                            String imageUrl = dc.getDocument().getString("product_image");
                            String productName = dc.getDocument().getString("product_name");
                            String productPrice = dc.getDocument().getString("product_subtotal");
                            Double productQty = dc.getDocument().getDouble("product_quantity");
                            String productQtyString = String.valueOf(productQty);

                            if (cartItem != null) {
//                                cartItems.add(cartItem);
                                cartItems.add(new Cart_Item(productName, productPrice, productQtyString, R.drawable.delete_btn, imageUrl));
                                Log.e("Firestore tite: ",  productName + productPrice + productQty + imageUrl);
                            } else {
                                Log.e("Firestore error: ", "Failed to convert document to Cart_Item.");
                            }
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                });
    }
}
