package com.example.umaknexus;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class ProductPage extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;
    private TextView productQtyTextView;
    private int productQty = 1;
    private ProgressDialog progressDialog;

    String imageUrl;
    TextView productNameTextView ;
    TextView productCategoryTextView;
    TextView productPriceTextView;
    Button addQty;
    Button subtractQty;
    Button btn_addtocart ;
    Button btnAddtowishlist ;
    ImageView productImageView;
    Boolean isProductInWishlist = false;
    String productID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpage);

        //Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        //Layout Reference
        productNameTextView = findViewById(R.id.prodName);
        productCategoryTextView = findViewById(R.id.category);
        productQtyTextView = findViewById(R.id.qty_item);
        productPriceTextView = findViewById(R.id.price);
        addQty = findViewById(R.id.btn_add);
        subtractQty= findViewById(R.id.btn_subtract);
        btn_addtocart = findViewById(R.id.btn_addtocart);
        btnAddtowishlist = findViewById(R.id.btnAddtowishlist);
        productImageView = findViewById(R.id.img_product);

        //Get Intent
        Intent intent = getIntent();
        productID = intent.getStringExtra("productID");

        setUserImage(productID);

        showProgressDialog();

        if (productID != null) {
            CollectionReference productsRef = db.collection("products");

            // Query to get the document where the "id" field matches the product ID
            productsRef.whereEqualTo(FieldPath.documentId(), productID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve the document ID
                                String documentId = document.getId();

                                // Retrieve other data from the document
                                imageUrl = document.getString("Image"); // Assign the value to imageUrl
                                String productCat = document.getString("category");
                                String productName = document.getString("product_name");
                                String productPrice = document.getString("product_price");

                                Glide.with(this).load(imageUrl).into(productImageView);
                                productCategoryTextView.setText(productCat);
                                productNameTextView.setText(productName);
                                productPriceTextView.setText(productPrice);
                            }

                            progressDialog.dismiss(); // Dismiss the dialog after data is fetched
                        } else {
                            // Handle errors
                            Toast.makeText(ProductPage.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss(); // Dismiss the dialog in case of an error
                        }
                    });
        } else {
            // Handle the case where productID is null
            Toast.makeText(ProductPage.this, "Product ID is null", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss(); // Dismiss the dialog if productID is null
        }

        // Set initial quantity in TextView
        productQtyTextView.setText(String.valueOf(productQty));

        // Set click listeners for add and subtract buttons
        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });

        subtractQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

        //Bottom Navigation
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
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
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

        //Add to Cart Button is Clicked
        btn_addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView productName = findViewById(R.id.prodName);
                TextView productQty = findViewById(R.id.qty_item);
                TextView productPrice = findViewById(R.id.price);

                String userID = user.getUid();
                String product = productName.getText().toString();
                int quantity = Integer.parseInt(productQty.getText().toString());
                String price = productPrice.getText().toString();
                String image = imageUrl;

                // Create a new document reference
                DocumentReference cartRef = db.collection("cart").document();

                // Create a map to represent the data
                Map<String, Object> cartData = new HashMap<>();
                cartData.put("userID", userID);
                cartData.put("product_name", product);
                cartData.put("product_quantity", quantity);
                cartData.put("product_subtotal", price);
                cartData.put("product_image", image);

                // Set the entire map as the document data
                cartRef.set(cartData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Product added to cart.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error adding product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    private void WishlistButton(String productID , Boolean isInWishlist) {
    btnAddtowishlist.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if (isInWishlist) {
            // Product is in the wishlist, so remove it
            removeFromWishlist(productID);
        } else {
            // Product is not in the wishlist, so add it
            addToWishlist(productID);
        }
    }
    });
    }

    private void removeFromWishlist(String productID) {
        db.collection("wishlist")
                .whereEqualTo("products.productID", productID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Product removed from wishlist.", Toast.LENGTH_SHORT).show();

                                        WishlistButton(productID, false);
                                        btnAddtowishlist.setText("ADD TO WISHLIST");
                                    } else {
                                        Toast.makeText(ProductPage.this, "Failed to delete user data: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void addToWishlist(String productID) {
        TextView productName = findViewById(R.id.prodName);
        TextView productQty = findViewById(R.id.qty_item);
        TextView productPrice = findViewById(R.id.price);

        String userID = user.getUid();
        String product = productName.getText().toString();
        int quantity = Integer.parseInt(productQty.getText().toString());
        String price = productPrice.getText().toString();
        String image = imageUrl;

        Map<String, Object> wishlistData = new HashMap<>();
        wishlistData.put("userID", userID);
        wishlistData.put("product_name", product);
        wishlistData.put("product_quantity", quantity);
        wishlistData.put("product_subtotal", price);
        wishlistData.put("product_image", image);
        wishlistData.put("productID", productID);

        Map<String, Object> wishlistProducts = new HashMap<>();
        wishlistProducts.put("products", wishlistData);

        db.collection("wishlist").add(wishlistProducts).addOnSuccessListener(documentReference -> {
            Toast.makeText(getApplicationContext(), "Product added to wishlist.", Toast.LENGTH_SHORT).show();
            WishlistButton(productID, true);
            btnAddtowishlist.setText("REMOVE FROM WISHLIST");
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    //Retrieve data from wishlist where product id is current id
    private void setUserImage(String productId) {
        // Reference to the "wishlist" collection
        CollectionReference wishlistRef = db.collection("wishlist");

        // Query to get the document where the "productID" field matches the product ID
        wishlistRef
        .whereEqualTo("products.userID", user.getUid())
                .whereEqualTo("products.productID", productId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        isProductInWishlist = true;
                    } else {
                        isProductInWishlist = false;
                    }

                    if(isProductInWishlist == true){
                        WishlistButton(productID, isProductInWishlist);
                        btnAddtowishlist.setText("REMOVE FROM WISHLIST");
                    }else{
                        WishlistButton(productID, isProductInWishlist);
                        btnAddtowishlist.setText("ADD TO WISHLIST");
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Toast.makeText(ProductPage.this, "Error getting wishlist data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set the message you want to display
        progressDialog.setCancelable(false); // Set whether the dialog can be canceled by clicking outside of it
        progressDialog.show();
    }
        private void incrementQuantity() {
            productQty++;
            updateQuantityTextView();
        }

        private void decrementQuantity() {
            if (productQty > 1) {
                productQty--;
                updateQuantityTextView();
            }
            else {
            }
        }
        private void updateQuantityTextView() {
            productQtyTextView.setText(String.valueOf(productQty));
        }
}
