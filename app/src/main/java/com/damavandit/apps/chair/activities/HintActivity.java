package com.damavandit.apps.chair.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;

public class HintActivity extends AppCompatActivity {

    private TextView mTextHint;
    private Button mButtonSignIn;
    private Button mButtonCall;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hint);

        mTextHint = findViewById(R.id.hint);
        mButtonSignIn = findViewById(R.id.sign_in_from_hint);
        mButtonCall = findViewById(R.id.call);

        Session session = new Session(this);
        mTextHint.setText(session.getSecondCode());

        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HintActivity.this, SignInActivity.class));

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
            }
        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:02144294631"));
                startActivity(callIntent);
            }
        });
    }
}
