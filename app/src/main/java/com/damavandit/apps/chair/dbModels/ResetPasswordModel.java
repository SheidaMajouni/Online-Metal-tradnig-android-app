package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResetPasswordModel {

    @SerializedName("UserName")
    @Expose
    private String mUserName;

    @SerializedName("Password")
    @Expose
    private String mPassword;

    @SerializedName("ConfirmPassword")
    @Expose
    private String mConfirm;

    @SerializedName("Code")
    @Expose
    private String mCode;

    public ResetPasswordModel(String username,String password, String confirm,String code) {
        this.mUserName = username;
        this.mPassword = password;
        this.mConfirm = confirm;
        this.mCode = code;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getConfirm() {
        return mConfirm;
    }

    public void setConfirm(String confirm) {
        this.mConfirm = confirm;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }
}
