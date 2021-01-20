package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EmployeesResponse {
    @SerializedName("data")
    private ArrayList<com.muqit.KharredoAdminPanel.Models.EmployeesData> EmployeesData;
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;

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

    public ArrayList<com.muqit.KharredoAdminPanel.Models.EmployeesData> getEmployeesData() {
        return this.EmployeesData;
    }

    public void setEmployeesData(ArrayList<com.muqit.KharredoAdminPanel.Models.EmployeesData> arrayList) {
        this.EmployeesData = arrayList;
    }
}
