package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserFullInfo {

    @SerializedName("FirstName")
    @Expose
    private String name;
    @SerializedName("LastName")
    @Expose
    private String family;
    @SerializedName("NationalId")
    @Expose
    private String email;
    @SerializedName("Address")
    @Expose
    private String address;

    public UserFullInfo(String date, String userId, String orderStatusId,String address) {
        this.name = date;
        this.family = userId;
        this.email = orderStatusId;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
