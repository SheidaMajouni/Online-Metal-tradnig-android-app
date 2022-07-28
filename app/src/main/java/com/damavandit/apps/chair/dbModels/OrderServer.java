package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderServer implements Parcelable {

    @SerializedName("OrderId")
    @Expose
    private int orderId;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    //    private String orderer;
    @SerializedName("OrderStatusId")
    @Expose
    private int orderStatusId;
    @SerializedName("RevisionOrderID")
    @Expose
    private int revisionOrderId;

    @SerializedName("TrackingCode")
    @Expose
    private String mTrackingCode;

    protected OrderServer(Parcel in) {
        orderId = in.readInt();
        orderDate = in.readString();
        orderStatusId = in.readInt();
        revisionOrderId = in.readInt();
        mTrackingCode = in.readString();
    }

    public static final Creator<OrderServer> CREATOR = new Creator<OrderServer>() {
        @Override
        public OrderServer createFromParcel(Parcel in) {
            return new OrderServer(in);
        }

        @Override
        public OrderServer[] newArray(int size) {
            return new OrderServer[size];
        }
    };

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

//    public String getOrderer() {
//        return orderer;
//    }
//
//    public void setOrderer(String orderer) {
//        this.orderer = orderer;
//    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public int getRevisionOrderId() {
        return revisionOrderId;
    }

    public void setRevisionOrderId(int revisionOrderId) {
        this.revisionOrderId = revisionOrderId;
    }

    public String getmTrackingCode() {
        return mTrackingCode;
    }

    public void setmTrackingCode(String mTrackingCode) {
        this.mTrackingCode = mTrackingCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeString(orderDate);
        dest.writeInt(orderStatusId);
        dest.writeInt(revisionOrderId);
        dest.writeString(mTrackingCode);
    }
}
