package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class SalesData {
    @SerializedName("firstname")
    private String FirstName;
    @SerializedName("id")
    private String ID;
    @SerializedName("lastname")
    private String LastName;
    @SerializedName("pay_id")
    private String PayID;
    @SerializedName("sales_date")
    private String SalesDate;
    @SerializedName("totalAmount")
    private int TotalAmount;

    public String getID() {
        return this.ID;
    }

    public void setID(String str) {
        this.ID = str;
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

    public String getPayID() {
        return this.PayID;
    }

    public void setPayID(String str) {
        this.PayID = str;
    }

    public int getTotalAmount() {
        return this.TotalAmount;
    }

    public void setTotalAmount(int i) {
        this.TotalAmount = i;
    }

    public String getSalesDate() {
        return this.SalesDate;
    }

    public void setSalesDate(String str) {
        this.SalesDate = str;
    }
}
