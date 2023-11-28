package com.example.umaknexus;

public class NewArrivals_Products {
    String name,price;
    int image;

    public NewArrivals_Products(String name, String price, int image) {
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {

        this.price = price;
    }
    public void setName(String name) {

        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
