package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Switch pushNotificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Initialize Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        pushNotificationSwitch = findViewById(R.id.push_notif_switch);

        // Set an OnCheckedChangeListener to detect changes in the switch state
        pushNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Call the function to update the database field
                updatePushNotifications(isChecked);
            }
        });

        // Load the current state of push notifications from the database and set the switch accordingly
        loadPushNotificationState();

        LinearLayout backBtn = findViewById(R.id.linearLayout2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });

        LinearLayout deleteBtn = findViewById(R.id.deletebutton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, delete_acc.class);
                intent.putExtra("email", user.getEmail().toString());
                startActivity(intent);
                finish();
            }
        });

    }

    private void loadPushNotificationState() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            db.collection("users")
                    .document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().exists()) {
                                Object pushNotificationsValue = task.getResult().get("push_notifications");

                                // Check if the value is not null and is of type Boolean
                                if (pushNotificationsValue instanceof Boolean) {
                                    // Cast the value to boolean and set the switch state accordingly
                                    boolean pushNotificationsEnabled = (boolean) pushNotificationsValue;
                                    pushNotificationSwitch.setChecked(pushNotificationsEnabled);
                                } else {
                                    pushNotificationSwitch.setChecked(true); // Set a default value, for example
                                }
                            }
                        }
                    });
        }
    }

    private void updatePushNotifications(boolean isEnabled) {
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();

            // Create a map to update the "push_notifications" field in the database
            Map<String, Object> updates = new HashMap<>();
            updates.put("push_notifications", isEnabled);

            // Update the document in the "users" collection
            db.collection("users")
                    .document(userId)
                    .update(updates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                        }
                    });
        }
    }
}