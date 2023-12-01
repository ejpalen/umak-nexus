package com.example.umaknexus;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    TextView notifTitle, notifDescription;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        notifTitle = itemView.findViewById(R.id.notifTitle);
        notifDescription = itemView.findViewById(R.id.notifDescription);
    }
}
