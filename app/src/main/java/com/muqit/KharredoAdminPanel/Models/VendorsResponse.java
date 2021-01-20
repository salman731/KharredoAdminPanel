package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class VendorsResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("success")
    private String Status;
    @SerializedName("data")
    private ArrayList<VendorsData> vendorsData;

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

    public ArrayList<VendorsData> getVendorsData() {
        return this.vendorsData;
    }

    public void setVendorsData(ArrayList<VendorsData> arrayList) {
        this.vendorsData = arrayList;
    }
}
