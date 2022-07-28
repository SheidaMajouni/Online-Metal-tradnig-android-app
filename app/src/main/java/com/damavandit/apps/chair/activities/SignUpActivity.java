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
import android.widget.Toast;

import com.damavandit.apps.chair.MainActivity;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ronash.pushe.Pushe;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.CONFLICT_USERNAME;
import static com.damavandit.apps.chair.constants.Const.action.ERROR_IN_REGISTER;
import static com.damavandit.apps.chair.constants.Const.action.ONE_SIGNAL_NOTIFICATION_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PUSHEID_WAS_UPDATED;
import static com.damavandit.apps.chair.constants.Const.action.PUSHE_ID_DID_NOT_UPDATE;

public class SignUpActivity extends AppCompatActivity
        implements ServiceConnection {

    private AppMainService mMainService;
    private RegisterBroadcastReceiver mBroadcastReceiver;

    private TextInputEditText mEditPhone;
    private Button mButtonSignUp;
    private View mProgressView;
    private View mSignUpFormView;
    private Session session;
    private Intent mIntent;
    String pass = "$h@red.P@$$w0rd.4.@ll.U$er$";

    private Setting setting;
    public static AppCompatActivity mActivitySignUp;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = new Setting(SignUpActivity.this);
        setting.SetLocale("fa");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_sign_up);
        mActivitySignUp = this;

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new RegisterBroadcastReceiver();

        session = new Session(this);

        if (getIntent() != null) {
            mIntent = new Intent();
            mIntent = getIntent();
        }

        mEditPhone = findViewById(R.id.phone_edit_text);
        mButtonSignUp = findViewById(R.id.sign_up_button);


        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.sign_up_form);
        mProgressView = findViewById(R.id.sign_up_progress);
    }

    private void attemptSignUp() {
        mEditPhone.setError(null);

        String userName = mEditPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(userName)) {
            mEditPhone.setError(getString(R.string.error_field_required));
            focusView = mEditPhone;
            cancel = true;
        } else if (!isUserNameValid(userName)) {
            mEditPhone.setError(getString(R.string.error_invalid_user_name));
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
                    mMainService.deleteUser("09352640304");
                    mMainService.register(
                            mEditPhone.getText().toString(),
                            pass, pass, "hint");
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isUserNameValid(String userName) {
        String regex = "^09[0-9]{9}$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userName);

        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        String regex = "(?=^.{5,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mSignUpFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSignUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        filter.addAction(ONE_SIGNAL_NOTIFICATION_RECEIVED);
        filter.addAction(ACCESS_TOKEN_RECEIVED);
        filter.addAction(ACCESS_TOKEN_FAILED);
        filter.addAction(PUSHEID_WAS_UPDATED);
        filter.addAction(PUSHE_ID_DID_NOT_UPDATE);
        filter.addAction(ERROR_IN_REGISTER);
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
    public void onBackPressed() {


        if (mIntent.getStringExtra("FROM_ACTIVITY") != null && mIntent.getStringExtra("FROM_ACTIVITY").equals("shopping")) {
            finish();
            startActivity(new Intent(SignUpActivity.this, ShoppingCartActivity.class));
        } else if (mIntent.getStringExtra("FROM_ACTIVITY") != null && mIntent.getStringExtra("FROM_ACTIVITY").equals("chat")) {

            finish();
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));

        } else if (mIntent.getStringExtra("FROM_ACTIVITY") != null && mIntent.getStringExtra("FROM_ACTIVITY").equals("camera")) {
            finish();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class RegisterBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ACCESS_TOKEN_RECEIVED)) {
                    Pushe.initialize(SignUpActivity.this, true);
                    String pusheId = Pushe.getPusheId(SignUpActivity.this);

                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        if (mMainService != null) {
                            mMainService.updatePusheId(session.getUserId(), pusheId);
                        }
                    } else {
                        Toast.makeText(SignUpActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                    }

                } else if (intent.getAction().equals(ERROR_IN_REGISTER)) {
                    showProgress(false);
                    Toast.makeText(SignUpActivity.this, R.string.error_registration_failed, Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(PUSHEID_WAS_UPDATED)) {
                    showProgress(false);
                    Intent intent1 = new Intent(SignUpActivity.this, SmsVerificationActivity.class);
                    intent1.putExtra("EXTRA_PHONE", mEditPhone.getText().toString());
                    intent1.putExtra("FROM_ACTIVITY", "signIn");
                    startActivity(intent1);
                } else if (intent.getAction().equals(PUSHE_ID_DID_NOT_UPDATE)) {
                    showProgress(false);
                    Toast.makeText(SignUpActivity.this, R.string.error_registration_failed, Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(CONFLICT_USERNAME)) {
                    showProgress(false);
                    Toast.makeText(SignUpActivity.this, R.string.error_conflict, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
