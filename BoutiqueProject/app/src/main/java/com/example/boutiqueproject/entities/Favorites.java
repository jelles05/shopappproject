package com.example.boutiqueproject.entities;
public class Favorites {
    private int id;
    private int productId;
    private int userId;
    public Favorites() {
    }
    public Favorites(int id, int productId, int userId) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getProductId() {
        return productId;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
