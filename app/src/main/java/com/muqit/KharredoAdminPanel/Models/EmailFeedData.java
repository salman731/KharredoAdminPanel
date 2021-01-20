package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class EmailFeedData {
    @SerializedName("email")
    private String Email;
    @SerializedName("id")
    private int ID;

    public EmailFeedData(String str, int i) {
        this.Email = str;
        this.ID = i;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String str) {
        this.Email = str;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int i) {
        this.ID = i;
    }
}
