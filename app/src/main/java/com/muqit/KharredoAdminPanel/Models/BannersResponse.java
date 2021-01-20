package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BannersResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<BannersData> bannersData;

    public class BannersData {
        @SerializedName("cover")
        private String CoverURL;
        @SerializedName("id")
        private String ID;

        public BannersData() {
        }

        public String getID() {
            return this.ID;
        }

        public void setID(String str) {
            this.ID = str;
        }

        public String getCoverURL() {
            return this.CoverURL;
        }

        public void setCoverURL(String str) {
            this.CoverURL = str;
        }
    }

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

    public ArrayList<BannersData> getBannersData() {
        return this.bannersData;
    }

    public void setBannersData(ArrayList<BannersData> arrayList) {
        this.bannersData = arrayList;
    }
}
