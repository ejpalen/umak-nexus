package com.example.umaknexus;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    ImageView categoryImageView;
    TextView categoryTextView;
    LinearLayout categoryContainer;

    public CategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        categoryImageView= itemView.findViewById(R.id.category_ImageView);
        categoryTextView = itemView.findViewById(R.id.category_Textview);
        categoryContainer =itemView.findViewById(R.id.category_LinearLayout);
    }
}
