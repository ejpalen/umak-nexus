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

public class NewArrivals_Products_Adapter extends RecyclerView.Adapter<NewArrivals_Products_ViewHolder> {

    Context context;
    List<NewArrivals_Products> items;

    public NewArrivals_Products_Adapter(Context context, List<NewArrivals_Products> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public NewArrivals_Products_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewArrivals_Products_ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_product_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewArrivals_Products_ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.productsTextView.setText(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImage()).into(holder.productsImageView);
        holder.productsPriceTextView.setText(items.get(position).getPrice());

        holder.NewArrivalsProductLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productID = items.get(position).getproductID();
                Intent intent = new Intent(context, ProductPage.class);
                intent.putExtra("productID", productID);
                intent.putExtra("activity", "shop_products");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this line
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
