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

    public interface OnItemRemoveListener {
        void onItemRemove(Cart_Item item);
    }

    private OnItemRemoveListener onItemRemoveListener;

    public void setOnItemRemoveListener(OnItemRemoveListener listener) {
        this.onItemRemoveListener = listener;
    }


    public CartAdapter(Context context, List<Cart_Item> items) {
        this.context = context;
        this.items = items;
    }

    public interface OnQuantityChangeListener {
        void onIncrement(Cart_Item item, int position);
        void onDecrement(Cart_Item item, int position);
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
        holder.productQtyTextView.setText(String.valueOf(currentItem.getQuantity()));
        holder.delete_btn.setImageResource(currentItem.getDelete_btn());
        Glide.with(context).load(currentItem.getImg_product()).into(holder.img_product);

        // Set click listeners for add and subtract buttons
        holder.addQty.setOnClickListener(v -> incrementQuantity(holder, currentItem));
        holder.subtractQty.setOnClickListener(v -> decrementQuantity(holder, currentItem));

        holder.delete_btn.setOnClickListener(v -> {
            // Call the method to remove the item from the cart and Firestore
            removeItem(currentItem, position);
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            String userID = currentUser.getUid();

            CollectionReference cartRef = db.collection("cart");

            // Query to get documents where the "userID" field matches the user ID
            cartRef.whereEqualTo("userID", userID)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String imageUrl = document.getString("product_image");
                                String productName = document.getString("product_name");
                                String productPrice = document.getString("product_subtotal");

                                // Get product_quantity as Double
                                Double productQtyDouble = document.getDouble("product_quantity");

                                // Convert Double to int
                                int productQty = (productQtyDouble != null) ? productQtyDouble.intValue() : 0;
                                String productQtyString = String.valueOf(productQty);

                                // Assuming you have a Cart_Item class with appropriate fields
                                Cart_Item cartItem = new Cart_Item(productName, productPrice, productQtyString, R.drawable.delete_btn, imageUrl, productQty);

                                // Update UI or perform other actions with cartItem
                                // For example, you might add it to a list or display it in a RecyclerView

                                // Use the document ID as needed
                                Log.d("Document ID", document.getId());
                            }
                        } else {
                            // Handle errors
                            Toast.makeText(context, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Handle the case where the current user is null
            Toast.makeText(context, "Current user is null", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeFromFirestore(String itemId) {
        // Replace the comments with the actual logic to delete the document from Firestore
        // Example assuming you have a "cart" collection:
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart").document(itemId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Document successfully deleted
                    Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.w("Firestore", "Error removing product", e);
                });
    }

    private void removeItem(Cart_Item currentItem, int position) {
        // Remove the item from Firestore
        removeFromFirestore(currentItem.getDocumentId());

        // Notify the listener that an item is removed before updating the local list
        if (onItemRemoveListener != null) {
            onItemRemoveListener.onItemRemove(currentItem);
        }

        // Remove the item from the local list
        items.remove(position);
        notifyItemRemoved(position);
    }

    public String getItemId(Cart_Item currentItem) {
        // Replace the comments with the actual logic to get the unique identifier for the item
        // In this example, assuming you have a method in Cart_Item to retrieve the document ID:
        return currentItem.getDocumentId();
    }

    private void incrementQuantity (CartHolder holder, Cart_Item currentItem){
        int quantity = currentItem.getQuantity() + 1;
        currentItem.setQuantity(quantity);
        holder.productQtyTextView.setText(String.valueOf(quantity));
    }

    private void decrementQuantity (CartHolder holder, Cart_Item currentItem){
        int quantity = currentItem.getQuantity();
        if (quantity > 1) {
            quantity--;
            currentItem.setQuantity(quantity);
            holder.productQtyTextView.setText(String.valueOf(quantity));
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}