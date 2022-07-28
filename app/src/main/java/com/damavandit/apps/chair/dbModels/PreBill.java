package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PreBill implements Parcelable {

    @SerializedName("Tax")
    @Expose
    private double mTax;
    @SerializedName("OrderDetails")
    @Expose
    private ArrayList<PreBillDetail> mPreBillDetails = null;

    protected PreBill(Parcel in) {
        mTax = in.readDouble();
        mPreBillDetails = in.createTypedArrayList(PreBillDetail.CREATOR);
    }

    public static final Creator<PreBill> CREATOR = new Creator<PreBill>() {
        @Override
        public PreBill createFromParcel(Parcel in) {
            return new PreBill(in);
        }

        @Override
        public PreBill[] newArray(int size) {
            return new PreBill[size];
        }
    };

    public ArrayList<PreBillDetail> getPreBillDetails() {
        return mPreBillDetails;
    }

    public void setPreBillDetails(ArrayList<PreBillDetail> preBillDetails) {
        this.mPreBillDetails = preBillDetails;
    }

    public double getTax() {
        return mTax;
    }

    public void setTax(double tax) {
        this.mTax = tax;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(mTax);
        dest.writeTypedList(mPreBillDetails);
    }
}
