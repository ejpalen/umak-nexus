package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Shop_Products extends AppCompatActivity {

    private RecyclerView shopProductrecyclerView;
    RecyclerView.LayoutManager layoutManager, shopProductLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_products);

        RecyclerView category_RecyclerView = findViewById(R.id.categoryRecyclerView);
        shopProductrecyclerView=findViewById(R.id.shopProductRecyclerView);

        List<Categories> categoryItems= new ArrayList<Categories>();
        categoryItems.add(new Categories("All", R.drawable.all_icon));
        categoryItems.add(new Categories("Lace", R.drawable.lace_icon));
        categoryItems.add(new Categories("Books", R.drawable.books_icon));
        categoryItems.add(new Categories("Uniform", R.drawable.uniform_icon));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        category_RecyclerView.setLayoutManager(layoutManager);
        category_RecyclerView.setAdapter(new CategoryAdapter(getApplicationContext(), categoryItems));

        List<Products> productsItems=new ArrayList<Products>();
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));

        shopProductLayoutManager = new GridLayoutManager(this, 2);
        shopProductrecyclerView.setLayoutManager(shopProductLayoutManager);
        shopProductrecyclerView.setAdapter(new shopProductsAdapter(getApplication(), productsItems));




    }
}