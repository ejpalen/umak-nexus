package com.example.umaknexus;

public class Categories {
    String Category_name;
    String icon;

    public Categories(String Category_name, String icon) {
        this.Category_name = Category_name;
        this.icon = icon;
    }

    public String getName() {
        return Category_name;
    }

    public void setName(String Category_name) {
        this.Category_name = Category_name;
    }

    public String getImage() {
        return icon;
    }

    public void setImage(String icon) {
        this.icon = icon;
    }
}
