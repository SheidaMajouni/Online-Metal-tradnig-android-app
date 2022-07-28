package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryPoint implements Parcelable {


    @SerializedName("DeliveryPointId")
    @Expose
    private int deliveryPointId;
    @SerializedName("DeliveryPointName")
    @Expose
    private String deliveryPointName;

    protected DeliveryPoint(Parcel in) {
        deliveryPointId = in.readInt();
        deliveryPointName = in.readString();
    }

    public DeliveryPoint(){

    }

    public static final Creator<DeliveryPoint> CREATOR = new Creator<DeliveryPoint>() {
        @Override
        public DeliveryPoint createFromParcel(Parcel in) {
            return new DeliveryPoint(in);
        }

        @Override
        public DeliveryPoint[] newArray(int size) {
            return new DeliveryPoint[size];
        }
    };

    public int getDeliveryPointId() {
        return deliveryPointId;
    }

    public void setDeliveryPointId(int deliveryPointId) {
        this.deliveryPointId = deliveryPointId;
    }

    public String getDeliveryPointName() {
        return deliveryPointName;
    }

    public void setDeliveryPointName(String deliveryPointName) {
        this.deliveryPointName = deliveryPointName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(deliveryPointId);
        dest.writeString(deliveryPointName);
    }
}
