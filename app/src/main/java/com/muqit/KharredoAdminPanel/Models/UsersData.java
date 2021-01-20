package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class UsersData {
    @SerializedName("created_on")
    private String Created_Date;
    @SerializedName("email")
    private String Email;
    @SerializedName("firstname")
    private String FirstName;
    @SerializedName("id")
    private String ID;
    @SerializedName("lastname")
    private String LastName;
    @SerializedName("photo")
    private String PhotoURL;
    @SerializedName("status")
    private int Status;

    public String getID() {
        return this.ID;
    }

    public void setID(String str) {
        this.ID = str;
    }

    public String getPhotoURL() {
        return this.PhotoURL;
    }

    public void setPhotoURL(String str) {
        this.PhotoURL = str;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setFirstName(String str) {
        this.FirstName = str;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setLastName(String str) {
        this.LastName = str;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String str) {
        this.Email = str;
    }

    public int getStatus() {
        return this.Status;
    }

    public void setStatus(int i) {
        this.Status = i;
    }

    public String getCreated_Date() {
        return this.Created_Date;
    }

    public void setCreated_Date(String str) {
        this.Created_Date = str;
    }
}
