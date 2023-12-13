package com.example.umaknexus;

public class Products {
    String name,price,productID;
    String image;

    public Products(String name, String price, String image, String productID) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.productID = productID;
    }

    public String getName() {
        return name;
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
}
