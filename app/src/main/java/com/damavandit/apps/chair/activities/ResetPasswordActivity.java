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
import android.widget.Toast;

import com.damavandit.apps.chair.MainActivity;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_FAILED;
import static com.damavandit.apps.chair.constants.Const.action.ACCESS_TOKEN_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PASSWORD_HAS_BEEN_RESETED;

public class ResetPasswordActivity extends AppCompatActivity implements ServiceConnection {

    private TextInputEditText newPassword;
    private TextInputEditText newPasswordConfirm;
    private Button mButtonSetNewPassword;

    private AppMainService mMainService;
    private ResetPasswordBroadcastReceiver mBroadcastReceiver;
    private Session session;

    private String secondCode;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new ResetPasswordBroadcastReceiver();
        session = new Session(this);

        newPassword = findViewById(R.id.forget_password_edit_text);
        newPasswordConfirm = findViewById(R.id.forget_confirm_password_edit_text);
        mButtonSetNewPassword = findViewById(R.id.button_set_new_pass);

        if (getIntent() != null) {
            Intent mIntent = getIntent();
            secondCode = session.getSecondCode();
            userName = mIntent.getStringExtra("userName");
        }

        mButtonSetNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptResetPassword();
            }
        });

    }

    public void attemptResetPassword() {
        newPassword.setError(null);
        newPasswordConfirm.setError(null);


        String password = newPassword.getText().toString();
        String confirm = newPasswordConfirm.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            newPassword.setError(getString(R.string.error_invalid_password));
            focusView = newPassword;
            cancel = true;
        }

        if (TextUtils.isEmpty(confirm)) {
            newPasswordConfirm.setError(getString(R.string.error_field_required));
            focusView = newPasswordConfirm;
            cancel = true;
        } else if (!password.equals(confirm)) {
            newPasswordConfirm.setError(getString(R.string.error_invalid_confirm));
            focusView = newPasswordConfirm;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (mMainService != null) {
                mMainService.sendResetPassword(
                        userName,
                        newPassword.getText().toString(),
                        newPasswordConfirm.getText().toString(), secondCode);
            }
        }
    }

    private boolean isPasswordValid(String password) {
        String regex = "(?=^.{5,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
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
        filter.addAction(PASSWORD_HAS_BEEN_RESETED);
        filter.addAction(ACCESS_TOKEN_RECEIVED);
        filter.addAction(ACCESS_TOKEN_FAILED);
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

    public class ResetPasswordBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ACCESS_TOKEN_RECEIVED)) {
                    session.setLoggedin(true);
                    startActivity(new Intent(ResetPasswordActivity.this, MainActivity.class));
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
                    if (SmsVerificationActivity.mActivitySms != null) {
                        SmsVerificationActivity.mActivitySms.finish();
                    }
                    finish();
                } else if (intent.getAction().equals(ACCESS_TOKEN_FAILED)) {
                    Toast.makeText(ResetPasswordActivity.this, R.string.error_reset_pass_failed, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

