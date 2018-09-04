package com.example.wwwconcepts.firebase.POJOs;

public class OrderItem {
    private String price;
    private String quantity;
    private String productId;

    public OrderItem(){}

    public OrderItem(String price, String quantity, String productId){
        this.price = price;
        this.quantity=quantity;
        this.productId=productId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
