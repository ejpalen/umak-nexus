package com.example.umaknexus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WishlistViewHolder extends RecyclerView.ViewHolder {

    TextView wishlistProduct, wishlistPrice;
    ImageView productImage;

    public WishlistViewHolder(@NonNull View itemView) {
        super(itemView);
        wishlistProduct = itemView.findViewById(R.id.wishlistProduct);
        wishlistPrice = itemView.findViewById(R.id.wishlistPrice);
        productImage = itemView.findViewById(R.id.wishlistProductImg);
    }
}
