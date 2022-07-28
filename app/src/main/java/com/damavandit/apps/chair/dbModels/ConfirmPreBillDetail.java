package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConfirmPreBillDetail {

    @SerializedName("OrderDetailsId")
    @Expose
    private int mOrderDetailId;
    @SerializedName("Quantity")
    @Expose
    private double mQuantity;

    public int getOrderDetailId() {
        return mOrderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.mOrderDetailId = orderDetailId;
    }

    public double getQuantity() {
        return mQuantity;
    }

    public void setQuantity(double quantity) {
        this.mQuantity = quantity;
    }
}
