package com.example.umaknexus;

public class WishlistItems {
    String wishlistProduct;
    String wishlistPrice;
    String productImage;

    public WishlistItems(String wishlistProduct, String wishlistPrice, String productImage) {
        this.wishlistProduct = wishlistProduct;
        this.wishlistPrice = wishlistPrice;
        this.productImage = productImage;
    }

    public String getWishlistProduct() { return wishlistProduct; }

    public void setWishlistProduct(String wishlistProduct) { this.wishlistProduct = wishlistProduct; }

    public String getWishlistPrice() { return wishlistPrice; }

    public void setWishlistPrice(String wishlistPrice) { this.wishlistPrice = wishlistPrice; }

    public String getProductImage() { return productImage; }

    public void setProductImage(String productImage) { this.productImage = productImage; }
}
