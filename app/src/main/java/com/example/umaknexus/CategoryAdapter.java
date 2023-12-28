package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    Context context;
    List<Categories> items;
    int selectedPosition = RecyclerView.NO_POSITION;

    public CategoryAdapter(Context context, List<Categories> items) {
        this.context = context;
        this.items = items;

        if(Home.isCurrentShop == true){
            selectedPosition = Shop_Products.filterPosition;
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.category_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.categoryTextView.setText(items.get(position).getName());
        Glide.with(context).load(items.get(position).getImage()).into(holder.categoryImageView);

        // Set background based on selection
        if (position == selectedPosition) {
            holder.categoryContainer.setBackgroundResource(R.drawable.category_bg_active);
        } else {
            holder.categoryContainer.setBackgroundResource(R.drawable.category_bg);
        }

        holder.categoryContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = position;
                notifyDataSetChanged(); // Update views

                Shop_Products.categoryFilter = items.get(position).getName();
                Shop_Products.getproductsItems(items.get(position).getName());

                if (items.get(position).getPage().equals("Home")) {
                    Intent intent = new Intent(context, Shop_Products.class);
                    intent.putExtra("filter", items.get(position).getName());
                    intent.putExtra("selectedPosition", position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
