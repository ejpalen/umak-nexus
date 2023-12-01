package com.example.umaknexus;

public class Cart_Item {
    String prodName, prodPrice, qty_item;
    int img_product, delete_btn;

    public Cart_Item(String prodName, String prodPrice, String qty_item, int img_product, int delete_btn) {
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.qty_item = qty_item;
        this.img_product = img_product;
        this.delete_btn = delete_btn;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getQty_item() {
        return qty_item;
    }

    public void setQty_item(String qty_item) {
        this.qty_item = qty_item;
    }

    public int getImg_product() {
        return img_product;
    }

    public void setImg_product(int img_product) {
        this.img_product = img_product;
    }

    public int getDelete_btn() {
        return delete_btn;
    }

    public void setDelete_btn(int delete_btn) {
        this.delete_btn = delete_btn;
    }

}
