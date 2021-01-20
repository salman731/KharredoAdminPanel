package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class CartData {

    @SerializedName("id")
    public String ID;

    @SerializedName("name")
    public String ProductName;

    @SerializedName("quantity")
    public String Quantity;

    public String getID() {
        return ID;
    }

    public void setID(String UID) {
        this.ID = UID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}
