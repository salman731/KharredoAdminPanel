package com.muqit.KharredoAdminPanel.Fragments;

import com.google.gson.annotations.SerializedName;

public class DeleteBannerResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;

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
}
