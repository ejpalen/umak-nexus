package com.example.umaknexus;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.umaknexus.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Edit_Profile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    ActivityMainBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    CircularImageView circularImageView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        String userId = user.getUid();

        //Layout Reference
        circularImageView = findViewById(R.id.profileImage);
        circularImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String displayName = user.getDisplayName();
        String displayEmail = user.getEmail();
        TextView userName = findViewById(R.id.user_name_edit);
        userName.setText(displayName);
        TextView userEmail = findViewById(R.id.user_email);
        userEmail.setText(displayEmail);
        Button btnSave = findViewById(R.id.btn_save);
        CircularImageView selectImage = findViewById(R.id.cameraImage);

        setUserImage(userId);

        //Button cannot be clicked initially unless user changed photo
        btnSave.setEnabled(false);
        if (!btnSave.isEnabled()) {
            btnSave.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#669AFF")));

        FrameLayout back_btn = findViewById(R.id.backButtonFrame);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    //Set user image
    private void setUserImage(String userId){
        // Reference to the "users" collection
        CollectionReference usersRef = db.collection("users");

        // Query to get the document where the "id" field matches the user ID
        usersRef.whereEqualTo("id", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve the image URL from the document
                            String imageUrl = document.getString("profile");

                            if (imageUrl != null && !imageUrl.isEmpty() && !imageUrl.equals("")){
                                Glide.with(this).load(imageUrl).into(circularImageView);
                                Log.e("Image exist!!", imageUrl);
                            }
                            else{
                                Glide.with(this).load(user.getPhotoUrl().toString()).into(circularImageView);
                                Log.e("Image does not exist!!", user.getPhotoUrl().toString());
                            }
                        }
                    } else {
                        // Handle errors
                        Toast.makeText(Edit_Profile.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Handle uploading of image to Firebase Storage
    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Saving Changes....");
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUri) {
                                // Update the "profile" field of the signed-in user with the download URL
                                String imageUrl = downloadUri.toString();
                                updateProfileImage(imageUrl);
                            }
                        });

                        circularImageView.setImageURI(null);
                        if (progressDialog.isShowing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }

                        Toast.makeText(Edit_Profile.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (IllegalArgumentException ex) {
                                ex.printStackTrace();
                            }
                        }
                        Toast.makeText(Edit_Profile.this, "Failed to Upload", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Update profile image field in Firestore of current user
    private void updateProfileImage(String imageUrl) {
        // Update the "profile" field of the signed-in user with the image URL
        db.collection("users")
                .document(user.getUid())
                .update("profile", imageUrl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Handle successful update
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failed update
                        Toast.makeText(Edit_Profile.this, "Failed to update profile image", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    //Get selected image from Files Viewer
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri = data.getData();
            circularImageView.setImageURI(imageUri);

            Button btnSave = findViewById(R.id.btn_save);
            btnSave.setEnabled(true);
            if (btnSave.isEnabled()) {
                btnSave.setBackgroundTintList(null);
            }

        }
    }
}