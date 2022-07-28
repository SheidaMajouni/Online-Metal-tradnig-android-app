package com.damavandit.apps.chair.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.UserFullInfo;
import com.damavandit.apps.chair.fragments.RegisterFragment;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_DID_NOT_SEND;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY;

public class UserInfoActivity extends AppCompatActivity
        implements ServiceConnection {

    private AppMainService mMainService;
    private InfoBroadcastReceiver mBroadcastReceiver;

    private Setting setting;

    private TextView mTextName;
    private TextView mTextFamily;
    private TextView mTextEmail;
    private TextView mTextAddress;
    private Button mButtonSendUserInfo;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = new Setting(UserInfoActivity.this);
        setting.SetLocale("fa");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_user_info);

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new InfoBroadcastReceiver();

        mTextName = findViewById(R.id.name_text);
        mTextFamily = findViewById(R.id.text_family);
        mTextEmail = findViewById(R.id.email_complete_text);
        mTextAddress = findViewById(R.id.address_comp_text);
        mButtonSendUserInfo = findViewById(R.id.button_send_user_info);

        final UserFullInfo userFullInfo = new UserFullInfo(mTextName.getText().toString(),
                mTextFamily.getText().toString(),
                mTextEmail.getText().toString(),
                mTextAddress.getText().toString());

        mButtonSendUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (mMainService != null) {
                        mMainService.sendFullUserInfo(userFullInfo);
                    }
                } else {
                    Toast.makeText(UserInfoActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
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
        filter.addAction(USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY);
        filter.addAction(USER_INFO_DID_NOT_SEND);
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

    public class InfoBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(USER_INFO_HAS_BEEN_SENT_SUCCESSFULLY)) {

                    Session session = new Session(UserInfoActivity.this);
                    session.setUserInfoComplete(true);

//                    RegisterFragment fragment = RegisterFragment.newInstance();
//                    Bundle args = new Bundle();
//                    args.putString("drawer", "shopping");
//                    fragment.setArguments(args);
//
//                    String fragmentTag = fragment.getClass().getName();
//                    boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);
//
//                    if (!fragmentPopped) { //fragment not in back stack, create it.
//                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                        ft.replace(R.id.basket_container, fragment);
//                        ft.addToBackStack(fragmentTag);
//                        ft.commit();
//                    }

//                    finish();
                    onBackPressed();
                } else if (intent.getAction().equals(USER_INFO_DID_NOT_SEND)) {
                    Toast.makeText(UserInfoActivity.this, R.string.user_info_did_not_send_try, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
