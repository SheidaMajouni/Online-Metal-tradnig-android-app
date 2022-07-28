package com.damavandit.apps.chair.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damavandit.apps.chair.MainActivity;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_DID_NOT_SEND;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_FILE_HAS_BEEN_SENT_SUCCESSFULLY;

public class CameraActivity extends AppCompatActivity implements ServiceConnection {

    private AppMainService mMainService;
    private SendOrderImageBroadcastReceiver mBroadcastReceiver;

    private ImageView mImageGallery;
    private ImageView mImageCamera;
    private ImageView mImageUploaded;
    private TextView mTextNoImage;
    private Button mButtonSumbit;
    private View mProgressView;

    //image
    private static final int CAMERA_ID = 1;
    private static final int SDCard_ID = 2;
    private Uri image_uri;
    private Bitmap bitmap;
    private String path = "";

    public static AppCompatActivity mActivityCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Setting setting = new Setting(CameraActivity.this);
        setting.SetLocale("fa");

        startService(new Intent(this, AppMainService.class));
        mBroadcastReceiver = new SendOrderImageBroadcastReceiver();
        final Session session = new Session(CameraActivity.this);
        mActivityCamera = this;

        mImageGallery = findViewById(R.id.gallery_image_button);
        mImageCamera = findViewById(R.id.camera_image_button);
        mImageUploaded = findViewById(R.id.uploaded_image);
        mTextNoImage = findViewById(R.id.text_noImage);
        mButtonSumbit = findViewById(R.id.submit_button_photo);
        mProgressView = findViewById(R.id.store_progress);

        mImageGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SDCard_ID);
            }
        });

        mImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(CameraActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_ID);
            }
        });

        mButtonSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMainService != null && !path.equals("")) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        if (session.getUserInfoComplete()){
                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                            final int random = new Random().nextInt((999999999 - 10000000) + 1) + 10000000;
                            showProgress(true);
                            mMainService.uploadOrderImage(path,random);
                        } else {
                            startActivity(new Intent(CameraActivity.this, UserInfoActivity.class));
                        }
                    } else {
                        Toast.makeText(CameraActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == CAMERA_ID) {
            bitmap = data.getParcelableExtra("data");
            Glide.with(CameraActivity.this)
                    .load(bitmap)
                    .into(mImageUploaded);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path1 = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            Uri uri = Uri.parse(path1);

            path = getPath(CameraActivity.this,uri);
            mTextNoImage.setVisibility(View.INVISIBLE);
        }
        if (resultCode == RESULT_OK && requestCode == SDCard_ID) {
            image_uri = data.getData();
            Glide.with(CameraActivity.this)
                    .load(image_uri)
                    .into(mImageUploaded);
            path = getPath(CameraActivity.this,image_uri);
            mTextNoImage.setVisibility(View.INVISIBLE);
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return
                "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return
                "com.google.android.apps.photos.content".equals(uri.getAuthority());
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
                    Toast.makeText(CameraActivity.this, "عکس ارسال شد", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if (intent.getAction().equals(ORDER_FILE_DID_NOT_SEND)) {
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.error_upload_image), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

