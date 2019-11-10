package com.example.pushcartapp20;

import java.sql.Blob;

public class GroceryItem {

    private String productName;
    private int productId;
    private byte[] imageUrl;
    private int stock;
    private float price;
    private String category;
    private String description;
    private float discount = 0;
    private float promo;

    public GroceryItem( int productId,String productName, float price, int stock, String category, String description, byte[] imageUrl, float discount, float promo) {
        this.productName = productName;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.price = price;
        this.category = category;
        this.description = description;
        this.stock = stock;
        this.discount = discount;
        this.promo = promo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPromo() {
        return promo;
    }

    public void setPromo(float promo) {
        this.promo = promo;
    }
}
