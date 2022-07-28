package com.damavandit.apps.chair.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.MainActivity;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.controllers.TokenManager;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.Random;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.CAN_NOT_LOGIN;
import static com.damavandit.apps.chair.constants.Const.action.ERROR_IN_LOGIN;
import static com.damavandit.apps.chair.constants.Const.action.LOGED_IN;

public class SignInActivity extends AppCompatActivity
        implements ServiceConnection {

    private AppMainService mMainService;
    private MainBroadcastReceiver mBroadcastReceiver;

    private TextInputEditText mEditPhone;
    private TextView mTextClickRegister;
    private Button mButtonSignIn;
    private Session session;
    private Setting setting;
    private View mProgressView;
    private View mLoginFormView;
    public static AppCompatActivity mActivitySignIn;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = new Setting(SignInActivity.this);
        setting.SetLocale("fa");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sign_in);
        mActivitySignIn = this;

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new MainBroadcastReceiver();

        session = new Session(this);

        session.setLoggedin(false);
        if (session.loggedin()) {
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
            finish();
        }

        mEditPhone = findViewById(R.id.phone_edit_text_signIn);
        mButtonSignIn = findViewById(R.id.sign_up_button_signIn);
        mTextClickRegister = findViewById(R.id.click_register);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        mTextClickRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
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
        filter.addAction(ACCESS_TOKEN_RECEIVED);
        filter.addAction(ACCESS_TOKEN_FAILED);
        filter.addAction(ERROR_IN_LOGIN);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void attemptLogin() {
        mEditPhone.setError(null);
        String userName = mEditPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userName)) {
            mEditPhone.setError(getString(R.string.error_field_required));
            focusView = mEditPhone;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                if (mMainService != null) {
                    mMainService.login(
                            mEditPhone.getText().toString(),
                            "$h@red.P@$$w0rd.4.@ll.U$er$");
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
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

    public class MainBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ACCESS_TOKEN_RECEIVED)) {
                    showProgress(false);
                    Intent intent1 = new Intent(SignInActivity.this, SmsVerificationActivity.class);
                    intent1.putExtra("EXTRA_PHONE", mEditPhone.getText().toString());
                    intent1.putExtra("FROM_ACTIVITY", "signIn");
                    startActivity(intent1);
                } else if (intent.getAction().equals(ACCESS_TOKEN_FAILED)) {
//                    showProgress(false);
//                    Toast.makeText(SignInActivity.this, R.string.error_login, Toast.LENGTH_LONG).show();
//                    TokenManager.getAccessToken(mEditPhone.getText().toString(),"$h@red.P@$$w0rd.4.@ll.U$er$");
                } else if (intent.getAction().equals(ERROR_IN_LOGIN)) {
                    showProgress(false);
                    Toast.makeText(SignInActivity.this, getString(R.string.error_login), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

