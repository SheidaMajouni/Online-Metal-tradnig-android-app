package com.damavandit.apps.chair.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("UserName")
    @Expose
    private String mUserName;
    @SerializedName("HasRegistered")
    @Expose
    private boolean mHasRegistered;
    @SerializedName("LoginProvider")
    @Expose
    private String mLoginProvider;

    public UserInfo() {
    }

    public UserInfo(String userName, boolean hasRegistered, String loginProvider) {
        this.mUserName = userName;
        this.mHasRegistered = hasRegistered;
        this.mLoginProvider = loginProvider;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public boolean getHasRegistered() {
        return mHasRegistered;
    }

    public void setHasRegistered(boolean hasRegistered) {
        this.mHasRegistered = hasRegistered;
    }

    public String getLoginProvider() {
        return mLoginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.mLoginProvider = loginProvider;
    }

}
