package com.example.umaknexus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductsViewHolder extends RecyclerView.ViewHolder {

    ImageView productsImageView;
    TextView productsTextView, productsPriceTextView;

    public ProductsViewHolder(@NonNull View itemView) {
        super(itemView);

        productsImageView= itemView.findViewById(R.id.Product_ImageView);
        productsTextView = itemView.findViewById(R.id.Product_Textview);
        productsPriceTextView = itemView.findViewById(R.id.ProductPrice_Textview);
    }
}
