package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class ChattingData {
    @SerializedName("firstname")
    private String FirstName;
    @SerializedName("message")
    private String Message;
    @SerializedName("id")
    private String ID;
    @SerializedName("user_id")
    private String UserID;
    @SerializedName("client")
    private String Client;
    @SerializedName("sender_id")
    private String SenderID;
    @SerializedName("lastname")
    private String LastName;
    @SerializedName("created_at")
    private String CreatedAt;

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

    public String getClient() {
        return Client;
    }

    public void setClient(String client) {
        Client = client;
    }

    public String getSenderID() {
        return SenderID;
    }

    public void setSenderID(String senderID) {
        SenderID = senderID;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
