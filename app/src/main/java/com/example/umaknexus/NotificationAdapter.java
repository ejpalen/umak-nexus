package com.example.umaknexus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    Context context;
    List<NotificationItem> items;

    public NotificationAdapter(Context context, List<NotificationItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.notifications_item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.notifTitle.setText(items.get(position).getNotifTitle());
        holder.notifDescription.setText(items.get(position).getNotifDesc());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
