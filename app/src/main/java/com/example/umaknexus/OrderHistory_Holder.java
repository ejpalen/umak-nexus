package com.example.umaknexus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OrderHistory_Holder extends RecyclerView.ViewHolder {
    TextView order_number, order_status, item_name, item_price, total_price;

    public OrderHistory_Holder(@NonNull View itemView) {
        super(itemView);

        order_number = itemView.findViewById(R.id.order_number);
        order_status = itemView.findViewById(R.id.order_status);
        item_name = itemView.findViewById(R.id.item_name);
        total_price = itemView.findViewById(R.id.total_price);

    }
}
