package com.example.umaknexus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class shopProductsAdapter extends RecyclerView.Adapter<ProductsViewHolder> {

    Context context;
    List<Products> items;

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
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {
        holder.productsTextView.setText(items.get(position).getName());
        holder.productsImageView.setImageResource(items.get(position).getImage());
        holder.productsPriceTextView.setText(items.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
