package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreBillDetail implements Parcelable {

    @SerializedName("OrderDetailsId")
    @Expose
    private int mOrderDetailsId;
    @SerializedName("ProductGroupName")
    @Expose
    private String mProductName;
    @SerializedName("ProductSubgroupName")
    @Expose
    private String mProductSubGroupName;
    @SerializedName("Length")
    @Expose
    private double length;

    @SerializedName("Width")
    @Expose
    private double width;

    @SerializedName("Height")
    @Expose
    private double height;

    @SerializedName("Thickness")
    @Expose
    private double thickness;

    @SerializedName("Size")
    @Expose
    private double size;

    @SerializedName("Alloy")
    @Expose
    private String alloy;
    @SerializedName("DefaultDimensionValue")
    @Expose
    private int mDefaultDimensionValue;
    @SerializedName("DefaultDimensionName")
    @Expose
    private String mDefaultDimensionName;
    @SerializedName("Quantity")
    @Expose
    private int mQuantity;
    @SerializedName("Price")
    @Expose
    private int mPrice;
    @SerializedName("MeasurementUnitId")
    @Expose
    private int mMeasurementUnitId;
    @SerializedName("MeasurementUnitName")
    @Expose
    private String mMeasurementUnitName;
    @SerializedName("DeliveryPointId")
    @Expose
    private int mDeliveryPointId;

    @SerializedName("DeliveryPointName")
    @Expose
    private String mDeliveryPointName;

    public PreBillDetail(){

    }

    protected PreBillDetail(Parcel in) {
        mOrderDetailsId = in.readInt();
        mProductName = in.readString();
        mProductSubGroupName = in.readString();
        mDefaultDimensionValue = in.readInt();
        mDefaultDimensionName = in.readString();
        mQuantity = in.readInt();
        mPrice = in.readInt();
        mMeasurementUnitId = in.readInt();
        mMeasurementUnitName = in.readString();
        mDeliveryPointId = in.readInt();
        mDeliveryPointName = in.readString();
        length = in.readDouble();
        width = in.readDouble();
        height = in.readDouble();
        thickness = in.readDouble();
        size = in.readInt();
        alloy = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mOrderDetailsId);
        dest.writeString(mProductName);
        dest.writeString(mProductSubGroupName);
        dest.writeInt(mDefaultDimensionValue);
        dest.writeString(mDefaultDimensionName);
        dest.writeInt(mQuantity);
        dest.writeInt(mPrice);
        dest.writeInt(mMeasurementUnitId);
        dest.writeString(mMeasurementUnitName);
        dest.writeInt(mDeliveryPointId);
        dest.writeString(mDeliveryPointName);
        dest.writeDouble(length);
        dest.writeDouble(width);
        dest.writeDouble(height);
        dest.writeDouble(thickness);
        dest.writeDouble(size);
        dest.writeString(alloy);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PreBillDetail> CREATOR = new Creator<PreBillDetail>() {
        @Override
        public PreBillDetail createFromParcel(Parcel in) {
            return new PreBillDetail(in);
        }

        @Override
        public PreBillDetail[] newArray(int size) {
            return new PreBillDetail[size];
        }
    };

    public int getOrderDetailsId() {
        return mOrderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.mOrderDetailsId = orderDetailsId;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        this.mProductName = productName;
    }

    public String getProductSubGroupName() {
        return mProductSubGroupName;
    }

    public void setProductSubGroupName(String productSubGroupName) {
        this.mProductSubGroupName = productSubGroupName;
    }

    public int getDefaultSizeValue() {
        return mDefaultDimensionValue;
    }

    public void setDefaultSizeValue(int defaultSizeName) {
        this.mDefaultDimensionValue = defaultSizeName;
    }

    public String getmDefaultDimensionName() {
        return mDefaultDimensionName;
    }

    public void setmDefaultDimensionName(String mDefaultDimensionName) {
        this.mDefaultDimensionName = mDefaultDimensionName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        this.mPrice = price;
    }

    public int getMeasurementUnitId() {
        return mMeasurementUnitId;
    }

    public void setMeasurementUnitId(int measurementUnitId) {
        this.mMeasurementUnitId = measurementUnitId;
    }

    public String getmMeasurementUnitName() {
        return mMeasurementUnitName;
    }

    public void setmMeasurementUnitName(String mMeasurementUnitName) {
        this.mMeasurementUnitName = mMeasurementUnitName;
    }

    public int getDeliveryPointId() {
        return mDeliveryPointId;
    }

    public void setDeliveryPointId(int deliveryPointId) {
        this.mDeliveryPointId = deliveryPointId;
    }

    public String getDeliveryPointName() {
        return mDeliveryPointName;
    }

    public void setDeliveryPointName(String mDeliveryPointName) {
        this.mDeliveryPointName = mDeliveryPointName;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getAlloy() {
        return alloy;
    }

    public void setAlloy(String alloy) {
        this.alloy = alloy;
    }
}
