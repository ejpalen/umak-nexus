package com.example.umaknexus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartHolder extends RecyclerView.ViewHolder {

    ImageView img_product, delete_btn;
    TextView prodName, prodPrice, qty_item;

    public CartHolder(@NonNull View itemView) {
        super(itemView);

        img_product = itemView.findViewById(R.id.img_product);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        prodName = itemView.findViewById(R.id.prodName);
        prodPrice = itemView.findViewById(R.id.prodPrice);
        qty_item = itemView.findViewById(R.id.qty_item);

    }
}
