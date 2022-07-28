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
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.damavandit.apps.chair.MainActivity;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.FAIL_VERIFY;
import static com.damavandit.apps.chair.constants.Const.action.FAIL_VERIFY_SERVER;
import static com.damavandit.apps.chair.constants.Const.action.SECOND_CODE_WAS_CORRECT;
import static com.damavandit.apps.chair.constants.Const.action.VERIFICATION_CODE_WAS_CORRECT;

public class SmsVerificationActivity extends AppCompatActivity
        implements ServiceConnection {

    private AppMainService mMainService;
    private VerificationBroadcastReceiver mBroadcastReceiver;

    private Session session;

    private EditText mEditCode;
    private Button mButtonSubmit;
    private View mProgressView;
    private View mVerifyFormView;
    private Handler handler;
    private Runnable myRunnable;
    private boolean goToMain = true;
    private boolean fromShopping = false;
    private boolean fromChat = false;

    private String phone;
    public static AppCompatActivity mActivitySms;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sms_verification);

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new VerificationBroadcastReceiver();
        session = new Session(this);
        mActivitySms = this;

        if (getIntent() != null) {
            Intent mIntent = getIntent();
            if (mIntent.getStringExtra("FROM_ACTIVITY").equals("forget")) {
                goToMain = false;
            } else if (mIntent.getStringExtra("FROM_ACTIVITY").equals("shopping")) {
                fromShopping = true;
            } else if (mIntent.getStringExtra("FROM_ACTIVITY").equals("chat")) {
                fromChat = true;
            }
        }

        handler = new Handler();
        myRunnable = new Runnable() {
            public void run() {
                startActivity(new Intent(SmsVerificationActivity.this, LockedOutActivity.class));
            }
        };
        handler.postDelayed(myRunnable, 300000);

        phone = getIntent().getStringExtra("EXTRA_PHONE");

        mEditCode = findViewById(R.id.enter_code);
        mButtonSubmit = findViewById(R.id.submit);

        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptVerify();
            }
        });

        mVerifyFormView = findViewById(R.id.verify_form);
        mProgressView = findViewById(R.id.verify_progress);
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
        filter.addAction(SECOND_CODE_WAS_CORRECT);
        filter.addAction(VERIFICATION_CODE_WAS_CORRECT);
        filter.addAction(FAIL_VERIFY);
        filter.addAction(FAIL_VERIFY_SERVER);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private void attemptVerify() {
        showProgress(true);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (mMainService != null) {
                mMainService.verifyCode(phone, mEditCode.getText().toString());
            }
        } else {
            Toast.makeText(SmsVerificationActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mVerifyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mVerifyFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mVerifyFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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


    public class VerificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(VERIFICATION_CODE_WAS_CORRECT)) {
                    if (goToMain) {
                        showProgress(false);
                        session.setLoggedin(true);
                        sendBroadcast(intent);
                        startActivity(new Intent(SmsVerificationActivity.this, MainActivity.class));
                        if (SignInActivity.mActivitySignIn != null) {
                            SignInActivity.mActivitySignIn.finish();
                        }
                        if (SignUpActivity.mActivitySignUp != null) {
                            SignUpActivity.mActivitySignUp.finish();
                        }
                        if (LockedOutActivity.mActivityLockedOut != null) {
                            LockedOutActivity.mActivityLockedOut.finish();
                        }
                        if (ForgetPasswordActivity.mActivityForgetPass != null) {
                            ForgetPasswordActivity.mActivityForgetPass.finish();
                        }
                        handler.removeCallbacks(myRunnable);
                        finish();
                    } else if (!goToMain) {
                        handler.removeCallbacks(myRunnable);
                        Intent intent2 = new Intent(SmsVerificationActivity.this, HintActivity.class);
                        intent2.putExtra("userName", phone);
                        startActivity(intent2);
                    }
                } else if (intent.getAction().equals(ACCESS_TOKEN_FAILED)) {
                    showProgress(false);
                    Toast.makeText(SmsVerificationActivity.this, R.string.error_registration_failed, Toast.LENGTH_LONG).show();
                    if (mMainService != null) {
                        Toast.makeText(SmsVerificationActivity.this, getResources().getString(R.string.sms_resend), Toast.LENGTH_SHORT).show();
                        mMainService.login(
                                phone,
                                "$h@red.P@$$w0rd.4.@ll.U$er$");
                    }
                } else if (intent.getAction().equals(FAIL_VERIFY_SERVER)) {
                    showProgress(false);
                    if (mMainService != null) {
                        Toast.makeText(SmsVerificationActivity.this, getResources().getString(R.string.sms_resend), Toast.LENGTH_SHORT).show();
                        mMainService.login(
                                phone,
                                "$h@red.P@$$w0rd.4.@ll.U$er$");
                    }
                } else if (intent.getAction().equals(FAIL_VERIFY)) {
                    showProgress(false);
                    Toast.makeText(SmsVerificationActivity.this, getResources().getString(R.string.wrong_sms_code_interred), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }
        }
    }
}

