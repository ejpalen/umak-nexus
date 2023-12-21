package com.example.umaknexus;

public class NewArrivals_Products {
    String name,price,productID, productCategory;
    String image;

    public NewArrivals_Products(String name, String price, String image, String productID, String productCategory) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.productID = productID;
        this.productCategory = productCategory;
    }

    public String getName() {
        return name;
    }
    public String getProductCategory() {
        return productCategory;
    }

    public String getPrice() {
        return price;
    }

    public String getproductID(){return productID;}


    public void setPrice(String price) {

        this.price = price;
    }
    public void setName(String name) {

        this.name = name;
    }

    public void setproductID (String productID){
        this.productID = productID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public void setProductCategory (String productCategory){
        this.productCategory = productCategory;
    }
}
