package com.damavandit.apps.chair.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import java.util.Locale;

public class Setting {

    Context con;

    public Setting(Context context){
        this.con = context;
    }

    //----------------------------------------OpenActitity------------------------------------------
    public void OpenActivity (Class <? extends Activity> myActivity){
        Intent i= new Intent(con,myActivity);
        con.startActivity(i);
    }

    //------------------------------------------setLocale-------------------------------------------
    public void SetLocale (Locale locale){
        locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale=locale;
        con.getResources().updateConfiguration(config,con.getResources().getDisplayMetrics());
    }

    public void SetLocale (String str){
        SetLocale(new Locale(str));
    }
}