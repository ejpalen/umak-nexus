package com.example.umaknexus;

public class WishlistItems {
    String wishlistProduct;
    String wishlistPrice;
    String productImage, product_ID;

    public WishlistItems(String wishlistProduct, String wishlistPrice, String productImage, String product_ID) {
        this.wishlistProduct = wishlistProduct;
        this.wishlistPrice = wishlistPrice;
        this.productImage = productImage;
        this.product_ID = product_ID;
    }

    public String getWishlistProduct() { return wishlistProduct; }
    public String getProduct_ID() { return product_ID; }
    public void setProduct_ID(String product_ID) { this.product_ID = product_ID; }

    public void setWishlistProduct(String wishlistProduct) { this.wishlistProduct = wishlistProduct; }

    public String getWishlistPrice() { return wishlistPrice; }

    public void setWishlistPrice(String wishlistPrice) { this.wishlistPrice = wishlistPrice; }

    public String getProductImage() { return productImage; }

    public void setProductImage(String productImage) { this.productImage = productImage; }
}
