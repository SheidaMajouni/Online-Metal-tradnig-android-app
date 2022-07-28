package com.damavandit.apps.chair.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.ProductGroupManufacturerModel2;
import com.damavandit.apps.chair.models.SectionItems;
import com.damavandit.apps.chair.newAdapters.MainRecyclerAdapter2;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED;

public class StoreFragment extends Fragment implements ServiceConnection {

    private RecyclerView mainRecycle;
    private Animation fade_in, fade_out;
    private ViewFlipper mViewFlipper;
    private View mProgressView;
    private ImageView mImageRefresh;

    private ArrayList<SectionItems> allSampleData;
    private ArrayList<ProductGroupManufacturerModel2> productGroupManufacturerModelList;
    private MainRecyclerAdapter2 mainRecyclerAdapter2;

    private AppMainService mMainService;
    private StoreBroadcastReceiver mBroadcastReceiver;

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mBroadcastReceiver = new StoreBroadcastReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        -------------------------slide show & animation------------------
        mViewFlipper = view.findViewById(R.id.slideShow);
        fade_in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);

        mViewFlipper.setInAnimation(fade_in);
        mViewFlipper.setOutAnimation(fade_out);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(3000);
        mViewFlipper.startFlipping();
//        -----------------------------recycle View--------------------------
        mainRecycle = view.findViewById(R.id.recycleView);
        productGroupManufacturerModelList = new ArrayList<>();
        mainRecyclerAdapter2 = new MainRecyclerAdapter2(getContext(), productGroupManufacturerModelList);
        mainRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mainRecycle.setAdapter(mainRecyclerAdapter2);
//      -------------------------------progress bar--------------------------
        mProgressView = view.findViewById(R.id.store_progress);
        mImageRefresh = view.findViewById(R.id.refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mImageRefresh.setVisibility(View.GONE);
                mMainService.getProductGroupManufacturerView();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mainRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        mainRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mainRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
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
    public void onPause() {
        super.onPause();
        getActivity().unbindService(this);
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED);
        filter.addAction(PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED);
        getActivity().registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
            showProgress(true);
            mMainService.getProductGroupManufacturerView();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class StoreBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(PRODUCT_GROUP_MANUFACTURER_LIST_RECEIVED)) {
                    showProgress(false);
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        productGroupManufacturerModelList = bundle.getParcelableArrayList(
                                "productGroupManufacturerModelList");

                        mainRecyclerAdapter2.setProductGroupManufacturerModelList(productGroupManufacturerModelList);
                        mainRecyclerAdapter2.notifyDataSetChanged();
                    }
                } else if (intent.getAction().equals(PRODUCT_GROUP_MANUFACTURER_LIST_DID_NOT_RECEIVED)) {
                    showProgress(false);
                    mImageRefresh.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
