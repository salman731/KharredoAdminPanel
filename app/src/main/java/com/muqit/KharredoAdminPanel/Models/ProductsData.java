package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class ProductsData {
    @SerializedName("description")
    private String Description;
    @SerializedName("id")
    private String ID;
    @SerializedName("name")
    private String Name;
    @SerializedName("photo")
    private String PhotoURL;
    @SerializedName("price")
    private String Price;
    @SerializedName("qty")
    private String Quantity;
    @SerializedName("sale")
    private String Sale;

    public String getID() {
        return this.ID;
    }

    public void setID(String str) {
        this.ID = str;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String str) {
        this.Name = str;
    }

    public String getPrice() {
        return this.Price;
    }

    public void setPrice(String str) {
        this.Price = str;
    }

    public String getPhotoURL() {
        return this.PhotoURL;
    }

    public void setPhotoURL(String str) {
        this.PhotoURL = str;
    }

    public String getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(String str) {
        this.Quantity = str;
    }

    public String getSale() {
        return this.Sale;
    }

    public void setSale(String str) {
        this.Sale = str;
    }

    public String getDescription() {
        return this.Description;
    }

    public void setDescription(String str) {
        this.Description = str;
    }
}
