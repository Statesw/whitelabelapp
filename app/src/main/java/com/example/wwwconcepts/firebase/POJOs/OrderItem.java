package com.example.wwwconcepts.firebase.POJOs;

public class OrderItem {
    private String price;
    private int quantity;
    private String productId;

    public OrderItem(){}

    public OrderItem(String price, int quantity){
        this.price = price;
        this.quantity=quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
