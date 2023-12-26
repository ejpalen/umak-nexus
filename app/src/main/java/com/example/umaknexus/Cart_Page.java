package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart_Page extends AppCompatActivity implements CartAdapter.OnItemRemoveListener {

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
        TextView totalAmountTextView = findViewById(R.id.amount);

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
        cartAdapter = new CartAdapter(this, cartItems, totalAmountTextView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);
        cartAdapter.setOnItemRemoveListener(this);
        cartAdapter.setOnQuantityChangeListener(new CartAdapter.OnQuantityChangeListener() {
            @Override
            public void onIncrement(Cart_Item item, int position) {
                // Implement the logic for incrementing the quantity
                int newQuantity = item.getQuantity() + 1;
                item.setQuantity(newQuantity);

                // Update the Firestore document with the new quantity
                updateQuantityInFirestore(item);

                // Update the local list and notify the adapter
                cartItems.set(position, item);
                cartAdapter.notifyItemChanged(position);

                // Update the total price here
                cartAdapter.updateTotalPrice();
            }

            @Override
            public void onDecrement(Cart_Item item, int position) {
                // Implement the logic for decrementing the quantity
                int newQuantity = item.getQuantity() - 1;
                if (newQuantity >= 1) {
                    item.setQuantity(newQuantity);

                    // Update the Firestore document with the new quantity
                    updateQuantityInFirestore(item);

                    // Update the local list and notify the adapter
                    cartItems.set(position, item);
                    cartAdapter.notifyItemChanged(position);

                    // Update the total price here
                    cartAdapter.updateTotalPrice();
                }
            }
        });

        // Call the method to get cart items
        getCartItems();


        confirm.setOnClickListener(view -> {
            // Get the current user's name
            String userName = getCurrentUserName();

            // Get the current date
            String purchaseDate = getCurrentDate();


            // Create a list to hold all order items
            List<Map<String, Object>> orderProducts = new ArrayList<>();

            // Iterate over all items in the cart
            for (Cart_Item cartItem : cartItems) {
                String product = cartItem.getProdName();
                int quantity = cartItem.getQuantity();
                String price = cartItem.getProdPrice();

                // Create a map for each order item
                Map<String, Object> orderItemData = new HashMap<>();
                orderItemData.put("product_name", product);
                orderItemData.put("product_quantity", quantity);
                orderItemData.put("product_subtotal", price);
                // Add other fields as needed

                // Add the order item to the list
                orderProducts.add(orderItemData);
            }

            // Create a map for the entire order document
            Map<String, Object> orderDocument = new HashMap<>();
            orderDocument.put("user_name", userName);
            orderDocument.put("purchase_date", purchaseDate);
            orderDocument.put("total_amount", cartAdapter.calculateTotalPrice()); // Add the total amount to the order
            orderDocument.put("products", orderProducts);
            // Add other fields as needed

            // Add the order to the "orders" collection
            db.collection("orders").add(orderDocument)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(getApplicationContext(), "Checked out successfully!", Toast.LENGTH_SHORT).show();

                        clearCart();

                        Intent intent = new Intent(getApplicationContext(), Order_Confirmation.class);

                        // Pass the user's name, purchase date, and order ID to Order_Confirmation activity
                        intent.putExtra("user_name", userName);
                        intent.putExtra("purchase_date", purchaseDate);
                        intent.putExtra("order_id", documentReference.getId()); // Use the generated order ID
                        intent.putExtra("total_amount", cartAdapter.calculateTotalPrice());


                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(getApplicationContext(), "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });


        // Set a click listener for the "Clear Cart" button
        clear.setOnClickListener(v -> clearCart());
    }

    private String getCurrentUserName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getDisplayName() : "Unknown User";
    }

    private String getCurrentDate() {
        // Use your preferred method/library to get the current date in the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void getAndIncrementOrderNumber(OnSuccessListener<Integer> successListener) {
        // Get the latest order number from Firestore and increment it
        DocumentReference orderNumberRef = db.collection("order_numbers").document("latest_order_number");

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(orderNumberRef);

            // Get the current order number
            Integer currentOrderNumber = snapshot.getLong("order_number").intValue();

            // Increment the order number
            int newOrderNumber = currentOrderNumber + 1;

            // Update the order number in Firestore for the next order
            transaction.update(orderNumberRef, "order_number", newOrderNumber);

            // Pass the updated order number to the success listener
            successListener.onSuccess(newOrderNumber);

            return null;
        }).addOnFailureListener(e ->
                Log.e("Firestore", "Error getting/incrementing order number", e));
    }

    private void clearCart() {
        // Iterate through all items in the cart and remove them from Firestore
        for (Cart_Item item : cartItems) {
            cartAdapter.removeFromFirestore(item.getDocumentId());  // Use item directly
        }

        // Clear the local list and notify the adapter
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemRemove(Cart_Item item) {
        // Handle item removal in Firestore or any other actions
        cartAdapter.removeFromFirestore(item.getDocumentId());  // Use item directly
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
                            // Set documentId for each Cart_Item
                            Cart_Item cartItem = new Cart_Item(productName, productPrice, productQtyString, R.drawable.delete_btn, imageUrl, productQty);
                            cartItem.setDocumentId(document.getId()); // Set documentId
                            cartItems.add(cartItem);

                            Log.e("Firestore title: ", productName + productPrice + productQty + imageUrl);
                        } else {
                            Log.e("Firestore error: ", "Missing fields in document.");
                        }
                    }

                    // Update the total price after fetching cart items
                    updateTotalPrice();
                    cartAdapter.notifyDataSetChanged();
                });
    }

    private void updateQuantityInFirestore(Cart_Item item) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference cartItemRef = db.collection("cart").document(item.getDocumentId());

        // Create a new map to update only the quantity field
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("product_quantity", item.getQuantity());

        // Update the Firestore document with the new quantity
        cartItemRef.update(updateData)
                .addOnSuccessListener(aVoid ->
                        Log.d("Firestore", "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e ->
                        Log.w("Firestore", "Error updating document", e));
    }


    private void updateTotalPrice() {
        cartAdapter.updateTotalPrice();
    }
}