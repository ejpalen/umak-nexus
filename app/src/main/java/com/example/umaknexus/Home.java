package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {
    TextView name_textView, NewArrivalsViewAll_Btn, BestSellersViewAll_Btn;
    Button ViewAllProductsBtn;
    FirebaseAuth auth;
    FirebaseUser user;
    static FirebaseFirestore database;
    List<Categories>  categoryItems;
    private static RecyclerView newArrivals_RecyclerView;
    private static RecyclerView bestSellers_RecyclerView;
    private static List<NewArrivals_Products> productsItems;
    private static List<Bestsellers_Products> bestSellersProductsItems;
    RelativeLayout searchBar;
    private CategoryAdapter categoryAdapter;
    private static NewArrivals_Products_Adapter productsAdapter;
    private static Bestsellers_Products_Adapter bestSellersProductsAdapter;
    private ProgressDialog progressDialog;
    String page="";
    public static Boolean isCurrentShop = false;

    @Override
    public void onStart() {
        super.onStart();

        //Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseUser currentUser = auth.getCurrentUser();

        getNotificationItems();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), Onboarding_Signin.class);
            startActivity(intent);
            finish();
        } else {
            // Access user-related data here if needed
            String displayName = user.getDisplayName();
            String[] nameParts = displayName.split(" ");
            String firstName = nameParts[0];

            name_textView = findViewById(R.id.welcome_panel_textView);
            name_textView.setText("Hi, " + firstName + "!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Intialize db
        database = FirebaseFirestore.getInstance();

        //Layout Reference
        ViewAllProductsBtn = findViewById(R.id.ViewAllProducts_btn);
        BestSellersViewAll_Btn = findViewById(R.id.BestSellersViewAllBtn);
        NewArrivalsViewAll_Btn = findViewById(R.id.NewArrivalsViewAllBtn);

        //Initialize Variables
        isCurrentShop = false;
        page="Home";

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);

        //Bottom Navigation
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
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
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                finish();
                return true;
            }

            return false;
        });

        searchBar = findViewById(R.id.search_bar);

        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, SearchPage.class);
                intent.putExtra("activity", "Home");
                startActivity(intent);
            }
        });

        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);

        //Add Category Items to Recyclerview
        categoryItems = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getApplicationContext(), categoryItems);
        categoryItems.add(new Categories("All", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Fall_icon.png?alt=media&token=079787e2-b46a-48e9-b9b7-0de4d33ddd0b", page));
        categoryItems.add(new Categories("Bestsellers", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Fbestsellers_icon.png?alt=media&token=b68027cf-d6a6-4c93-ab1e-709e5b3d6671", page));
        categoryItems.add(new Categories("Latest", "https://firebasestorage.googleapis.com/v0/b/umak-nexus-53bf2.appspot.com/o/categoryImages%2Flatest_icon.png?alt=media&token=fb75215a-8698-428c-a8aa-3ac144592e9a", page));

        getCategoryItems();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_RecyclerView.setLayoutManager(layoutManager);
        category_RecyclerView.setAdapter(categoryAdapter);

        //Add New Arrivals Product Items to Recyclerview
        productsItems = new ArrayList<>();
        productsAdapter = new NewArrivals_Products_Adapter(getApplicationContext(), productsItems);

        newArrivals_RecyclerView = findViewById(R.id.NewArrivalsRecyclerView);
        LinearLayoutManager NewArrivalslayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        newArrivals_RecyclerView.setLayoutManager(NewArrivalslayoutManager);
        newArrivals_RecyclerView.setAdapter(productsAdapter);

        //Add Bestsellers Product Items to Recyclerview
        bestSellersProductsItems = new ArrayList<>();
        bestSellersProductsAdapter = new Bestsellers_Products_Adapter(getApplicationContext(), bestSellersProductsItems);

        bestSellers_RecyclerView = findViewById(R.id.BestSellersRecyclerView);
        LinearLayoutManager bestSellerslayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        bestSellers_RecyclerView.setLayoutManager(bestSellerslayoutManager);
        bestSellers_RecyclerView.setAdapter(bestSellersProductsAdapter);

        getBestSellersProductsItems("");
        getNewArrivalsProductsItems("");

        ViewAllProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Shop_Products.class));
            }
        });

        BestSellersViewAll_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Shop_Products.class);
                intent.putExtra("selectedPosition", 1);
                startActivity(intent);
            }
        });

        NewArrivalsViewAll_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Shop_Products.class);
                intent.putExtra("selectedPosition", 2);
                startActivity(intent);
            }
        });

    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..."); // Set the message you want to display
        progressDialog.setCancelable(false); // Set whether the dialog can be canceled by clicking outside of it
        progressDialog.show();
    }

    //Retrieve category data from Firestore
    private void getCategoryItems(){
        showProgressDialog();
        database.collection("categories")
                .orderBy("Category_name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error: ", error.getMessage());
                            return; // Stop processing if there's an error
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String categoryName = dc.getDocument().getString("Category_name");
                                String icon = dc.getDocument().getString("Icon");
                                String id = dc.getDocument().getId();

                                if (categoryName != null && icon != null) {
                                    categoryItems.add(new Categories(categoryName, icon, page));
                                } else {
                                    Log.e("Firestore error: ", "One or more fields are null.");
                                }
                            }
                        }
                        progressDialog.dismiss();

                        // Notify the adapter after adding items
                        categoryAdapter.notifyDataSetChanged();

                    }
                });
    }

    //Retrieve New Arrivals data from Firestore
    public static void getNewArrivalsProductsItems(String categoryFilter) {
        CollectionReference productsCollection = database.collection("products");

        Query query, BestSellerQuery;
        query = productsCollection.orderBy("product_name", Query.Direction.DESCENDING).limit(10);

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            productsItems.clear(); // Clear existing items
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    String image = dc.getDocument().getString("Image");
                    String productName = dc.getDocument().getString("product_name");
                    String productPrice = dc.getDocument().getString("product_price");
                    String productCategory = dc.getDocument().getString("category");
                    String productID = dc.getDocument().getId();

                    if (productName != null && productPrice != null) {
                        productsItems.add(new NewArrivals_Products(productName, productPrice, image, productID, productCategory));
                    } else {
                        Log.e("Firestore error: ", "One or more fields are null.");
                    }
                }
            }
            productsAdapter.notifyDataSetChanged();
        });

    }

    //Retrieve Bestsellers data from Firestore
    public static void getBestSellersProductsItems(String categoryFilter) {
        CollectionReference productsCollection = database.collection("products");

        Query query, BestSellerQuery;
        BestSellerQuery = productsCollection.orderBy("sales", Query.Direction.DESCENDING).limit(10);

        BestSellerQuery.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            bestSellersProductsItems.clear(); // Clear existing items
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    String image = dc.getDocument().getString("Image");
                    String productName = dc.getDocument().getString("product_name");
                    String productPrice = dc.getDocument().getString("product_price");
                    String productCategory = dc.getDocument().getString("category");
                    String productID = dc.getDocument().getId();

                    if (productName != null && productPrice != null) {
                        bestSellersProductsItems.add(new Bestsellers_Products(productName, productPrice, image, productID, productCategory));
                    } else {
                        Log.e("Firestore error: ", "One or more fields are null.");
                    }
                }
            }

            bestSellersProductsAdapter.notifyDataSetChanged();
        });
    }

    //Retrieve Notifications data from Firestore
    public void getNotificationItems() {
        Query notificationsCollection = database.collection("notifications").whereIn("userID", Arrays.asList(user.getUid(), "admin")) ;


        notificationsCollection.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    String title = dc.getDocument().getString("title");
                    String message = dc.getDocument().getString("message");

                    if (title != null && message != null) {
                        showNotification(title, message);
                    } else {
                        Log.e("Firestore error: ", "One or more fields are null.");
                    }
                }
            }
        });

    }

    //Set Notifications from Firestore
    private void showNotification(String title, String description) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("your_channel_id", "Notifications", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Channel Description");
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "your_channel_id")
                .setSmallIcon(R.drawable.umak_nexus_logo)
                .setContentTitle(title)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(0, builder.build());
    }
}