package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinalBill implements Parcelable {

    @SerializedName("OrderId")
    @Expose
    private int mOrderId;
    @SerializedName("OrderStatusId")
    @Expose
    private int mOrderStatusId;
    @SerializedName("OrderDate")
    @Expose
    private String mOrderDate;
    @SerializedName("TrackingCode")
    @Expose
    private String mTrackingCode;
    @SerializedName("Orderer")
    @Expose
    private String mOrderer;
    @SerializedName("VendorName")
    @Expose
    private String mVendorName;
    @SerializedName("InvoiceAmount")
    @Expose
    private int mInvoiceAmount;
    @SerializedName("FaultTolerance")
    @Expose
    private String mFaultTolerance;
    @SerializedName("PayableAmount")
    @Expose
    private int mPayableAmount;

    public FinalBill(){

    }

    protected FinalBill(Parcel in) {
        mOrderId = in.readInt();
        mOrderStatusId = in.readInt();
        mOrderDate = in.readString();
        mTrackingCode = in.readString();
        mOrderer = in.readString();
        mVendorName = in.readString();
        mInvoiceAmount = in.readInt();
        mFaultTolerance = in.readString();
        mPayableAmount = in.readInt();
    }

    public static final Creator<FinalBill> CREATOR = new Creator<FinalBill>() {
        @Override
        public FinalBill createFromParcel(Parcel in) {
            return new FinalBill(in);
        }

        @Override
        public FinalBill[] newArray(int size) {
            return new FinalBill[size];
        }
    };

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int orderId) {
        this.mOrderId = orderId;
    }

    public int getOrderStatusId() {
        return mOrderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.mOrderStatusId = orderStatusId;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.mOrderDate = orderDate;
    }

    public String getTrackingCode() {
        return mTrackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.mTrackingCode = trackingCode;
    }

    public String getOrderer() {
        return mOrderer;
    }

    public void setOrderer(String orderer) {
        this.mOrderer = orderer;
    }

    public String getVendorName() {
        return mVendorName;
    }

    public void setVendorName(String vendorName) {
        this.mVendorName = vendorName;
    }

    public int getInvoiceAmount() {
        return mInvoiceAmount;
    }

    public void setInvoiceAmount(int invoiceAmount) {
        this.mInvoiceAmount = invoiceAmount;
    }

    public String getFaultTolerance() {
        return mFaultTolerance;
    }

    public void setFaultTolerance(String faultTolerance) {
        this.mFaultTolerance = faultTolerance;
    }

    public int getPayableAmount() {
        return mPayableAmount;
    }

    public void setPayableAmount(int payableAmount) {
        this.mPayableAmount = payableAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mOrderId);
        dest.writeInt(mOrderStatusId);
        dest.writeString(mOrderDate);
        dest.writeString(mTrackingCode);
        dest.writeString(mOrderer);
        dest.writeString(mVendorName);
        dest.writeInt(mInvoiceAmount);
        dest.writeString(mFaultTolerance);
        dest.writeInt(mPayableAmount);
    }
}
