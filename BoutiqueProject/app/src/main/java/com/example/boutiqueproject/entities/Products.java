package com.example.boutiqueproject.entities;
public class Products {
    private int id;
    private String name;
    private String description;
    private int categoryId;
    private int subCategoryId;
    private String image;
    private double price;
    private int quantity;
    private String mostSale;
    public Products() {}
    public Products(int id, String name, String description, int categoryId, int subCategoryId, String image, double price, int quantity, String mostSale) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.subCategoryId = subCategoryId;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
        this.mostSale = mostSale;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public int getSubCategoryId() {
        return subCategoryId;
    }
    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getMostSale() {
        return mostSale;
    }
    public void setMostSale(String mostSale) {
        this.mostSale = mostSale;
    }
}
