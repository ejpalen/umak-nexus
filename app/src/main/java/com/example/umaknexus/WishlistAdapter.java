package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistViewHolder> {

    Context context;
    List<WishlistItems> items;

    public WishlistAdapter(Context context, List<WishlistItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WishlistViewHolder(LayoutInflater.from(context).inflate(R.layout.wishlist_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.wishlistProduct.setText(items.get(position).getWishlistProduct());
        holder.wishlistPrice.setText(items.get(position).getWishlistPrice());
        Glide.with(context).load(items.get(position).getProductImage()).into(holder.productImage);

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productID = items.get(position).getProduct_ID();
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productID", productID);
                intent.putExtra("activity", "wishlist");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return items.size(); }
}
