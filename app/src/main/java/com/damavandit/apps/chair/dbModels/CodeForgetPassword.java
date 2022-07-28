package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CodeForgetPassword {

    @SerializedName("PasswordHint")
    @Expose
    private String mCode;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        this.mCode = code;
    }
}
