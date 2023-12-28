package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    private FirebaseFirestore database;

    private List<Products> productsItems;
    private CategoryAdapter categoryAdapter;
    private shopProductsAdapter productsAdapter;
    private RecyclerView shopProductrecyclerView;
    TextView cancel;
    RecyclerView.LayoutManager layoutManager, shopProductLayoutManager;
    private SearchView searchEditText;
    private boolean initialProductsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        database = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");

        cancel =findViewById(R.id.Cancel_Textview);
        searchEditText = findViewById(R.id.searchEditText);
        searchEditText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        searchEditText= findViewById(R.id.searchEditText);
        shopProductrecyclerView=findViewById(R.id.shopProductRecyclerView);

        searchEditText.requestFocus();

        searchEditText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    // If the search query is empty, clear the displayed products or take any other action
                    clearDisplayedProducts();
                    initialProductsLoaded = false; // Reset the flag so that next time user types, it fetches data again
                } else {
                    // If there's a search query, filter and display the relevant products
                    filterList(newText);

                    // Fetch data if it's the first time the user is typing or if the data is cleared
                    if (!initialProductsLoaded || productsItems.isEmpty()) {
                        getproductsItems();
                        initialProductsLoaded = true;
                    }
                }
                return false;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activity.equals("Shop_Products")){
                    startActivity(new Intent(getApplicationContext(), Shop_Products.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(), Home.class));
                }
            }
        });

        shopProductrecyclerView = findViewById(R.id.shopProductRecyclerView);

        //Initialize prouct items to Recyclerview
        productsItems = new ArrayList<>();
        productsAdapter = new shopProductsAdapter(getApplicationContext(), productsItems);

        GridLayoutManager shopProductLayoutManager = new GridLayoutManager(this, 2);
        shopProductrecyclerView.setLayoutManager(shopProductLayoutManager);
        shopProductrecyclerView.setAdapter(productsAdapter);
    }

   private void clearDisplayedProducts() {
        // Clear the displayed products
       if (searchEditText.getQuery().toString().isEmpty()) {
           productsItems.clear();
           productsAdapter.notifyDataSetChanged();
       }
    }

    //Filter Items
    private void filterList(String text) {
        List<Products> filteredList = new ArrayList<>();
        for (Products products: productsItems ){
            if(products.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(products);
            }
        }

        if(filteredList.isEmpty()){
            Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
        }else{
            productsAdapter.setFilteredList(filteredList);
        }
    }

    private void getproductsItems() {
        database.collection("products")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e("Firestore error: ", error.getMessage());
                        return;
                    }

                    runOnUiThread(() -> {
                        productsItems.clear(); // Clear existing items
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                String image = dc.getDocument().getString("Image");
                                String productName = dc.getDocument().getString("product_name");
                                String productPrice = dc.getDocument().getString("product_price");
                                String productCategory = dc.getDocument().getString("category");
                                String productID = dc.getDocument().getId();

                                if (productName != null && productPrice != null) {
                                    productsItems.add(new Products(productName, productPrice, image, productID, productCategory));
                                } else {
                                    Log.e("Firestore error: ", "One or more fields are null.");
                                }
                            }
                        }
                        productsAdapter.notifyDataSetChanged();
                    });
                });
    }
}
