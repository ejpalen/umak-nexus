package com.example.umaknexus;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class delete_acc extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 20;
    String email;
    ProgressDialog progressDialogLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_acc);

        //layout reference
        Button delete = findViewById(R.id.delete_btn);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        mAuth = FirebaseAuth.getInstance();

        //Get intent from settings activity
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        //Progress dialog
        progressDialogLogin = new ProgressDialog(this);
        progressDialogLogin.setMessage("Loading...");
        progressDialogLogin.setCancelable(false);

        //Google Sign in options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        LinearLayout back_btn = findViewById(R.id.back_to_profile_btn);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Settings.class));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog();
            }
        });
    }

    //Confirmation dialog before deleting account
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Are you sure you want to continue?")
                .setPositiveButton("Continue", (dialog, which) -> {
                    // Call your method here
                    googleSignIn();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Do nothing or handle cancel action
                })
                .show();
    }

    //Delete current user from Firebase Authentication
    private void deleteCurrentUser() {
        FirebaseUser user = auth.getCurrentUser();

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Deleting Account");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        if (user != null) {
            // Delete the currently authenticated user
            user.delete()
                    .addOnCompleteListener(this, task -> {

                        if (task.isSuccessful()) {
                            deleteUserDataFromFirestore(user.getUid(), progressDialog);
                        } else {
                            Toast.makeText(delete_acc.this, "Failed to delete user: " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    });
        }
    }

    //Delete Firestore data from collections
    private void deleteUserDataFromFirestore(String userId, ProgressDialog progressDialog) {

        //Delete data from users collection
        db.collection("users")
                .document(userId)
                .delete()
                .addOnCompleteListener(task -> {
                    progressDialog.dismiss();

                    if (task.isSuccessful()) {

                    } else {
                        Toast.makeText(delete_acc.this, "Failed to delete user data: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Delete data from whishlist collection
        db.collection("wishlist")
                .whereEqualTo("products.userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                    } else {
                                        Toast.makeText(delete_acc.this, "Failed to delete Wishlist: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

        //Delete data from cart collection
        db.collection("cart")
                .whereEqualTo("userID", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(delete_acc.this, "Failed to delete products from cart : " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void googleSignIn() {
        progressDialogLogin.show();

        // Force account selection by creating a new GoogleSignInOptions
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
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
                progressDialogLogin.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Sign in again to Google to Confirm, skip when user is recently signed in
    private void firbaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    progressDialogLogin.dismiss();
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        String userEmail = user != null ? user.getEmail() : "";

                        if (userEmail != null && userEmail.equals(email)) {
                            deleteCurrentUser();
                        } else {
                            Toast.makeText(delete_acc.this, "Select the logged in account", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Sign-in failed
                        Toast.makeText(delete_acc.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}