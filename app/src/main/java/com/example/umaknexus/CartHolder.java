package com.example.umaknexus;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;

public class CartHolder extends RecyclerView.ViewHolder {
//    TextView qty_item;
    ImageView img_product, delete_btn;
    TextView prodName, prodPrice;
    TextView productQtyTextView;
    private int productQty = 1;

    private Button addQty;
    private Button subtractQty;

    public CartHolder(@NonNull View itemView) {
        super(itemView);

        // Find TextView and Button elements in your layout

        img_product = itemView.findViewById(R.id.img_product);
        delete_btn = itemView.findViewById(R.id.delete_btn);
        prodName = itemView.findViewById(R.id.prodName);
        prodPrice = itemView.findViewById(R.id.prodPrice);
        productQtyTextView = itemView.findViewById(R.id.qty_item);
        addQty = itemView.findViewById(R.id.btn_add);
        subtractQty = itemView.findViewById(R.id.btn_subtract);

        // Set initial quantity in TextView
        productQtyTextView.setText(String.valueOf(productQty));

        // Set click listeners for add and subtract buttons
        addQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementQuantity();
            }
        });
        subtractQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrementQuantity();
            }
        });

    }

    private void incrementQuantity() {
        productQty++;
        updateQuantityTextView();
    }

    private void decrementQuantity() {
        if (productQty > 1) {
            productQty--;
            updateQuantityTextView();
        }
        else {
        }
    }
    private void updateQuantityTextView() {
        productQtyTextView.setText(String.valueOf(productQty));
    }
}
