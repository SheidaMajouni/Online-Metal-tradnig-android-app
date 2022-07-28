package com.damavandit.apps.chair.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterBindingModel {

    @SerializedName("UserName")
    @Expose
    private String mUserName;
    @SerializedName("Password")
    @Expose
    private String mPassword;
    @SerializedName("ConfirmPassword")
    @Expose
    private String mConfirmPassword;
    @SerializedName("PasswordHint")
    @Expose
    private String mPasswordHint;

    public RegisterBindingModel() {
    }

    public RegisterBindingModel(String userName, String password, String confirmPassword,String hint) {
        this.mUserName = userName;
        this.mPassword = password;
        this.mConfirmPassword = confirmPassword;
        this.mPasswordHint = hint;
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

    public String getConfirmPassword() {
        return mConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.mConfirmPassword = confirmPassword;
    }

    public String getPasswordHint() {
        return mPasswordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.mPasswordHint = passwordHint;
    }
}
