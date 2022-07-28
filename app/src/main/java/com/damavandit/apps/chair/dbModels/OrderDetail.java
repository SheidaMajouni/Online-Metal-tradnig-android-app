package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail implements Parcelable {

    @SerializedName("DailyProductPriceListId")
    @Expose
    private int dailyProductPriceListId;
    @SerializedName("Quantity")
    @Expose
    private int quantity;
    @SerializedName("MeasurementUnitId")
    @Expose
    private int measurementUnitId;

    public OrderDetail() {
    }

    protected OrderDetail(Parcel in) {
        dailyProductPriceListId = in.readInt();
        quantity = in.readInt();
        measurementUnitId = in.readInt();
    }

    public static final Creator<OrderDetail> CREATOR = new Creator<OrderDetail>() {
        @Override
        public OrderDetail createFromParcel(Parcel in) {
            return new OrderDetail(in);
        }

        @Override
        public OrderDetail[] newArray(int size) {
            return new OrderDetail[size];
        }
    };

    public int getDailyProductPriceListId() {
        return dailyProductPriceListId;
    }

    public void setDailyProductPriceListId(int dailyProductPriceListId) {
        this.dailyProductPriceListId = dailyProductPriceListId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMeasurementUnitId() {
        return measurementUnitId;
    }

    public void setMeasurementUnitId(int measurementUnitId) {
        this.measurementUnitId = measurementUnitId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dailyProductPriceListId);
        dest.writeInt(quantity);
        dest.writeInt(measurementUnitId);
    }
}
