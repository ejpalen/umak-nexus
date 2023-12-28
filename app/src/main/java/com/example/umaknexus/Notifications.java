package com.example.umaknexus;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Notifications extends AppCompatActivity {

    private static FirebaseFirestore database;
    private static List<NotificationItem> items;
    private static NotificationAdapter Notification_Adapter;
    private static FirebaseFirestore db;
    private FirebaseAuth auth;
    FirebaseUser user;
    static String userId;
    Button allBtn, orderUpdateBtn, systemMaintenanceBtn, emergencyAlertBtn;
    RecyclerView recyclerView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        //Initialize Firebase
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        //Layout Reference
        allBtn = findViewById(R.id.allBtn);
        orderUpdateBtn = findViewById(R.id.orderUpdateBtn);
        systemMaintenanceBtn = findViewById(R.id.systemMaintenanceBtn);
        emergencyAlertBtn = findViewById(R.id.emergencyAlertBtn);
        recyclerView = findViewById(R.id.recyclerview_notifications);

        //Intialze Notification Items for Recyclerview
        items = new ArrayList<>();
        Notification_Adapter = new NotificationAdapter(getApplicationContext(), items);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(Notification_Adapter);

        getNotificationItems("All");

        //All Button is clicked
        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(true);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(false);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(false);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(true);

                items.clear();
                getNotificationItems("All");

            }
        });

        //Order Updates Button is clicked
        orderUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(true);
                systemMaintenanceBtn.setSelected(false);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(false);
                systemMaintenanceBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(true);

                items.clear();
                getNotificationItems("Order Update");

            }
        });

        //System Maintenance Button is clicked
        systemMaintenanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(true);
                emergencyAlertBtn.setSelected(false);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(false);
                emergencyAlertBtn.setEnabled(true);

                items.clear();
                getNotificationItems("System Maintenance");

            }
        });

        //Emergency Alerts Button is clicked
        emergencyAlertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allBtn.setSelected(false);
                orderUpdateBtn.setSelected(false);
                systemMaintenanceBtn.setSelected(false);
                emergencyAlertBtn.setSelected(true);

                allBtn.setEnabled(true);
                orderUpdateBtn.setEnabled(true);
                systemMaintenanceBtn.setEnabled(true);
                emergencyAlertBtn.setEnabled(false);

                items.clear();
                getNotificationItems("Emergency Alert");

            }
        });

        allBtn.setSelected(true);

        //Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_notifications);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bottom_home) {
                startActivity(new Intent(getApplicationContext(), Home.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_shop) {
                startActivity(new Intent(getApplicationContext(), Shop_Products.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_cart) {
                startActivity(new Intent(getApplicationContext(), Cart_Page.class));
                finish();
                return true;
            } else if (item.getItemId() == R.id.bottom_notifications) {
                return true;
            } else if (item.getItemId() == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                finish();
                return true;
            }

            return false;
        });
    }

    //Retrieve Notification Items from Firebase
    public void getNotificationItems(String notificationFilter) {
        CollectionReference notificationsCollection = (CollectionReference) database.collection("notifications");

        Query query;
        if ("All".equals(notificationFilter) || notificationFilter == null || notificationFilter.isEmpty()) {
            query = notificationsCollection
                    .whereIn("userID", Arrays.asList(userId, "admin"))  // Include notifications without userID
                    .orderBy("time", Query.Direction.DESCENDING);
        } else if ("System Maintenance".equals(notificationFilter) || "Emergency Alert".equals(notificationFilter)) {
            query = notificationsCollection
                    .whereIn("userID", Arrays.asList("admin"))
                    .whereEqualTo("type", notificationFilter)
                    .orderBy("time", Query.Direction.DESCENDING);
        } else {
            query = notificationsCollection
                    .whereIn("userID", Arrays.asList(userId))
                    .whereIn("type", Arrays.asList(notificationFilter))
                    .orderBy("time", Query.Direction.DESCENDING);
        }

        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.e("Firestore error: ", error.getMessage());
                return;
            }

            items.clear(); // Clear existing items
            for (DocumentChange dc : value.getDocumentChanges()) {
                if (dc.getType() == DocumentChange.Type.ADDED) {
                    String title = dc.getDocument().getString("title");
                    String message = dc.getDocument().getString("message");

                    if (title != null && message != null) {
                        items.add(new NotificationItem(title, message));
                    } else {
                        Log.e("Firestore error: ", "One or more fields are null.");
                    }
                }
            }

            Notification_Adapter.notifyDataSetChanged();
        });

    }

}
