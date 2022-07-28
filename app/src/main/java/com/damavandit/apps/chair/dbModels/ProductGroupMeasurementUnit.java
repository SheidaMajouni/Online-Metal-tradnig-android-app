package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGroupMeasurementUnit implements Parcelable {

    @SerializedName("ProductGroupId")
    @Expose
    private int productGroupId;
    @SerializedName("MeasurementGroupId")
    @Expose
    private int measurementGroupId;

    protected ProductGroupMeasurementUnit(Parcel in) {
        productGroupId = in.readInt();
        measurementGroupId = in.readInt();
    }

    public ProductGroupMeasurementUnit(){

    }

    public static final Creator<ProductGroupMeasurementUnit> CREATOR = new Creator<ProductGroupMeasurementUnit>() {
        @Override
        public ProductGroupMeasurementUnit createFromParcel(Parcel in) {
            return new ProductGroupMeasurementUnit(in);
        }

        @Override
        public ProductGroupMeasurementUnit[] newArray(int size) {
            return new ProductGroupMeasurementUnit[size];
        }
    };

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public int getMeasurementGroupId() {
        return measurementGroupId;
    }

    public void setMeasurementGroupId(int measurementGroupId) {
        this.measurementGroupId = measurementGroupId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productGroupId);
        dest.writeInt(measurementGroupId);
    }
}
