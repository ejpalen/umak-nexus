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

        // Call the method to set up the CartAdapter
        setupCartAdapter();

        // Call the method to get cart items
        getCartItems();

        confirm.setOnClickListener(view -> {
            // Retrieve details from the first item in the cart (you might need to modify this logic based on your use case)
            if (!cartItems.isEmpty()) {
                List<Map<String, Object>> orderProductsList = new ArrayList<>();

                // Loop through cart items and create a list of order products
                for (Cart_Item cartItem : cartItems) {
                    String product = cartItem.getProdName();
                    int quantity = Integer.parseInt(cartItem.getQty_item());
                    String price = cartItem.getProdPrice();

                    Map<String, Object> orderData = new HashMap<>();
                    orderData.put("product_name", product);
                    orderData.put("product_quantity", quantity);
                    orderData.put("product_subtotal", price);
                    // Add other fields as needed

                    orderProductsList.add(orderData);
                }

                Map<String, Object> order = new HashMap<>();
                order.put("products", orderProductsList);

                db.collection("orders")
                        .add(order)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getApplicationContext(), "Checked out successfully!", Toast.LENGTH_SHORT).show();

                            // Calculate the total price
                            double totalPrice = calculateTotalPrice();

                            // Update the total amount TextView
                            TextView amountTextView = findViewById(R.id.amount);
                            amountTextView.setText(String.format("₱%.2f", totalPrice));

                            // Clear the cart after a successful order
                            clearCart();

                            startActivity(new Intent(getApplicationContext(), Order_Confirmation.class));
                            finish();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getApplicationContext(), "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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
                        String documentId = document.getId();

                        if (productName != null && productPrice != null && productQty != null) {
                            cartItems.add(new Cart_Item(productName, productPrice, productQtyString, R.drawable.delete_btn, imageUrl, productQty, documentId));
                            Log.e("Firestore title: ", productName + productPrice + productQty + imageUrl);
                        } else {
                            Log.e("Firestore error: ", "Missing fields in document.");
                        }
                    }
                    cartAdapter.notifyDataSetChanged();
                });
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;

        for (Cart_Item cartItem : cartItems) {
            // Parse the price and quantity to calculate subtotal for each item
            double price = Double.parseDouble(cartItem.getProdPrice());
            int quantity = Integer.parseInt(cartItem.getQty_item());
            double subtotal = price * quantity;

            // Add the subtotal to the total price
            totalPrice += subtotal;
        }

        return totalPrice;
    }


    private void setupCartAdapter() {
        cartAdapter.setOnItemDeleteListener(position -> {
            // Handle item deletion here
            removeItemFromCartAndFirestore(position);
        });
    }


    private void removeItemFromCartAndFirestore(int position) {
        // Get the item to be removed
        Cart_Item itemToRemove = cartItems.get(position);

        // Remove the item from Firestore
        // Replace "cart" with your actual Firestore collection name
        db.collection("cart").document(itemToRemove.getDocumentId()).delete()
                .addOnSuccessListener(aVoid -> {
                    // Remove the item from the local list
                    cartItems.remove(position);

                    // Notify the adapter that the data set has changed
                    cartAdapter.notifyItemRemoved(position);

                    // Now, add the item to the "orders" collection
                    addToOrdersCollection(itemToRemove);

                    // You can also update the total price or perform other actions
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(), "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Method to add the item to the "orders" collection
    private void addToOrdersCollection(Cart_Item cartItem) {
        Map<String, Object> orderProduct = new HashMap<>();
        orderProduct.put("product_name", cartItem.getProdName());
        orderProduct.put("product_quantity", cartItem.getQuantity());
        orderProduct.put("product_subtotal", cartItem.getProdPrice());
        // Add other fields as needed

        db.collection("orders")
                .add(orderProduct)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(), "Checked out successfully!", Toast.LENGTH_SHORT).show();

                    // Clear the cart after a successful order
                    clearCart();

                    startActivity(new Intent(getApplicationContext(), Order_Confirmation.class));
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(), "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}
