package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Edit_Profile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseUser currentUser = auth.getCurrentUser();
        String displayName = user.getDisplayName();
        String displayEmail = user.getEmail();
        Uri displayImageUri = currentUser.getPhotoUrl();
        TextView userName = findViewById(R.id.user_name);
        userName.setText(displayName);
        TextView userEmail = findViewById(R.id.user_email);
        userEmail.setText(displayEmail);
        CircularImageView circularImageView = findViewById(R.id.profileImage);
        Glide.with(this).load(displayImageUri).into(circularImageView);
        FrameLayout back_btn = findViewById(R.id.backButtonFrame);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });


    }
}