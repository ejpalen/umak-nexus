package com.example.umaknexus;

public class Cart_Item {
    private String prodName;
    private String prodPrice;
    private String qty_item;
    private int delete_btn;
    private String img_product;

    private int quantity;

    // Add a field to store the document ID
    private String documentId;

    // No-argument constructor
    public Cart_Item() {
        // Default constructor required for Firestore
    }

    public Cart_Item(String prodName, String prodPrice, String qty_item, int delete_btn, String img_product, int quantity) {
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.qty_item = qty_item;
        this.delete_btn = delete_btn;
        this.img_product = img_product;
        this.quantity = quantity;
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

    public int getDelete_btn() {
        return delete_btn;
    }

    public void setDelete_btn(int delete_btn) {
        this.delete_btn = delete_btn;
    }

    public String getImg_product() {
        return img_product;
    }

    public void setImg_product(String img_product) {
        this.img_product = img_product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}
