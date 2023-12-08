package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

public class InvalidDomain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invalid_domain);

        RelativeLayout backButton = findViewById(R.id.back_to_signin_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InvalidDomain.this, Onboarding_Signin.class);
                startActivity(intent);
                finish();
            }
        });
    }
}