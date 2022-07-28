package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductGroupManufacturerModel2 implements Parcelable {

    @SerializedName("ProductGroupId")
    @Expose
    private int productGroupId;
    @SerializedName("ProductGroupName")
    @Expose
    private String productGroupName;
    @SerializedName("ProductGroupImage")
    @Expose
    private String productGroupImage;
    @SerializedName("Manufacturers")
    @Expose
    private ArrayList<Manufacturer> manufacturers = null;

    protected ProductGroupManufacturerModel2(Parcel in) {
        productGroupId = in.readInt();
        productGroupName = in.readString();
        productGroupImage = in.readString();
        manufacturers = in.createTypedArrayList(Manufacturer.CREATOR);
    }

    public static final Creator<ProductGroupManufacturerModel2> CREATOR = new Creator<ProductGroupManufacturerModel2>() {
        @Override
        public ProductGroupManufacturerModel2 createFromParcel(Parcel in) {
            return new ProductGroupManufacturerModel2(in);
        }

        @Override
        public ProductGroupManufacturerModel2[] newArray(int size) {
            return new ProductGroupManufacturerModel2[size];
        }
    };

    public int getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(int productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getProductGroupName() {
        return productGroupName;
    }

    public void setProductGroupName(String productGroupName) {
        this.productGroupName = productGroupName;
    }

    public String getProductGroupImage() {
        return productGroupImage;
    }

    public void setProductGroupImage(String productGroupImage) {
        this.productGroupImage = productGroupImage;
    }

    public ArrayList<Manufacturer> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(ArrayList<Manufacturer> manufacturers) {
        this.manufacturers = manufacturers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(productGroupId);
        dest.writeString(productGroupName);
        dest.writeString(productGroupImage);
        dest.writeTypedList(manufacturers);
    }
}
