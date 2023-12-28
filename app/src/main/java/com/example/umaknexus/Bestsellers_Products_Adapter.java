package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;


public class Bestsellers_Products_Adapter extends RecyclerView.Adapter<Bestsellers_Products_ViewHolder> {

    Context context;
    List<Bestsellers_Products> items;

    public Bestsellers_Products_Adapter(Context context, List<Bestsellers_Products> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Bestsellers_Products_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Bestsellers_Products_ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_bestsellers_product_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Bestsellers_Products_ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productsTextView.setText(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImage()).into(holder.productsImageView);
        holder.productsPriceTextView.setText(items.get(position).getPrice());

        holder.BestsellersProductLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Proceed to product page when clicked
                String productID = items.get(position).getproductID();
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productID", productID);
                intent.putExtra("activity", "shop_products");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
