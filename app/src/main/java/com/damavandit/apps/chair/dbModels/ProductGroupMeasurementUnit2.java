package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductGroupMeasurementUnit2 implements Parcelable {

    @SerializedName("ProductGroupId")
    @Expose
    private int productGroupId;
    @SerializedName("MeasurementUnits")
    @Expose
    private ArrayList <MeasurementUnitId> measurementUnitId = null;

    public ProductGroupMeasurementUnit2(){

    }


    protected ProductGroupMeasurementUnit2(Parcel in) {
        productGroupId = in.readInt();
        measurementUnitId = in.createTypedArrayList(MeasurementUnitId.CREATOR);
    }

    public static final Creator<ProductGroupMeasurementUnit2> CREATOR = new Creator<ProductGroupMeasurementUnit2>() {
        @Override
        public ProductGroupMeasurementUnit2 createFromParcel(Parcel in) {
            return new ProductGroupMeasurementUnit2(in);
        }

        @Override
        public ProductGroupMeasurementUnit2[] newArray(int size) {
            return new ProductGroupMeasurementUnit2[size];
        }
    };

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public ArrayList<MeasurementUnitId> getMeasurementUnitId() {
        return measurementUnitId;
    }

    public void setMeasurementUnitId(ArrayList<MeasurementUnitId> measurementUnitId) {
        this.measurementUnitId = measurementUnitId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productGroupId);
        dest.writeTypedList(measurementUnitId);
    }
}

