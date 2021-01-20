package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AffiliateMarketersResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<AffiliateMarketersData> affiliateMarketersData;

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

    public ArrayList<AffiliateMarketersData> getAffiliateMarketersData() {
        return this.affiliateMarketersData;
    }

    public void setAffiliateMarketersData(ArrayList<AffiliateMarketersData> arrayList) {
        this.affiliateMarketersData = arrayList;
    }
}
