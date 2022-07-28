package com.damavandit.apps.chair.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.damavandit.apps.chair.R;

public class LockedOutActivity extends AppCompatActivity {

    private boolean allowBack = false;
    public static AppCompatActivity mActivityLockedOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locked_out);

        mActivityLockedOut = this;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SmsVerificationActivity.mActivitySms.finish();
                finish();
            }
        }, 10000);
    }

    @Override
    public void onBackPressed() {
        if (! allowBack){

        } else {
            super.onBackPressed();
        }
    }
}
