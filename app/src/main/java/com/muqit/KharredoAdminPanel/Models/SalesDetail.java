package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class SalesDetail {

    @SerializedName("name")
    public String ProductName;

    @SerializedName("price")
    public String Price;

    @SerializedName("totalQuantity")
    public String TotalQuantity;

    @SerializedName("totalAmount")
    public String TotalAmount;

    @SerializedName("sales_date")
    public String SalesDate;

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getSalesDate() {
        return SalesDate;
    }

    public void setSalesDate(String salesDate) {
        SalesDate = salesDate;
    }
}
