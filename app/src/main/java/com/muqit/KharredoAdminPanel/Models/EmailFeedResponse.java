package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmailFeedResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<EmailFeedData> emailFeedData;

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String str) {
        this.Status = str;
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String str) {
        this.Message = str;
    }

    public ArrayList<EmailFeedData> getEmailFeedData() {
        return this.emailFeedData;
    }

    public void setEmailFeedData(ArrayList<EmailFeedData> arrayList) {
        this.emailFeedData = arrayList;
    }
}
