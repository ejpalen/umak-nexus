package com.example.umaknexus;

public class OrderHistory_Item {
    String order_number, order_status, item_name, item_price, total_price, orderDate;

    public OrderHistory_Item(String order_number, String order_status, String item_name, String item_price, String total_price, String orderDate) {
        this.order_number = order_number;
        this.order_status = order_status;
        this.item_name = item_name;
        this.item_price = item_price;
        this.total_price = total_price;
        this.orderDate = orderDate;
    }

    public String getOrder_number() {
        return order_number;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

}
