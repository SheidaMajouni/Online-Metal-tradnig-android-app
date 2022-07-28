package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordModel {

    @SerializedName("UserName")
    @Expose
    private String mUserName;

    public ForgotPasswordModel(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }
}
