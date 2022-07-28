package com.damavandit.apps.chair.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.services.AppMainService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.damavandit.apps.chair.constants.Const.action.USERNAME_IS_EXIST_SENT_WITH_CODE;

public class ForgetPasswordActivity extends AppCompatActivity implements ServiceConnection {

    private Button mButtonGetCode;
    private TextInputEditText mTextPhone;

    private AppMainService mMainService;
    private ForgetPasswordBroadcastReceiver mBroadcastReceiver;

    private String code;
    public static AppCompatActivity mActivityForgetPass;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mActivityForgetPass = this;
        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new ForgetPasswordBroadcastReceiver();

        mButtonGetCode = findViewById(R.id.button_get_code);
        mTextPhone = findViewById(R.id.phone_edit_text_forget);

        mButtonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainService != null) {
                    mMainService.forgetPassword(mTextPhone.getText().toString());
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, AppMainService.class);
        bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(USERNAME_IS_EXIST_SENT_WITH_CODE);
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class ForgetPasswordBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(USERNAME_IS_EXIST_SENT_WITH_CODE)) {
                    Intent intent1 = new Intent(ForgetPasswordActivity.this, SmsVerificationActivity.class);
                    intent1.putExtra("EXTRA_PHONE", mTextPhone.getText().toString());
                    intent1.putExtra("FROM_ACTIVITY", "forget");
                    startActivity(intent1);
                }
            }
        }
    }
}
