package com.damavandit.apps.chair.other;

import android.content.Context;
import android.content.SharedPreferences;

import com.damavandit.apps.chair.dbModels.ShoppingCartModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Session {
    SharedPreferences mPrefs;
    SharedPreferences.Editor editor;
    Context mContext;

    public Session(Context ctx) {
        this.mContext = ctx;
        mPrefs = ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor = mPrefs.edit();
    }

    public void setLoggedin(boolean logggedin) {
        editor.putBoolean("loggedInmode", logggedin);
        editor.commit();
    }

    public boolean loggedin() {
        return mPrefs.getBoolean("loggedInmode", false);
    }

    public void addInBasket(ShoppingCartModel model) {

        ArrayList<ShoppingCartModel> models;

        if (getModelList() == null) {
            models = new ArrayList<>();
            models.add(model);
        } else {
            models = getModelList();
            models.add(model);
        }

        Gson gson = new Gson();
        String json = gson.toJson(models);
        editor.putString("user", json);
        editor.commit();
    }

    public ArrayList<ShoppingCartModel> delete(int id) {
        ArrayList<ShoppingCartModel> models = getModelList();

        for (int i = 0; i < models.size(); i++) {
            if (i == id) {
                models.remove(i);
                Gson gson = new Gson();
                String json = gson.toJson(models);
                editor.putString("user", json);
                editor.commit();
            }
        }
        return models;
    }

    public ArrayList<ShoppingCartModel> clearShoppingCart() {
        ArrayList<ShoppingCartModel> models = getModelList();

        int size = models.size();
        for (int i = 0; i < size; i++) {
            models.remove(0);
            Gson gson = new Gson();
            String json = gson.toJson(models);
            editor.putString("user", json);
            editor.commit();
        }
        setNotificationCount(0);
        return models;
    }

    public ArrayList<ShoppingCartModel> getModelList() {
        Gson gson = new Gson();
        String json = mPrefs.getString("user", "");

        Type type = new TypeToken<ArrayList<ShoppingCartModel>>() {
        }.getType();
        ArrayList<ShoppingCartModel> model = new Gson().fromJson(json, type);

        return model;
    }

    public void setNotificationCount(int i) {
        editor.putInt("notifCount", i);
        editor.commit();
    }

    public int getNotifCount() {
        return mPrefs.getInt("notifCount", 0);
    }

    public void deleteAllSharedPreferences() {
        editor.clear();
    }

    public void setUserId(String userId) {
        editor.putString("userId", userId);
        editor.commit();
    }

    public String getUserId() {
        return mPrefs.getString("userId", "");
    }

    public void setUserName(String userName) {
        editor.putString("userName", userName);
        editor.commit();
    }

    public String getUserName() {
        return mPrefs.getString("userName", "");
    }

    public void setSecondCode(String code) {
        editor.putString("code", code);
        editor.commit();
    }

    public String getSecondCode() {
        return mPrefs.getString("code", "");
    }

    public void setUserInfoComplete(boolean b){
        editor.putBoolean("userInfo",b);
        editor.commit();
    }

    public boolean getUserInfoComplete(){
        return mPrefs.getBoolean("userInfo",false);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("IS_FIRST_TIME_LAUNCH", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return mPrefs.getBoolean("IS_FIRST_TIME_LAUNCH", true);
    }

}
