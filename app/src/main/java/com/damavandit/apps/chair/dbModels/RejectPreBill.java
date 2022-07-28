package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RejectPreBill {

    @SerializedName("orderId")
    @Expose
    private int orderId;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
