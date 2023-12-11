package com.example.umaknexus;

public class WishlistItems {
    String wishlistProduct;
    String wishlistPrice;
    int productImage;

    public WishlistItems(String wishlistProduct, String wishlistPrice, int productImage) {
        this.wishlistProduct = wishlistProduct;
        this.wishlistPrice = wishlistPrice;
        this.productImage = productImage;
    }

    public String getWishlistProduct() { return wishlistProduct; }

    public void setWishlistProduct(String wishlistProduct) { this.wishlistProduct = wishlistProduct; }

    public String getWishlistPrice() { return wishlistPrice; }

    public void setWishlistPrice(String wishlistPrice) { this.wishlistPrice = wishlistPrice; }

    public int getProductImage() { return productImage; }

    public void setProductImage(int productImage) { this.productImage = productImage; }
}
