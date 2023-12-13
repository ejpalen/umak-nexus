package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<Cart_Item> items = new ArrayList<Cart_Item>();
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", "https://lh3.googleusercontent.com/a/ACg8ocKHWMOGsqqtg4qyIM81R0cxKzL6oLwiUG3er62jQf9F1R8=s96-c", R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", "https://lh3.googleusercontent.com/a/ACg8ocKHWMOGsqqtg4qyIM81R0cxKzL6oLwiUG3er62jQf9F1R8=s96-c", R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", "https://lh3.googleusercontent.com/a/ACg8ocKHWMOGsqqtg4qyIM81R0cxKzL6oLwiUG3er62jQf9F1R8=s96-c", R.drawable.delete_btn));
        items.add(new Cart_Item("Uniform L", "₱300.00", "1", "https://lh3.googleusercontent.com/a/ACg8ocKHWMOGsqqtg4qyIM81R0cxKzL6oLwiUG3er62jQf9F1R8=s96-c", R.drawable.delete_btn));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CartAdapter(getApplicationContext(), items));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView productName = findViewById(R.id.prodName);
                TextView productQty = findViewById(R.id.qty_item);
                TextView productPrice = findViewById(R.id.price);

                String product = productName.getText().toString();
                int quantity = Integer.parseInt(productQty.getText().toString());
                String price = productPrice.getText().toString();

                Map<String, Object> orderData = new HashMap<>();
                orderData.put("product_name", product);
                orderData.put("product_quantity", quantity);
                orderData.put("product_subtotal", price);

                Map<String, Object> orderProducts = new HashMap<>();
                orderProducts.put("products", orderData);

                db.collection("orders").add(orderProducts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Checked out succesfully!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(getApplicationContext(), Order_Confirmation.class));
                finish();
            }
        });

        // Set a click listener for the "Clear Cart" button
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
    }

    private void clearCart() {
        Cart_Item.clear();
        }
    }

