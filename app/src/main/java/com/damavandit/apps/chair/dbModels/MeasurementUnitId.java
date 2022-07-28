package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeasurementUnitId implements Parcelable {

    @SerializedName("MeasurementUnitId")
    @Expose
    private int measurementUnitId;

    protected MeasurementUnitId(Parcel in) {
        measurementUnitId = in.readInt();
    }

    public static final Creator<MeasurementUnitId> CREATOR = new Creator<MeasurementUnitId>() {
        @Override
        public MeasurementUnitId createFromParcel(Parcel in) {
            return new MeasurementUnitId(in);
        }

        @Override
        public MeasurementUnitId[] newArray(int size) {
            return new MeasurementUnitId[size];
        }
    };

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
        dest.writeInt(measurementUnitId);
    }
}
