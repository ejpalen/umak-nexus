package com.example.umaknexus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Categories> items;

    public CategoryAdapter(Context context, List<Categories> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryTextView.setText(items.get(position).getName());
        //holder.categoryImageView.setImageResource(items.get(position).getImage());
        Glide.with(context).load(items.get(position).getImage()).into(holder.categoryImageView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
