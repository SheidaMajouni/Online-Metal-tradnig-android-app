package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeasurementUnit implements Parcelable {

    @SerializedName("MeasurementUnitId")
    @Expose
    private int measurementUnitId;
    @SerializedName("MeasurementUnitName")
    @Expose
    private String measurementUnitName;

    protected MeasurementUnit(Parcel in) {
        measurementUnitId = in.readInt();
        measurementUnitName = in.readString();
    }

    public MeasurementUnit(){

    }

    public static final Creator<MeasurementUnit> CREATOR = new Creator<MeasurementUnit>() {
        @Override
        public MeasurementUnit createFromParcel(Parcel in) {
            return new MeasurementUnit(in);
        }

        @Override
        public MeasurementUnit[] newArray(int size) {
            return new MeasurementUnit[size];
        }
    };

    public int getMeasurementUnitId() {
        return measurementUnitId;
    }

    public void setMeasurementUnitId(int measurementUnitId) {
        this.measurementUnitId = measurementUnitId;
    }

    public String getMeasurementUnitName() {
        return measurementUnitName;
    }

    public void setMeasurementUnitName(String measurementUnitName) {
        this.measurementUnitName = measurementUnitName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(measurementUnitId);
        dest.writeString(measurementUnitName);
    }
}

