package com.example.umaknexus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartHolder> {

    private Context context;
    private List<Cart_Item> items;
    private TextView productQtyTextView;
    private int productQty = 1;

    public CartAdapter(Context context, List<Cart_Item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartHolder(LayoutInflater.from(context).inflate(R.layout.cart_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Cart_Item currentItem = items.get(position);

        holder.prodName.setText(currentItem.getProdName());
        holder.prodPrice.setText(currentItem.getProdPrice());
        holder.productQtyTextView.setText(currentItem.getQty_item());
        holder.delete_btn.setImageResource(currentItem.getDelete_btn());
        Glide.with(context).load(currentItem.getImg_product()).into(holder.img_product);

        TextView productNameTextView = holder.itemView.findViewById(R.id.prodName);
        TextView productCategoryTextView = holder.itemView.findViewById(R.id.category);
        productQtyTextView = holder.itemView.findViewById(R.id.qty_item);
        TextView productPriceTextView = holder.itemView.findViewById(R.id.price);
        Button addQty = holder.itemView.findViewById(R.id.btn_add);
        Button subtractQty = holder.itemView.findViewById(R.id.btn_subtract);
        ImageView productImageView = holder.itemView.findViewById(R.id.img_product);

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//
//        FirebaseUser currentUser = auth.getCurrentUser();
//        if (currentUser != null) {
//            String userID = currentUser.getUid();
//
//            CollectionReference userRef = db.collection("products");
//
//            // Query to get the document where the "id" field matches the user ID
//            userRef.whereEqualTo(FieldPath.documentId(), userID)
//                    .get()
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                // Retrieve other data from the document
//                                String imageUrl = document.getString("Image");
//                                String productCat = document.getString("category");
//                                String productName = document.getString("product_name");
//                                String productPrice = document.getString("product_price");
//                                Double productQty = document.getDouble("product_quantity");
//                                String productQtyString = String.valueOf(productQty);
//
//                                holder.prodName.setText(productName);
//                                holder.prodPrice.setText(productPrice);
//                                holder.productQtyTextView.setText(productQtyString);
//                                holder.delete_btn.setImageResource(currentItem.getDelete_btn());
//                                Glide.with(context).load(imageUrl).into(holder.img_product);
//
////                                productNameTextView.setText(productName);
////                                productPriceTextView.setText(productPrice);
////
////                                // Load the image into ImageView using Glide or another library
////                                Glide.with(context).load(imageUrl).into(productImageView);
//
//                                // Use the document ID as needed
//                                Log.d("Document ID", document.getId());
//                            }
//                        } else {
//                            // Handle errors
//                            Toast.makeText(context, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
//                        }
//                    });
//        } else {
//            // Handle the case where productID is null
//            Toast.makeText(context, "Current user is null", Toast.LENGTH_SHORT).show();
//        }

        // Set initial quantity in TextView
        productQtyTextView.setText(String.valueOf(productQty));

        // Set click listeners for add and subtract buttons
        addQty.setOnClickListener(v -> incrementQuantity());

        subtractQty.setOnClickListener(v -> decrementQuantity());
    }

    private void incrementQuantity() {
        productQty++;
        updateQuantityTextView();
    }

    private void decrementQuantity() {
        if (productQty > 1) {
            productQty--;
            updateQuantityTextView();
        }
    }

    private void updateQuantityTextView() {
        productQtyTextView.setText(String.valueOf(productQty));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
