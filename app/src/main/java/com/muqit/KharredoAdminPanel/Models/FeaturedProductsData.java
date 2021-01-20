package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class FeaturedProductsData {
    @SerializedName("featured_end")
    private String FeaturedEnd;
    @SerializedName("featured_start")
    private String FeaturedStart;
    @SerializedName("id")
    private String ID;
    @SerializedName("name")
    private String Name;
    @SerializedName("photo")
    private String PhotoURL;
    @SerializedName("price")
    private String Price;

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

    public String getFeaturedStart() {
        return this.FeaturedStart;
    }

    public void setFeaturedStart(String str) {
        this.FeaturedStart = str;
    }

    public String getFeaturedEnd() {
        return this.FeaturedEnd;
    }

    public void setFeaturedEnd(String str) {
        this.FeaturedEnd = str;
    }
}
