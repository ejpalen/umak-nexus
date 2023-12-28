package com.example.umaknexus;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistory_Adapter extends RecyclerView.Adapter<OrderHistory_Holder>{

    Context context;
    List<OrderHistory_Item> items;

    public OrderHistory_Adapter(Context context, List<OrderHistory_Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public OrderHistory_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderHistory_Holder(LayoutInflater.from(context).inflate(R.layout.orderhistory_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistory_Holder holder, int position) {
        OrderHistory_Item currentItem = items.get(position);
        holder.order_number.setText(currentItem.getOrder_number());
        holder.order_status.setText(currentItem.getOrder_status());
        holder.item_name.setText(currentItem.getItem_name());
        holder.total_price.setText(currentItem.getTotal_price());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
