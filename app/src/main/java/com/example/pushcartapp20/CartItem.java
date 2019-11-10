package com.example.pushcartapp20;

public class CartItem {
    private String productName;
    private int productId;
    private int quantity;
    private byte[] imageUrl;
    private float price;
    private String category;
    private float discount;
    private float promo;

    public CartItem(String productName, int productId, int quantity, float price, String category, float discount, float promo) {
        this.productName = productName;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
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
