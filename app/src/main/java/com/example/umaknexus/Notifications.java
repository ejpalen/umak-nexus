package com.example.umaknexus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Notifications extends AppCompatActivity {

    Button allBtn, orderUpdateBtn, systemMaintenanceBtn, feedbackReportBtn, emergencyAlertBtn;
    RecyclerView recyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        allBtn = findViewById(R.id.allBtn);
        orderUpdateBtn = findViewById(R.id.orderUpdateBtn);
        systemMaintenanceBtn = findViewById(R.id.systemMaintenanceBtn);
        feedbackReportBtn = findViewById(R.id.feedbackRequestBtn);
        emergencyAlertBtn = findViewById(R.id.emergencyAlertBtn);
        recyclerView = findViewById(R.id.recyclerview_notifications);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(true);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(false);
                feedbackReportBtn.setSelected(false);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(false);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(true);
                feedbackReportBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(true);

                List<NotificationItem> items = new ArrayList<NotificationItem>();
                items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

                recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
                recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
            }
        });

        orderUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(true);
                systemMaintenanceBtn.setSelected(false);
                feedbackReportBtn.setSelected(false);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(false);
                systemMaintenanceBtn.setEnabled(true);
                feedbackReportBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(true);

                List<NotificationItem> items = new ArrayList<NotificationItem>();
                items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

                recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
                recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
            }
        });

        systemMaintenanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(true);
                feedbackReportBtn.setSelected(false);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(false);
                feedbackReportBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(true);

                List<NotificationItem> items = new ArrayList<NotificationItem>();
                items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

                recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
                recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
            }
        });

        feedbackReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(false);
                feedbackReportBtn.setSelected(true);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(true);
                feedbackReportBtn.setEnabled(false);
                emergencyAlertBtn.setEnabled(true);

                List<NotificationItem> items = new ArrayList<NotificationItem>();
                items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

                recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
                recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
            }
        });

        emergencyAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(false);
                feedbackReportBtn.setSelected(false);
                emergencyAlertBtn.setSelected(true);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(true);
                feedbackReportBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(false);

                List<NotificationItem> items = new ArrayList<NotificationItem>();
                items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

                recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
                recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
            }
        });

        allBtn.setSelected(true);
        List<NotificationItem> items = new ArrayList<NotificationItem>();
        items.add(new NotificationItem("Order Update", "Your Order #52114 is ready to be picked up on COOP Office."));

        recyclerView.setLayoutManager(new LinearLayoutManager(Notifications.this));
        recyclerView.setAdapter(new NotificationAdapter(getApplicationContext(),items));
    }
}
