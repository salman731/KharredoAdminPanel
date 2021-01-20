package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BlogResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<BlogData> blogData;

    public class BlogData {
        @SerializedName("id")
        private String ID;
        @SerializedName("t_img")
        private String PhotoURL;
        @SerializedName("title")
        private String Title;

        public BlogData() {
        }

        public String getID() {
            return this.ID;
        }

        public void setID(String str) {
            this.ID = str;
        }

        public String getTitle() {
            return this.Title;
        }

        public void setTitle(String str) {
            this.Title = str;
        }

        public String getPhotoURL() {
            return this.PhotoURL;
        }

        public void setPhotoURL(String str) {
            this.PhotoURL = str;
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

    public ArrayList<BlogData> getBlogData() {
        return this.blogData;
    }

    public void setBlogData(ArrayList<BlogData> arrayList) {
        this.blogData = arrayList;
    }
}
