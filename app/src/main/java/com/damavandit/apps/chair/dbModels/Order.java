package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order implements Parcelable {

    @SerializedName("OrderDate")
    @Expose
    private String date;
    @SerializedName("UserId")
    @Expose
    private String userId;
    @SerializedName("OrderStatusId")
    @Expose
    private int orderStatusId;
    @SerializedName("OrderDetails")
    @Expose
    private List<OrderDetail> orderDetails = null;

    public Order() {
    }

    protected Order(Parcel in) {
        userId = in.readString();
        orderStatusId = in.readInt();
        orderDetails = in.createTypedArrayList(OrderDetail.CREATOR);
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeInt(orderStatusId);
        dest.writeTypedList(orderDetails);
    }
}
