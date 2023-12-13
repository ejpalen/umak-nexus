package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchPage extends AppCompatActivity {
    private RecyclerView shopProductrecyclerView;
    TextView cancel;
    RecyclerView.LayoutManager layoutManager, shopProductLayoutManager;
    EditText searchEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        Intent intent = getIntent();
        String activity = intent.getStringExtra("activity");

        cancel =findViewById(R.id.Cancel_Textview);
         searchEditText = findViewById(R.id.searchEditText);
        searchEditText= findViewById(R.id.searchEditText);
        shopProductrecyclerView=findViewById(R.id.shopProductRecyclerView);

        searchEditText.requestFocus();
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

//        List<Products> productsItems=new ArrayList<Products>();
//        productsItems.add(new Products("UNIFORM (FEMALE)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("UNIFORM (MALE)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("LACE (CCIS)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("BOOKS (SCIENCE)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("BOOKS (ENGLISH)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("LACE (CTHM)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("UNIFORM (PE-FEMALE)", "$300.00",R.drawable.unif_sample));
//        productsItems.add(new Products("UNIFORM (PE-MALE)", "$300.00",R.drawable.unif_sample));

//        shopProductLayoutManager = new GridLayoutManager(this, 2);
//        shopProductrecyclerView.setLayoutManager(shopProductLayoutManager);
//        shopProductrecyclerView.setAdapter(new shopProductsAdapter(getApplication(), productsItems));


    }
}