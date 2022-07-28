package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyProductSubGroup implements Parcelable {

    @SerializedName("DailyProductPriceListId")
    @Expose
    private int mDailyProductPriceListId;
    @SerializedName("DailyProductPriceListDate")
    @Expose
    private String mDailyProductPriceListDate;
    @SerializedName("ManufacturerProductSubgroupId")
    @Expose
    private int mManufacturerProductSubgroupId;
    @SerializedName("ProductSubgroupId")
    @Expose
    private int mProductSubGroupId;
    @SerializedName("ProductSubgroupName")
    @Expose
    private String mProductSubGroupName;
    @SerializedName("ProductGroupImage")
    @Expose
    private String mProductGroupImage;
    @SerializedName("ManufacturerName")
    @Expose
    private String mManufacturerName;
    @SerializedName("DeliveryPointId")
    @Expose
    private int mDeliveryPointId;
    @SerializedName("DeliveryPointName")
    @Expose
    private String mDeliveryPointName;
    @SerializedName("Price")
    @Expose
    private long mPrice;
    @SerializedName("Length")
    @Expose
    private double mLenght;
    @SerializedName("Width")
    @Expose
    private double mWidth;
    @SerializedName("Height")
    @Expose
    private double mHeight;
    @SerializedName("Thickness")
    @Expose
    private double mThickness;
    @SerializedName("Size")
    @Expose
    private int mSize;
    @SerializedName("Alloy")
    @Expose
    private String mAlloy;
    @SerializedName("DefaultDimensionName")
    @Expose
    private String mDefaultDimensionName;
    @SerializedName("DefaultDimensionValue")
    @Expose
    private double mDefaultDimensionValue;

    protected DailyProductSubGroup(Parcel in) {
        mDailyProductPriceListId = in.readInt();
        mDailyProductPriceListDate = in.readString();
        mManufacturerProductSubgroupId = in.readInt();
        mProductSubGroupId = in.readInt();
        mProductSubGroupName = in.readString();
        mProductGroupImage = in.readString();
        mManufacturerName = in.readString();
        mDeliveryPointId = in.readInt();
        mDeliveryPointName = in.readString();
        mPrice = in.readLong();
        mLenght = in.readDouble();
        mWidth = in.readDouble();
        mHeight = in.readDouble();
        mThickness = in.readDouble();
        mSize = in.readInt();
        mAlloy = in.readString();
        mDefaultDimensionName = in.readString();
        mDefaultDimensionValue = in.readDouble();
    }

    public static final Creator<DailyProductSubGroup> CREATOR = new Creator<DailyProductSubGroup>() {
        @Override
        public DailyProductSubGroup createFromParcel(Parcel in) {
            return new DailyProductSubGroup(in);
        }

        @Override
        public DailyProductSubGroup[] newArray(int size) {
            return new DailyProductSubGroup[size];
        }
    };

    public int getDailyProductPriceListId() {
        return mDailyProductPriceListId;
    }

    public void setDailyProductPriceListId(int dailyProductPriceListId) {
        this.mDailyProductPriceListId = dailyProductPriceListId;
    }

    public String getDailyProductPriceListDate() {
        return mDailyProductPriceListDate;
    }

    public void setDailyProductPriceListDate(String dailyProductPriceListDate) {
        this.mDailyProductPriceListDate = dailyProductPriceListDate;
    }


    public int getProductSubGroupId() {
        return mProductSubGroupId;
    }

    public void setProductSubGroupId(int productSubGroupId) {
        this.mProductSubGroupId = productSubGroupId;
    }

    public String getProductSubGroupName() {
        return mProductSubGroupName;
    }

    public void setProductSubGroupName(String productSubGroupName) {
        this.mProductSubGroupName = productSubGroupName;
    }

    public String getProductGroupImage() {
        return mProductGroupImage;
    }

    public void setProductGroupImage(String productGroupImage) {
        this.mProductGroupImage = productGroupImage;
    }

    public int getManufacturerProductSubgroupId() {
        return mManufacturerProductSubgroupId;
    }

    public void setManufacturerProductSubgroupId(int manufacturerProductSubgroupId) {
        this.mManufacturerProductSubgroupId = manufacturerProductSubgroupId;
    }

    public String getManufacturerName() {
        return mManufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.mManufacturerName = manufacturerName;
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

    public void setDeliveryPointName(String deliveryPointName) {
        this.mDeliveryPointName = deliveryPointName;
    }

    public long getPrice() {
        return mPrice;
    }

    public void setPrice(long price) {
        this.mPrice = price;
    }

    public double getLenght() {
        return mLenght;
    }

    public void setLenght(double lenght) {
        this.mLenght = lenght;
    }

    public double getWidth() {
        return mWidth;
    }

    public void setWidth(double width) {
        this.mWidth = width;
    }

    public double getHeight() {
        return mHeight;
    }

    public void setHeight(double height) {
        this.mHeight = height;
    }

    public double getThickness() {
        return mThickness;
    }

    public void setThickness(int thickness) {
        this.mThickness = thickness;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        this.mSize = size;
    }

    public String getAlloy() {
        return mAlloy;
    }

    public void setAlloy(String alloy) {
        this.mAlloy = alloy;
    }

    public String getDefaultSizeName() {
        return mDefaultDimensionName;
    }

    public void setDefaultSizeName(String defaultSizeName) {
        this.mDefaultDimensionName = defaultSizeName;
    }

    public double getDefaultDimensionValue() {
        return mDefaultDimensionValue;
    }

    public void setDefaultSizeValue(double defaultSizeValue) {
        this.mDefaultDimensionValue = defaultSizeValue;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mDailyProductPriceListId);
        dest.writeString(mDailyProductPriceListDate);
        dest.writeInt(mManufacturerProductSubgroupId);
        dest.writeInt(mProductSubGroupId);
        dest.writeString(mProductSubGroupName);
        dest.writeString(mProductGroupImage);
        dest.writeString(mManufacturerName);
        dest.writeInt(mDeliveryPointId);
        dest.writeString(mDeliveryPointName);
        dest.writeLong(mPrice);
        dest.writeDouble(mLenght);
        dest.writeDouble(mWidth);
        dest.writeDouble(mHeight);
        dest.writeDouble(mThickness);
        dest.writeInt(mSize);
        dest.writeString(mAlloy);
        dest.writeString(mDefaultDimensionName);
        dest.writeDouble(mDefaultDimensionValue);
    }
}
