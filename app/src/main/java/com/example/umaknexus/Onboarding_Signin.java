package com.example.umaknexus;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import android.app.ProgressDialog;

public class Onboarding_Signin extends AppCompatActivity {
    ProgressDialog progressDialog;
    RelativeLayout signinButton;
    FirebaseAuth auth;
    FirebaseFirestore database;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;

    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_signin);

        //Layout Reference
        signinButton = findViewById(R.id.signin_btn);

        //Initialize Firebase and Google
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in...");
        progressDialog.setCancelable(false);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
            }
        });

    }
    private void googleSignIn() {
        progressDialog.show();

        // Force account selection by creating a new GoogleSignInOptions
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .setHostedDomain("umak.edu.ph") // Specify your hosted domain
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firbaseAuth(account.getIdToken());
            } catch (ApiException e) {
                progressDialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Handle Signing in and adding data to Firebase Authentication and Firestore
    private void firbaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        // Check if the user's email has the allowed domain
                        String allowedDomain = "umak.edu.ph";
                        String userEmail = user != null ? user.getEmail() : "";

                        if (userEmail != null && userEmail.endsWith("@" + allowedDomain)) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("id", user.getUid());
                            map.put("email", user.getEmail());
                            map.put("name", user.getDisplayName());

                            //Add data if user does not exist and merge if exists
                            database.collection("users").document(user.getUid()).set(map, SetOptions.merge())
                                    .addOnCompleteListener(task1 -> {
                                        Intent intent = new Intent(Onboarding_Signin.this, Home.class);
                                        startActivity(intent);
                                        finish();
                                    });


                        } else {
                            auth.signOut();
                            mGoogleSignInClient.signOut()
                                    .addOnCompleteListener(this, signOutTask -> {
                                        Intent intent = new Intent(Onboarding_Signin.this, InvalidDomain.class);
                                        startActivity(intent);
                                    });
                        }
                    } else {
                        // Sign-in failed
                        Toast.makeText(Onboarding_Signin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}