package com.damavandit.apps.chair.application;

import android.app.Application;
import android.content.Context;

import com.damavandit.apps.chair.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Font/Sans.ttf").setFontAttrId(R.attr.fontPath).build());
    }

    public static Context getContext() {
        return mContext;
    }
}
