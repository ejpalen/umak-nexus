package com.example.umaknexus;

import static com.example.umaknexus.R.id.bottomNavigation;
import static com.example.umaknexus.R.id.bottomNavigation1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(getApplicationContext(), FeedbackSupport.class));

        Button getStartedBtn = findViewById(R.id.getStarted_btn);
        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation1);

        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FeedbackSupport.class));
            }
        });

    }
}