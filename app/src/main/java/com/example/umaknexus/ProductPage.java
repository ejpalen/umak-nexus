package com.example.umaknexus;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProductPage extends AppCompatActivity {
    Button btn_s, btn_m, btn_l, btn_addtocart, btn_addtowishlist;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpage);

//        public void onSizeButtonClick(View view){
////            Reset background color for all buttons
//            Button btn_addtocart = findViewById(R.id.btn_addtocart);
            Button btn_addtowishlist = findViewById(R.id.edit_profile);
            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();

//
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
        bottomNavigationView.setSelectedItemId(R.id.bottom_shop);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_shop) {
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
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            }

            return false;
       });

        FrameLayout back_btn = findViewById(R.id.backButtonFrame);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Shop_Products.class));
                finish();
            }
        });

        btn_addtowishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView productName = findViewById(R.id.prodName);
                TextView productQty = findViewById(R.id.qty_item);
                TextView productPrice = findViewById(R.id.price);

                String product = productName.getText().toString();
                int quantity = Integer.parseInt(productQty.getText().toString());
                String price = productPrice.getText().toString();

                Map<String, Object> wishlistData = new HashMap<>();
                wishlistData.put("product_name", product);
                wishlistData.put("product_quantity", quantity);
                wishlistData.put("product_subtotal", price);
//                wishlistData.put("user", auth.getCurrentUser().getUid());

                Map<String, Object> wishlistProducts = new HashMap<>();
                wishlistProducts.put("products", wishlistData);

                db.collection("wishlist").add(wishlistProducts).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Product added to wishlist.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(getApplicationContext(), Shop_Products.class));
                finish();
            }
        });

    }
}
