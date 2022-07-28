package com.damavandit.apps.chair.dbModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoState {

    @SerializedName("UserInfoComplete")
    @Expose
    private boolean userInfoComplete;

    public boolean isUserInfoComplete() {
        return userInfoComplete;
    }

    public void setUserInfoComplete(boolean userInfoComplete) {
        this.userInfoComplete = userInfoComplete;
    }
}
