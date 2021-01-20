package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class ChatHistoryData {
    @SerializedName("firstname")
    private String FirstName;
    @SerializedName("message")
    private String Message;
    @SerializedName("id")
    private String ID;
    @SerializedName("user_id")
    private String UserID;

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
