package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class shopProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    Context context;
    List<Products> items;

    public void setFilteredList(List<Products> filteredList){
        this.items = filteredList;
        notifyDataSetChanged();
    }

    public shopProductsAdapter(Context context, List<Products> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductsViewHolder(LayoutInflater.from(context).inflate(R.layout.shopproduct_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productsTextView.setText(items.get(position).getName());
       // holder.productsImageView.setImageResource(items.get(position).getImage());
        Glide.with(context).load(items.get(position).getImage()).into(holder.productsImageView);
        holder.productsPriceTextView.setText(items.get(position).getPrice());

        holder.shopProductLinearLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.anim_one));


        holder.shopProductLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle image click, start ProductPage activity with product code

                String productID = items.get(position).getproductID();
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productID", productID);
                intent.putExtra("activity", "shop_products");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line
                context.startActivity(intent);
//                Toast.makeText(view.getContext(), "PRODUCT ID: "  + productID, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
