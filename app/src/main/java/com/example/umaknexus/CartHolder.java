package com.example.umaknexus;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartHolder extends RecyclerView.ViewHolder {
    ImageView img_product, delete_btn;
    TextView prodName, prodPrice;
    TextView productQtyTextView;
    Button addQty;
    Button subtractQty;

    public CartHolder(@NonNull View itemView) {
        super(itemView);

        img_product = itemView.findViewById(R.id.img_product);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        prodName = itemView.findViewById(R.id.prodName);
        prodPrice = itemView.findViewById(R.id.prodPrice);
        productQtyTextView = itemView.findViewById(R.id.qty_item);
        addQty = itemView.findViewById(R.id.btn_add);
        subtractQty = itemView.findViewById(R.id.btn_subtract);
    }

    public void bind(Cart_Item currentItem, CartAdapter.OnQuantityChangeListener quantityChangeListener, int position) {
        // Set initial quantity in TextView
        productQtyTextView.setText(String.valueOf(currentItem.getQuantity()));

        // Set click listeners for add and subtract buttons
        addQty.setOnClickListener(v -> {
            quantityChangeListener.onIncrement(currentItem, position);
        });

        subtractQty.setOnClickListener(v -> {
            quantityChangeListener.onDecrement(currentItem, position);
        });
    }
}
