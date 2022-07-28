package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfirmPreBill {

    @SerializedName("OrderId")
    @Expose
    private int mOrderId;
    @SerializedName("Orderer")
    @Expose
    private String mSpecifications;
    @SerializedName("QuantityList")
    @Expose
    private List<ConfirmPreBillDetail> mConfirmPreBillDetail = null;

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        this.mOrderId = orderId;
    }

    public String getSpecifications() {
        return mSpecifications;
    }

    public void setSpecifications(String specifications) {
        this.mSpecifications = specifications;
    }

    public List<ConfirmPreBillDetail> getConfirmPreBill() {
        return mConfirmPreBillDetail;
    }

    public void setConfirmPreBills(List<ConfirmPreBillDetail> confirmPreBills) {
        this.mConfirmPreBillDetail = confirmPreBills;
    }
}

