package com.example.boutiqueproject.entities;
public class Orders {
    private int id;
    private int userId;
    private double amount;
    private double tax;
    private String date;
    public Orders() {
    }
    public Orders(int id, int userId, double amount, double tax, String date) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.tax = tax;
        this.date = date;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public double getTax() {
        return tax;
    }
    public void setTax(double tax) {
        this.tax = tax;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
}
