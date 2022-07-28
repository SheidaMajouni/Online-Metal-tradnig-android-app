package com.damavandit.apps.chair.font;

import android.app.Application;

import com.damavandit.apps.chair.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MyAppFont extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("Font/Sans.ttf").setFontAttrId(R.attr.fontPath).build());
    }
}