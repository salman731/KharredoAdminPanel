package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class EmployeeDetailData {
    @SerializedName("data")
    private EmployeeData Employeedata;
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;

    public class EmployeeData {
        @SerializedName("address")
        private String Address;
        @SerializedName("email")
        private String Email;
        @SerializedName("firstname")
        private String FirstName;
        @SerializedName("lastname")
        private String LastName;
        @SerializedName("passwords")
        private String Password;
        @SerializedName("phone")
        private String Phone;

        public EmployeeData() {
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

        public String getEmail() {
            return this.Email;
        }

        public void setEmail(String str) {
            this.Email = str;
        }

        public String getPassword() {
            return this.Password;
        }

        public void setPassword(String str) {
            this.Password = str;
        }

        public String getPhone() {
            return this.Phone;
        }

        public void setPhone(String str) {
            this.Phone = str;
        }

        public String getAddress() {
            return this.Address;
        }

        public void setAddress(String str) {
            this.Address = str;
        }
    }

    public String getMessage() {
        return this.Message;
    }

    public void setMessage(String str) {
        this.Message = str;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String str) {
        this.Status = str;
    }

    public EmployeeData getEmployeedata() {
        return this.Employeedata;
    }

    public void setEmployeedata(EmployeeData employeeData) {
        this.Employeedata = employeeData;
    }
}
