package com.example.umaknexus;

import android.content.Context;
import android.view.LayoutInflater;
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
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        holder.wishlistProduct.setText(items.get(position).getWishlistProduct());
        holder.wishlistPrice.setText(items.get(position).getWishlistPrice());
        Glide.with(context).load(items.get(position).getProductImage()).into(holder.productImage);
    }

    @Override
    public int getItemCount() { return items.size(); }
}
