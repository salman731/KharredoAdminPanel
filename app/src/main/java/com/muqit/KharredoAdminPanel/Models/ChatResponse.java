package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private ArrayList<ChatData> chatData;

    public class ChatData {
        @SerializedName("id")
        private String ID;
        @SerializedName("firstname")
        private String Name;

        public ChatData() {
        }

        public String getID() {
            return this.ID;
        }

        public void setID(String str) {
            this.ID = str;
        }

        public String getName() {
            return this.Name;
        }

        public void setName(String str) {
            this.Name = str;
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

    public ArrayList<ChatData> getChatData() {
        return this.chatData;
    }

    public void setChatData(ArrayList<ChatData> arrayList) {
        this.chatData = arrayList;
    }
}
