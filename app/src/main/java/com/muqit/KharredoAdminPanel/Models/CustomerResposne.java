package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class CustomerResposne {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private CustomerData customerData;

    public class CustomerData {
        @SerializedName("address")
        private String Address;
        @SerializedName("email")
        private String Email;
        @SerializedName("firstname")
        private String FirstName;
        @SerializedName("id")
        private String ID;
        @SerializedName("lastname")
        private String LastName;
        @SerializedName("contact_info")
        private String Phone;

        public CustomerData() {
        }

        public String getID() {
            return this.ID;
        }

        public void setID(String str) {
            this.ID = str;
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

        public String getAddress() {
            return this.Address;
        }

        public void setAddress(String str) {
            this.Address = str;
        }

        public String getPhone() {
            return this.Phone;
        }

        public void setPhone(String str) {
            this.Phone = str;
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

    public CustomerData getCustomerData() {
        return this.customerData;
    }

    public void setCustomerData(CustomerData customerData2) {
        this.customerData = customerData2;
    }
}
