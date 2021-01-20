package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class VendorDetailResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private VendorDetailData vendorDetailData;

    public class VendorDetailData {
        @SerializedName("address")
        private String Address;
        @SerializedName("city")
        private String City;
        @SerializedName("company")
        private String Company;
        @SerializedName("country")
        private String Country;
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
        @SerializedName("state")
        private String State;

        public VendorDetailData() {
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

        public String getCountry() {
            return this.Country;
        }

        public void setCountry(String str) {
            this.Country = str;
        }

        public String getState() {
            return this.State;
        }

        public void setState(String str) {
            this.State = str;
        }

        public String getCity() {
            return this.City;
        }

        public void setCity(String str) {
            this.City = str;
        }

        public String getCompany() {
            return this.Company;
        }

        public void setCompany(String str) {
            this.Company = str;
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

    public VendorDetailData getVendorDetailData() {
        return this.vendorDetailData;
    }

    public void setVendorDetailData(VendorDetailData vendorDetailData2) {
        this.vendorDetailData = vendorDetailData2;
    }
}
