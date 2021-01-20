package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class CategoriesData {
    @SerializedName("category_id")
    private String Category_ID;
    @SerializedName("commission")
    private String Commission;
    @SerializedName("id")
    private String ID;
    @SerializedName("name")
    private String Name;
    @SerializedName("Pname")
    private String PName;

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    @SerializedName("photo")
    private String PhotoURL;

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

    public String getPhotoURL() {
        return this.PhotoURL;
    }

    public void setPhotoURL(String str) {
        this.PhotoURL = str;
    }

    public String getCategory_ID() {
        return this.Category_ID;
    }

    public void setCategory_ID(String str) {
        this.Category_ID = str;
    }

    public String getCommission() {
        return this.Commission;
    }

    public void setCommission(String str) {
        this.Commission = str;
    }
}
