package com.example.boutiqueproject.entities;
public class Users {
    private int id;
    private String password;
    private String firstName;
    private String lastName ;
    private String email;
    private String address;
    private String city;
    private String isAdmin;
    private String postalCode;
    private String province;
    public Users() {
    }
    public Users(int id, String password, String firstName, String lastName, String email, String address, String city, String isAdmin, String postalCode, String province) {
        this.id = id;

        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.city = city;
        this.isAdmin = isAdmin;
        this.postalCode = postalCode;
        this.province = province;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lasName) {
        this.lastName = lasName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
}
