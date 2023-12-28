package com.example.umaknexus;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class Order_Confirmation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderconfirmation);

        // Retrieve order ID from the intent
        String orderId = getIntent().getStringExtra("order_id");
        String generatedOrderID = getIntent().getStringExtra("generated_order_id");

        // Update TextViews with the retrieved values
        TextView orderStatusTextView = findViewById(R.id.orderStatus);
        TextView orderTitleTextView = findViewById(R.id.orderTitle);
        TextView nameTextView = findViewById(R.id.name);
        TextView dateTextView = findViewById(R.id.date);
        TextView totalAmountTextView = findViewById(R.id.total);

        // Fetch total amount from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .document(orderId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve the total amount from the document
                                double totalAmount = document.getDouble("total_amount");

                                // Retrieve user's name and purchase date from the document
                                String userName = document.getString("user_name");
                                String purchaseDate = document.getString("purchase_date");

                                // Update TextViews with the retrieved values
                                orderStatusTextView.setText(getString(R.string.order_status_message));
                                orderTitleTextView.setText(getString(R.string.order_number_message, generatedOrderID));
                                nameTextView.setText(userName);
                                dateTextView.setText(purchaseDate);

                                // Set the total amount directly
                                totalAmountTextView.setText(getString(R.string.total_amount_message, String.format(Locale.getDefault(), "%.2f", totalAmount)));
                            }
                        }
                    }
                });

        // Close button click listener
        Button close = findViewById(R.id.btn_close);
        close.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Home.class));
            finish();
        });
    }
}
