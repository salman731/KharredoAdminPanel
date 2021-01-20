package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductsResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<ProductsData> productsData;

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

    public ArrayList<ProductsData> getProductsData() {
        return this.productsData;
    }

    public void setProductsData(ArrayList<ProductsData> arrayList) {
        this.productsData = arrayList;
    }
}
