package com.example.umaknexus;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    ImageView productsImageView;
    TextView productsTextView, productsPriceTextView;
    LinearLayout shopProductLinearLayout;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        productsImageView= itemView.findViewById(R.id.Product_ImageView);
        productsTextView = itemView.findViewById(R.id.Product_Textview);
        productsPriceTextView = itemView.findViewById(R.id.ProductPrice_Textview);
        shopProductLinearLayout = itemView.findViewById(R.id.shopProduct_LinearLayout);

    }
}
