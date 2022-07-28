package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manufacturer implements Parcelable {

    @SerializedName("ManufacturerId")
    @Expose
    private int mManufacturerId;
    @SerializedName("ManufacturerName")
    @Expose
    private String mManufacturerName;
    @SerializedName("ManufacturerLogo")
    @Expose
    private String mManufacturerLogo;

    protected Manufacturer(Parcel in) {
        mManufacturerId = in.readInt();
        mManufacturerName = in.readString();
        mManufacturerLogo = in.readString();
    }

    public static final Creator<Manufacturer> CREATOR = new Creator<Manufacturer>() {
        @Override
        public Manufacturer createFromParcel(Parcel in) {
            return new Manufacturer(in);
        }

        @Override
        public Manufacturer[] newArray(int size) {
            return new Manufacturer[size];
        }
    };

    public int getManufacturerId() {
        return mManufacturerId;
    }

    public void setManufacturerId(int manufacturerId) {
        this.mManufacturerId = manufacturerId;
    }

    public String getManufacturerName() {
        return mManufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.mManufacturerName = manufacturerName;
    }

    public String getManufacturerLogo() {
        return mManufacturerLogo;
    }

    public void setManufacturerLogo(String manufacturerLogo) {
        this.mManufacturerLogo = manufacturerLogo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mManufacturerId);
        dest.writeString(mManufacturerName);
        dest.writeString(mManufacturerLogo);
    }
}
