package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoriesResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<CategoriesData> categoriesData;

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

    public ArrayList<CategoriesData> getCategoriesData() {
        return this.categoriesData;
    }

    public void setCategoriesData(ArrayList<CategoriesData> arrayList) {
        this.categoriesData = arrayList;
    }
}
