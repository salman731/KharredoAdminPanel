package com.muqit.KharredoAdminPanel.Models;

import com.google.gson.annotations.SerializedName;

public class OrderItemResponse {
    @SerializedName("message")
    private String Message;
    @SerializedName("status")
    private String Status;
    @SerializedName("data")
    private OrderItemData orderItemData;

    public class OrderItemData {
        @SerializedName("id")
        private String ID;
        @SerializedName("name")
        private String Name;
        @SerializedName("photo")
        private String Photo;
        @SerializedName("price")
        private String Price;
        @SerializedName("quantity")
        private String Quantity;

        public OrderItemData() {
        }

        public String getID() {
            return this.ID;
        }

        public void setID(String str) {
            this.ID = str;
        }

        public String getQuantity() {
            return this.Quantity;
        }

        public void setQuantity(String str) {
            this.Quantity = str;
        }

        public String getPrice() {
            return this.Price;
        }

        public void setPrice(String str) {
            this.Price = str;
        }

        public String getPhoto() {
            return this.Photo;
        }

        public void setPhoto(String str) {
            this.Photo = str;
        }

        public String getName() {
            return this.Name;
        }

        public void setName(String str) {
            this.Name = str;
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

    public OrderItemData getOrderItemData() {
        return this.orderItemData;
    }

    public void setOrderItemData(OrderItemData orderItemData2) {
        this.orderItemData = orderItemData2;
    }
}
