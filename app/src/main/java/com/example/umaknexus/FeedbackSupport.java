package com.example.umaknexus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.Map;
import java.util.HashMap;

public class FeedbackSupport extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_support);
        Button FeedbackSendBtn = findViewById(R.id. FeedbackSend);
        LinearLayout backBtn = findViewById(R.id.linearLayout2);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });

        //Feedback Submit Button is Clicked
        FeedbackSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RatingBar ratingBar = findViewById(R.id.ratingBar);
                EditText WriteReview = findViewById(R.id.WriteReview);
                float RatingStar = ratingBar.getRating();
                String DisplayName = user.getDisplayName();
                String Review = WriteReview.getText().toString();

                Map<String, Object> feedbackData = new HashMap<>();
                feedbackData.put("DisplayName", DisplayName);
                feedbackData.put("Review", Review);
                feedbackData.put("ratingBar", RatingStar);

                Map<String, Object> feedback = new HashMap<>();
                feedback.put("review", feedbackData);

                db.collection("feedback").add(feedback).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Feedback Submitted.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}



