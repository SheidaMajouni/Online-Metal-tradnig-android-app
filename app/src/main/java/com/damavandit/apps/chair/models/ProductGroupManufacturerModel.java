package com.damavandit.apps.chair.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductGroupManufacturerModel implements Parcelable {

    @SerializedName("ProductGroupId")
    @Expose
    private int mProductGroupId;
    @SerializedName("ProductGroupName")
    @Expose
    private String mProductGroupName;
    @SerializedName("ProductGroupImage")
    @Expose
    private String mProductGroupImage;
    @SerializedName("ManufacturerId")
    @Expose
    private int mManufacturerId;
    @SerializedName("ManufacturerName")
    @Expose
    private String mManufacturerName;
    @SerializedName("Logo")
    @Expose
    private String mLogo;

    public ProductGroupManufacturerModel(){
    }

    public ProductGroupManufacturerModel
            (int mProductGroupId,
             String mProductGroupName,
             String mProductGroupImage,
             int mManufacturerId,
             String mManufacturerName,
             String mLogo) {
        this.mProductGroupId = mProductGroupId;
        this.mProductGroupName = mProductGroupName;
        this.mProductGroupImage = mProductGroupImage;
        this.mManufacturerId = mManufacturerId;
        this.mManufacturerName = mManufacturerName;
        this.mLogo = mLogo;
    }

    protected ProductGroupManufacturerModel(Parcel in) {
        mProductGroupId = in.readInt();
        mProductGroupName = in.readString();
        mProductGroupImage = in.readString();
        mManufacturerId = in.readInt();
        mManufacturerName = in.readString();
        mLogo = in.readString();
    }

    public static final Creator<ProductGroupManufacturerModel> CREATOR = new Creator<ProductGroupManufacturerModel>() {
        @Override
        public ProductGroupManufacturerModel createFromParcel(Parcel in) {
            return new ProductGroupManufacturerModel(in);
        }

        @Override
        public ProductGroupManufacturerModel[] newArray(int size) {
            return new ProductGroupManufacturerModel[size];
        }
    };

    public int getProductGroupId() {
        return mProductGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.mProductGroupId = productGroupId;
    }

    public String getProductGroupName() {
        return mProductGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.mProductGroupName = productGroupName;
    }

    public String getProductGroupImage() {
        return mProductGroupImage;
    }

    public void setProductGroupImage(String productGroupImage) {
        this.mProductGroupImage = productGroupImage;
    }

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

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String logo) {
        this.mLogo = logo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mProductGroupId);
        dest.writeString(mProductGroupName);
        dest.writeString(mProductGroupImage);
        dest.writeInt(mManufacturerId);
        dest.writeString(mManufacturerName);
        dest.writeString(mLogo);
    }
}
