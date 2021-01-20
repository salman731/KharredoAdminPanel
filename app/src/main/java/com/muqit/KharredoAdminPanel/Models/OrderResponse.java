package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OrderResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("data")
    private ArrayList<com.muqit.KharredoAdminPanel.Models.OrdersData> OrdersData;
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

    public ArrayList<com.muqit.KharredoAdminPanel.Models.OrdersData> getOrdersData() {
        return this.OrdersData;
    }

    public void setOrdersData(ArrayList<com.muqit.KharredoAdminPanel.Models.OrdersData> arrayList) {
        this.OrdersData = arrayList;
    }
}
