package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import java.util.HashMap;
import java.util.Map;

public class Cart_Page extends AppCompatActivity {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_page);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Simulate a purchase
        // Replace this with your actual purchase logic
        // For example, you might have a button that triggers this function
        simulatePurchase();

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

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Cart_Item> items = new ArrayList<Cart_Item>();
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", R.drawable.sample_image, R.drawable.delete_btn));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CartAdapter(getApplicationContext(), items));

        Button confirm = findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Order_Confirmation.class));
            }
        });
    }
    private void simulatePurchase() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // Get the current user's cart document reference
            DocumentReference cartRef = db.collection("cart").document(currentUser.getUid());

            cartRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot cartSnapshot = task.getResult();
                        if (cartSnapshot.exists()) {
                            // Perform the purchase logic here
                            // For example, move data from the cart to the orders collection
                            moveDataToOrders(cartRef, cartSnapshot.getData());
                            // Clear the cart after moving data
                            clearCart(cartRef);
                        } else {
                            Log.d("Cart_Page", "Cart does not exist for the current user");
                        }
                    } else {
                        Log.e("Cart_Page", "Error getting cart document", task.getException());
                    }
                }
            });
        } else {
            Log.d("Cart_Page", "User is not authenticated");
        }
    }

    private void moveDataToOrders(DocumentReference cartRef, Map<String, Object> cartData) {
        // Get the "orders" collection reference
        CollectionReference ordersCollection = db.collection("orders");

        // Generate a new document ID for the order
        DocumentReference orderRef = ordersCollection.document();

        // Create a map with the order data
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("users", auth.getCurrentUser().getUid());
        orderData.put("products", cartData); // Assuming your product data is stored in the cart

        // Add the order data to the "orders" collection
        orderRef.set(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Cart_Page", "Order added to orders collection");
                } else {
                    Log.e("Cart_Page", "Error adding order to orders collection", task.getException());
                }
            }
        });
    }

    private void clearCart(DocumentReference cartRef) {
        WriteBatch batch = db.batch();

        // Delete the cart document
        batch.delete(cartRef);

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Cart_Page", "Cart cleared successfully");
                } else {
                    Log.e("Cart_Page", "Error clearing cart", task.getException());
                }
            }
        });
    }
}
