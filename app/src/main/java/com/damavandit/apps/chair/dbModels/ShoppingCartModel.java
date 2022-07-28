package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShoppingCartModel {

    @SerializedName("BasketId")
    @Expose
    private int basketId;
    @SerializedName("explain")
    @Expose
    private String explain;
    @SerializedName("price")
    @Expose
    private int price;
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unitId")
    @Expose
    private int unitId;
    @SerializedName("deliveryPoint")
    @Expose
    private String deliveryPoint;

    @SerializedName("factoryName")
    @Expose
    private String factoryName;

    @SerializedName("DailyProductPriceListId")
    @Expose
    private int dailyProductPriceListId;

    @SerializedName("ManufacturerLogo")
    @Expose
    private String mManufacturerLogo;

    public int getBasketId() {
        return basketId;
    }

    public void setBasketId(int basketId) {
        this.basketId = basketId;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getUnitId() {
        return unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public String getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    public int getDailyProductPriceListId() {
        return dailyProductPriceListId;
    }

    public void setDailyProductPriceListId(int dailyProductPriceListId) {
        this.dailyProductPriceListId = dailyProductPriceListId;
    }

    public String getManufacturerLogo() {
        return mManufacturerLogo;
    }

    public void setManufacturerLogo(String mManufacturerLogo) {
        this.mManufacturerLogo = mManufacturerLogo;
    }
}
