package com.example.umaknexus;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolder>{

    Context context;
    List<Cart_Item> items;

    public CartAdapter(Context context, List<Cart_Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        holder.prodName.setText(items.get(position).getProdName());
        holder.prodPrice.setText(items.get(position).getProdPrice());
        holder.qty_item.setText(items.get(position).getQty_item());
        holder.delete_btn.setImageResource(items.get(position).getDelete_btn());
        Glide.with(context).load(items.get(position).getImg_product()).into(holder.img_product);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
