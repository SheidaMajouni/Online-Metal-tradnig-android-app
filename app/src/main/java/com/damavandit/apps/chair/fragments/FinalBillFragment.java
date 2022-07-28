package com.damavandit.apps.chair.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.FinalBill;
import com.damavandit.apps.chair.services.AppMainService;

import saman.zamani.persiandate.PersianDate;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.FINAL_BILL_LIST_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.FINAL_BILL_LIST_RECEIVED;

public class FinalBillFragment extends Fragment implements ServiceConnection {

    private FinalBill mFinalBill;

    private AppMainService mMainService;
    private FinalBillBroadcastReceiver finalReceiver;
    private DatabaseHelper db;

    private TextView trackingCode;
    private TextView orderer;
    private TextView invoiceAmount;
    private TextView tolerance;
    private TextView payableAmount;
    private TextView orderDate;
    private TextView orderStatus;

    public static FinalBillFragment newInstance() {
        FinalBillFragment fragment = new FinalBillFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getContext());
        getActivity().startService(new Intent(getContext(), AppMainService.class));
        finalReceiver = new FinalBillBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_final_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");

        trackingCode = view.findViewById(R.id.id_number);
        orderer = view.findViewById(R.id.customer_name_text);
        invoiceAmount = view.findViewById(R.id.bill_price_text);
        tolerance = view.findViewById(R.id.tolerance_text);
        payableAmount = view.findViewById(R.id.final_cost_text);
        orderDate = view.findViewById(R.id.order_date_text);
        orderStatus = view.findViewById(R.id.order_status_text);

        setFont(view, face);

    }

    public void setFont(View view, Typeface face) {
        TextView final_bil_text = view.findViewById(R.id.final_bil_text);
        TextView id_text = view.findViewById(R.id.id_text);
        TextView order_date = view.findViewById(R.id.order_date);
        TextView customer_name = view.findViewById(R.id.customer_name);
        TextView order_status = view.findViewById(R.id.order_status);
        TextView bill_price = view.findViewById(R.id.bill_price);
        TextView tolerancee = view.findViewById(R.id.tolerance);
        TextView final_cost = view.findViewById(R.id.final_cost);
        TextView text_pick = view.findViewById(R.id.text_pick);
        final_bil_text.setTypeface(face);
        id_text.setTypeface(face);
        order_date.setTypeface(face);
        customer_name.setTypeface(face);
        order_status.setTypeface(face);
        bill_price.setTypeface(face);
        tolerancee.setTypeface(face);
        final_cost.setTypeface(face);
        text_pick.setTypeface(face);
        trackingCode.setTypeface(face);
        orderer.setTypeface(face);
        invoiceAmount.setTypeface(face);
        tolerance.setTypeface(face);
        payableAmount.setTypeface(face);
        orderDate.setTypeface(face);
        orderStatus.setTypeface(face);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unbindService(this);
        getActivity().unregisterReceiver(finalReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(FINAL_BILL_LIST_RECEIVED);
        getActivity().registerReceiver(finalReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Bundle bundle = getArguments();
        int orderId = bundle.getInt("orderId");

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();

            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                if (mMainService != null) {
                    mMainService.getFinalBillList(orderId);
                }
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }


    public class FinalBillBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(FINAL_BILL_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        mFinalBill = bundle.getParcelable("finalBillList");

                        trackingCode.setText(String.valueOf(mFinalBill.getTrackingCode()));
                        orderer.setText(String.valueOf(mFinalBill.getOrderer()));
                        invoiceAmount.setText(String.valueOf(mFinalBill.getInvoiceAmount()));
                        tolerance.setText(String.valueOf(mFinalBill.getFaultTolerance()));
                        payableAmount.setText(String.valueOf(mFinalBill.getPayableAmount()));

                        //persian date
                        String gregorianDate = String.valueOf(mFinalBill.getOrderDate().toString().substring(0, 10));
                        final PersianDate pDate = new PersianDate();
                        int year = Integer.valueOf(gregorianDate.substring(0, 4));
                        int month = Integer.valueOf(gregorianDate.substring(5, 7));
                        int day = Integer.valueOf(gregorianDate.substring(8, 10));

                        int[] persianDate = pDate.toJalali(year, month, day);
                        final String showDate = (persianDate[0] + "/" + persianDate[1] + "/" + persianDate[2]);
                        orderDate.setText(showDate);


                        orderStatus.setText(db.getOrderStatusById(mFinalBill.getOrderStatusId()).getOrderStatusName());
                        db.closeDB();
                    }
                } else if (intent.getAction().equals(FINAL_BILL_LIST_NOT_RECEIVED)) {
                    trackingCode.setVisibility(View.INVISIBLE);
                    orderer.setVisibility(View.INVISIBLE);
                    invoiceAmount.setVisibility(View.INVISIBLE);
                    tolerance.setVisibility(View.INVISIBLE);
                    payableAmount.setVisibility(View.INVISIBLE);
                    orderDate.setVisibility(View.INVISIBLE);
                    orderStatus.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
