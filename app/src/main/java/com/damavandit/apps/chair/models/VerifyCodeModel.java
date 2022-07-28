package com.damavandit.apps.chair.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyCodeModel {

    @SerializedName("UserName")
    @Expose
    private String mPhone;
    @SerializedName("Code")
    @Expose
    private String mVerificationCode;

    public VerifyCodeModel(){
    }

    public VerifyCodeModel(String phone, String verificationCode) {
        this.mPhone = phone;
        this.mVerificationCode = verificationCode;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    public String getVerificationCode() {
        return mVerificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.mVerificationCode = verificationCode;
    }
}
