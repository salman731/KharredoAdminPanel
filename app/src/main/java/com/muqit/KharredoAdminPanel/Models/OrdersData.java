package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class OrdersData {
    @SerializedName("totalAmount")
    private int Amount;
    @SerializedName("sales_date")
    private String Date;
    @SerializedName("id")
    private int Order_ID;
    @SerializedName("status")
    private int Order_Status;
    @SerializedName("totalQuantity")
    private int Quantity;
    @SerializedName("user_id")
    private int User_ID;

    public OrdersData(int i, int i2, int i3, int i4, String str, int i5) {
        this.Order_ID = i;
        this.User_ID = i2;
        this.Amount = i3;
        this.Quantity = i4;
        this.Date = str;
        this.Order_Status = i5;
    }

    public int getOrder_Status() {
        return this.Order_Status;
    }

    public void setOrder_Status(int i) {
        this.Order_Status = i;
    }

    public int getOrder_ID() {
        return this.Order_ID;
    }

    public void setOrder_ID(int i) {
        this.Order_ID = i;
    }

    public int getUser_ID() {
        return this.User_ID;
    }

    public void setUser_ID(int i) {
        this.User_ID = i;
    }

    public int getAmount() {
        return this.Amount;
    }

    public void setAmount(int i) {
        this.Amount = i;
    }

    public int getQuantity() {
        return this.Quantity;
    }

    public void setQuantity(int i) {
        this.Quantity = i;
    }

    public String getDate() {
        return this.Date;
    }

    public void setDate(String str) {
        this.Date = str;
    }
}
