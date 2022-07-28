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
import android.graphics.Typeface;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.RegisterRecycleAdapter;
import com.damavandit.apps.chair.dbModels.OrderServer;
import com.damavandit.apps.chair.models.RegisterStatus;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_DID_NOT_RECIEVE;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_IS_EMPTY;
import static com.damavandit.apps.chair.constants.Const.action.LAST_TEN_ORDERS_LIST_RECEIVED;

public class RegisterFragment extends Fragment implements ServiceConnection {

    private ArrayList<RegisterStatus> mRegisterList, mRegisterSubmitedList;
    private ArrayList<OrderServer> mLastTenOrders;

    private RecyclerView registerRecycle, registerRecycleSubmit;
    private Button statusButton;
    private TabHost mTabHost;
    private View mProgressView;
    private ImageView mImageRefresh;
    private TextView emptyText;

    private int loadItemIndex;
    private int pageNumber = 1;

    private AppMainService mMainService;
    private LastTenOrdersBroadcastReceiver mTenOrderReceiver;
    private RegisterRecycleAdapter registerAdapter, registerAdapterSubmit;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mTenOrderReceiver = new LastTenOrdersBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_list_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerRecycle = view.findViewById(R.id.register_recycle);
        registerRecycleSubmit = view.findViewById(R.id.register_recycle_second);
        mRegisterList = new ArrayList<RegisterStatus>();
        mRegisterSubmitedList = new ArrayList<>();
        mLastTenOrders = new ArrayList<>();

//------------------------------refresh-------------------------------------
        final Session session = new Session(getContext());
        emptyText = view.findViewById(R.id.empty_list);
        mProgressView = view.findViewById(R.id.store_progress);
        mImageRefresh = view.findViewById(R.id.refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mImageRefresh.setVisibility(View.GONE);
                mMainService.getLastTenOrders(session.getUserId(), pageNumber);
            }
        });

//------------------------------set fonts------------------------------------
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
        TextView title = view.findViewById(R.id.registered_text);
        title.setTypeface(face);

        TextView statusText = view.findViewById(R.id.status_registered);
        TextView statusText2 = view.findViewById(R.id.status_registered_second);
        statusText.setTypeface(face);
        statusText2.setTypeface(face);

        TextView timerText = view.findViewById(R.id.timer_registered);
        TextView timerText2 = view.findViewById(R.id.timer_registered_second);
        timerText.setTypeface(face);
        timerText2.setTypeface(face);

        TextView dateText = view.findViewById(R.id.date_registered);
        TextView dateText2 = view.findViewById(R.id.date_registered_second);
        dateText.setTypeface(face);
        dateText2.setTypeface(face);

        TextView rowText = view.findViewById(R.id.row_registered);
        TextView rowText2 = view.findViewById(R.id.row_registered_second);
        rowText.setTypeface(face);
        rowText2.setTypeface(face);

//------------------get argument from order fragment--------------------------
//----------and send it in a string list for register adapter

        Bundle bundle = getArguments();
        String fromDrawer = bundle.getString("drawer");

        registerAdapter = new RegisterRecycleAdapter(getContext(), mRegisterList, fromDrawer);
        registerRecycle.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));
        registerRecycle.setAdapter(registerAdapter);

        registerAdapterSubmit = new RegisterRecycleAdapter(getContext(), mRegisterSubmitedList, fromDrawer);
        registerRecycleSubmit.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));
        registerRecycleSubmit.setAdapter(registerAdapterSubmit);

//----------------------------------tab Host-----------------------------------
        mTabHost = view.findViewById(R.id.tabHost);
        mTabHost.setup();

        // tab1
        TabHost.TabSpec sepc = mTabHost.newTabSpec("tab one");
        sepc.setContent(R.id.tab1);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_host, null);
        TextView tv = v.findViewById(R.id.tabsText);
        tv.setText(getResources().getString(R.string.my_orders));
        tv.setTypeface(face);
        sepc.setIndicator(v);
        mTabHost.addTab(sepc);

        // tab2
        sepc = mTabHost.newTabSpec("tab two");
        sepc.setContent(R.id.tab2);
        View v2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_second_tab_host, null);
        TextView tv2 = v2.findViewById(R.id.tabsText_second);
        tv2.setText(getResources().getString(R.string.my_shops));
        tv2.setTypeface(face);
        sepc.setIndicator(v2);
        mTabHost.addTab(sepc);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        registerRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        registerRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                registerRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
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
        getActivity().unregisterReceiver(mTenOrderReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(LAST_TEN_ORDERS_LIST_RECEIVED);
        filter.addAction(LAST_TEN_ORDERS_IS_EMPTY);
        filter.addAction(LAST_TEN_ORDERS_DID_NOT_RECIEVE);
        getActivity().registerReceiver(mTenOrderReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
            Session session = new Session(getContext());
            showProgress(true);
            mMainService.getLastTenOrders(session.getUserId(), pageNumber);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class LastTenOrdersBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(LAST_TEN_ORDERS_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        showProgress(false);
                        mLastTenOrders.clear();
                        mRegisterList.clear();
                        mRegisterSubmitedList.clear();
                        mLastTenOrders = bundle.getParcelableArrayList(
                                "lastTenOrders");

                        for (int i = 0; i < mLastTenOrders.size(); i++) {
                            RegisterStatus registerStatus = new RegisterStatus();
                            registerStatus.setOrderId(mLastTenOrders.get(i).getOrderId());
                            registerStatus.setDate(mLastTenOrders.get(i).getOrderDate().toString());
                            registerStatus.setStatusId(mLastTenOrders.get(i).getOrderStatusId());
                            registerStatus.setTrackingCode(mLastTenOrders.get(i).getmTrackingCode());

                            if (mLastTenOrders.get(i).getOrderStatusId() == 3) {
                                mRegisterSubmitedList.add(registerStatus);
                            } else {
                                mRegisterList.add(registerStatus);
                            }
                        }
                        registerAdapter.notifyDataSetChanged();
                        registerAdapterSubmit.notifyDataSetChanged();
                    }
                }else if( intent.getAction().equals(LAST_TEN_ORDERS_IS_EMPTY)){
                    showProgress(false);
                    emptyText.setVisibility(View.VISIBLE);
                } else if (intent.getAction().equals(LAST_TEN_ORDERS_DID_NOT_RECIEVE)){
                    showProgress(false);
                    mImageRefresh.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), getString(R.string.error_last_ten_order), Toast.LENGTH_LONG).show();
                }
                else {
                    showProgress(false);
                    mImageRefresh.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
