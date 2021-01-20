package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class VendorsData {
    @SerializedName("created_on")
    private String Date_Created;
    @SerializedName("email")
    private String Email;
    @SerializedName("firstname")
    private String FirstName;
    @SerializedName("id")
    private String ID;
    @SerializedName("lastname")
    private String LastName;
    @SerializedName("role")
    private String Role;
    @SerializedName("status")
    private int Status;
    @SerializedName("CnicImage")
    private String VendorCNICURL;
    @SerializedName("photo")
    private String VendorPhotoURL;

    public String getID() {
        return this.ID;
    }

    public void setID(String str) {
        this.ID = str;
    }

    public String getVendorPhotoURL() {
        return this.VendorPhotoURL;
    }

    public void setVendorPhotoURL(String str) {
        this.VendorPhotoURL = str;
    }

    public String getVendorCNICURL() {
        return this.VendorCNICURL;
    }

    public void setVendorCNICURL(String str) {
        this.VendorCNICURL = str;
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

    public String getDate_Created() {
        return this.Date_Created;
    }

    public void setDate_Created(String str) {
        this.Date_Created = str;
    }

    public String getRole() {
        return this.Role;
    }

    public void setRole(String str) {
        this.Role = str;
    }
}
