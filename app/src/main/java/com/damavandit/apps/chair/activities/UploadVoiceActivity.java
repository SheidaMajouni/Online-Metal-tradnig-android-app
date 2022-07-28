package com.damavandit.apps.chair.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.Random;

import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_DID_NOT_SEND;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY;

public class UploadVoiceActivity extends AppCompatActivity implements ServiceConnection {

    private ImageView green;
    private ImageView mImageVoice;
    private Button sendFile;
    private String path = "";
    private View mProgressView;

    int RQS_RECORDING = 1;

    private AppMainService mMainService;
    private SendOrderImageBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_voice);

        Setting setting = new Setting(UploadVoiceActivity.this);
        setting.SetLocale("fa");

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new SendOrderImageBroadcastReceiver();

        green = findViewById(R.id.green);
        sendFile = findViewById(R.id.send_voice);
        mImageVoice = findViewById(R.id.voice_pic);
        mProgressView = findViewById(R.id.store_progress);

        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, RQS_RECORDING);
            }
        });

        sendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session(UploadVoiceActivity.this);
                if (mMainService != null && !path.equals("")) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        if (session.getUserInfoComplete()){
                            ActivityCompat.requestPermissions(UploadVoiceActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                            final int random = new Random().nextInt((999999999 - 10000000) + 1) + 10000000;
                            showProgress(true);
                            mMainService.uploadOrderImage(path,random);
                        }else {
                            startActivity(new Intent(UploadVoiceActivity.this, UserInfoActivity.class));
                        }
                    } else {
                        Toast.makeText(UploadVoiceActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RQS_RECORDING) {

            if (resultCode == Activity.RESULT_OK) {

                mImageVoice.setVisibility(View.VISIBLE);

                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                if (cursor == null) {
                    path = data.getData().getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cursor.getString(idx);
                }
                String c = "jhg";

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
    protected void onResume() {
        super.onResume();
        Intent service = new Intent(this, AppMainService.class);
        bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY);
        filter.addAction(ORDER_FILE_DID_NOT_SEND);
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

    public class SendOrderImageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY)) {
                    Toast.makeText(UploadVoiceActivity.this, "صدا ارسال شد", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if (intent.getAction().equals(ORDER_FILE_DID_NOT_SEND)) {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_upload_image), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
