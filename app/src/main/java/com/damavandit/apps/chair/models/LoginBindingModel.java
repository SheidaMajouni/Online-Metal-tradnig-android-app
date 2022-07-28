package com.damavandit.apps.chair.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBindingModel {

    @SerializedName("UserName")
    @Expose
    private String mUserName;
    @SerializedName("Password")
    @Expose
    private String mPassword;

    public LoginBindingModel(){
    }

    public LoginBindingModel(String mUserName, String mPassword) {
        this.mUserName = mUserName;
        this.mPassword = mPassword;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
