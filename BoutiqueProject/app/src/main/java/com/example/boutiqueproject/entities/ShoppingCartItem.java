package com.example.boutiqueproject.entities;
public class ShoppingCartItem {
    private int idProduit;
    private int quantity;
    private String size;

    public ShoppingCartItem() { }

    public ShoppingCartItem(int idProduit, int quantity, String size) {
        this.idProduit = idProduit;
        this.quantity = quantity;
        this.size = size;
    }

    public int getIdProduit() {
        return idProduit;
    }
    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getSize() {
        return size;
    }
    public void setSize(String size) {
        this.size = size;
    }
}