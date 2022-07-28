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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.PreBillRecycleAdapter;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.ConfirmPreBill;
import com.damavandit.apps.chair.dbModels.ConfirmPreBillDetail;
import com.damavandit.apps.chair.dbModels.PreBill;
import com.damavandit.apps.chair.dbModels.RejectPreBill;
import com.damavandit.apps.chair.models.OrderModel;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_HAS_CHANGED;
import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_LIST_DID_NOT_RECEIVE;
import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_LIST_RECEIVED;

public class PreBillFragment extends Fragment implements ServiceConnection {

    private PreBill preBillList;
    private ArrayList<OrderModel> orders;
    private boolean hasChanged = false;

    private RecyclerView mPreBillRecycle;
    private Button confirmButton;
    private Button cancelButton;
    private Button editButton;
    private TextView billText;
    private TextView taxText;
    private TextView totelPriceText;
    private TextView billDate;
    private TextView billCode;
    private TextInputEditText mTextSpecifications;
    private View mProgressView;
    private ImageView mImageRefresh;
    private ConstraintLayout constraintLayout;

    private AppMainService mMainService;
    private PreBillBroadcastReceiver mPreBillReceiver;
    private PreBillRecycleAdapter preBillAdapter;
    private DatabaseHelper db;
    private Bundle args;
    private int orderId;
    private int containerId;

    public static PreBillFragment newInstance() {
        PreBillFragment fragment = new PreBillFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(getContext());
        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mPreBillReceiver = new PreBillBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pre_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");

        mPreBillRecycle = view.findViewById(R.id.pre_bill_recycle);
        confirmButton = view.findViewById(R.id.confirm_button);
        cancelButton = view.findViewById(R.id.cancle_button);
        editButton = view.findViewById(R.id.edit_button);
        billText = view.findViewById(R.id.pre_bill_price_text);
        taxText = view.findViewById(R.id.tax);
        totelPriceText = view.findViewById(R.id.total_price);
        billDate = view.findViewById(R.id.producer_info);
        billCode = view.findViewById(R.id.producer);
        mTextSpecifications = view.findViewById(R.id.customer_info_text);
        constraintLayout = view.findViewById(R.id.constrain_pre);
        orders = new ArrayList<OrderModel>();

        setFont(view, face);

//---------------------------refresh----------------------------------
        Bundle bundle = getArguments();
        final int orderId1 = bundle.getInt("orderId");
        mProgressView = view.findViewById(R.id.store_progress);
        mImageRefresh = view.findViewById(R.id.refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mImageRefresh.setVisibility(View.GONE);
                mMainService.getPreBillList(orderId1);
            }
        });

//----------------get argument from register adapter----------------------------

        args = new Bundle();
        orderId = bundle.getInt("orderId");
        args.putInt("orderId", orderId);

        String fromDrawer = bundle.getString("drawer");
        if (fromDrawer.equals("shopping")) {
            containerId = R.id.basket_container;
            args.putString("drawer", "shopping");
        } else {
            containerId = R.id.container;
            args.putString("drawer", "drawer");
        }

        String date = bundle.getString("date");
        billDate.setText(date);
        billCode.setText(bundle.getString("code"));

//-----------------------------make orderList for show and pass it to adapter-----------------------

        preBillAdapter = new PreBillRecycleAdapter(getContext(), orders, hasChanged);
        mPreBillRecycle.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));
        mPreBillRecycle.setAdapter(preBillAdapter);

//------------------put argument for final bill fragment-----------------------------

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTextSpecifications.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getContext().getString(R.string.enter_specifications), Toast.LENGTH_SHORT).show();
                } else {

                    ArrayList<ConfirmPreBillDetail> confirmPreBillDetailList = new ArrayList<>();
                    for (int i = 0; i < orders.size(); i++) {
                        ConfirmPreBillDetail confirmPreBillDetail = new ConfirmPreBillDetail();
                        confirmPreBillDetail.setQuantity(orders.get(i).getCount());
                        confirmPreBillDetail.setOrderDetailId(orders.get(0).getOrderDetailsId());
                        confirmPreBillDetailList.add(confirmPreBillDetail);
                    }
                    db.closeDB();

                    ConfirmPreBill confirmPreBill = new ConfirmPreBill();
                    confirmPreBill.setOrderId(orderId);
                    confirmPreBill.setSpecifications(mTextSpecifications.getText().toString());
                    confirmPreBill.setConfirmPreBills(confirmPreBillDetailList);

                    ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                        if (mMainService != null) {
                            mMainService.sendConfirmPreBill(confirmPreBill);
                        }
                    } else {
                        Toast.makeText(getContext(), getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RejectPreBill rejectPreBill = new RejectPreBill();
                rejectPreBill.setOrderId(orderId);

                ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (mMainService != null) {
                        mMainService.sendCancelPreBill(rejectPreBill);
                    }
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar
                        .make(constraintLayout, getString(R.string.edit_enable), Snackbar.LENGTH_LONG);
                snackbar.show();
                hasChanged = true;
                PreBillRecycleAdapter.hasChenged = true;
                preBillAdapter.notifyDataSetChanged();
            }
        });
    }

    public void setFont(View view, Typeface face) {
        TextView preBilText = view.findViewById(R.id.pre_bil_text);
        preBilText.setTypeface(face);
        billCode.setTypeface(face);
        billDate.setTypeface(face);
        TextView customer = view.findViewById(R.id.customer);
        customer.setTypeface(face);
        TextView deliver_pre_bill = view.findViewById(R.id.deliver_pre_bill);
        deliver_pre_bill.setTypeface(face);
        TextView total_price_pre_bill = view.findViewById(R.id.total_price_pre_bill);
        total_price_pre_bill.setTypeface(face);
        TextView unit_pre_bill = view.findViewById(R.id.unit_pre_bill);
        unit_pre_bill.setTypeface(face);
        TextView count_pre_bill = view.findViewById(R.id.count_pre_bill);
        count_pre_bill.setTypeface(face);
        TextView price_pre_bill = view.findViewById(R.id.price_pre_bill);
        price_pre_bill.setTypeface(face);
        TextView explain_pre_bill = view.findViewById(R.id.explain_pre_bill);
        explain_pre_bill.setTypeface(face);
        TextView row_registered = view.findViewById(R.id.row_registered);
        row_registered.setTypeface(face);
        TextView pre_bill_price = view.findViewById(R.id.pre_bill_price);
        pre_bill_price.setTypeface(face);
        TextView tax_text = view.findViewById(R.id.tax_text);
        tax_text.setTypeface(face);
        TextView total_text = view.findViewById(R.id.total_text);
        total_text.setTypeface(face);
        TextView text_aaa = view.findViewById(R.id.text_aaa);
        text_aaa.setTypeface(face);
        TextView text_bbb = view.findViewById(R.id.text_bbb);
        text_bbb.setTypeface(face);

        confirmButton.setTypeface(face);
        cancelButton.setTypeface(face);
        editButton.setTypeface(face);
        billText.setTypeface(face);
        taxText.setTypeface(face);
        totelPriceText.setTypeface(face);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mPreBillRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        mPreBillRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mPreBillRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
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
        getActivity().unregisterReceiver(mPreBillReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(PRE_BILL_LIST_RECEIVED);
        filter.addAction(PRE_BILL_HAS_CHANGED);
        filter.addAction(CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY);
        filter.addAction(CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY);
        filter.addAction(PRE_BILL_LIST_DID_NOT_RECEIVE);
        getActivity().registerReceiver(mPreBillReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Bundle bundle = getArguments();
        int orderId1 = bundle.getInt("orderId");

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();

            ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                if (mMainService != null) {
                    showProgress(true);
                    mMainService.getPreBillList(orderId1);
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

    public class PreBillBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(PRE_BILL_LIST_RECEIVED)) {
                    showProgress(false);
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        preBillList = (PreBill) bundle.getParcelable("preBillList");
                        orders.clear();

                        for (int i = 0; i < preBillList.getPreBillDetails().size(); i++) {
                            OrderModel order1 = new OrderModel();
                            order1.setRow(String.valueOf(i + 1));

                            order1.setOrderDetailsId(preBillList.getPreBillDetails().get(i).getOrderDetailsId());
                            order1.setProductName(preBillList.getPreBillDetails().get(i).getProductName());
                            order1.setProductSubGroup(preBillList.getPreBillDetails().get(i).getProductSubGroupName());
                            order1.setDefaultSize(preBillList.getPreBillDetails().get(i).getDefaultSizeValue());

                            order1.setExplain(preBillList.getPreBillDetails().get(i).getProductName() +
                                    preBillList.getPreBillDetails().get(i).getProductSubGroupName() +
                                    preBillList.getPreBillDetails().get(i).getDefaultSizeValue());

                            order1.setPrice(String.valueOf(preBillList.getPreBillDetails().get(i).getPrice()));
                            order1.setCount(preBillList.getPreBillDetails().get(i).getQuantity());

                            order1.setDeliverPlace(db.getDeliveryPointById(
                                    preBillList.getPreBillDetails().get(i).getDeliveryPointId())
                                    .getDeliveryPointName());

                            order1.setUnit(db.getMeasurementById(
                                    preBillList.getPreBillDetails().get(i).getMeasurementUnitId()).getMeasurementUnitId());

                            db.closeDB();
                            orders.add(order1);
                        }

                        //-----------------------------calculate total price---------------------------------
                        int bill = 0;
                        int rand = preBillList.getPreBillDetails().size();
                        for (int i = 0; i < rand; i++) {
                            bill = preBillList.getPreBillDetails().get(i).getPrice() *
                                    preBillList.getPreBillDetails().get(i).getQuantity() + bill;
                        }
                        double tax = preBillList.getTax();
                        double totalPrice = bill + tax * bill;

                        billText.setText("" + bill);
                        taxText.setText("" + tax);
                        totelPriceText.setText("" + totalPrice);

                        preBillAdapter.notifyDataSetChanged();
                    }
                }
                if (intent.getAction().equals(PRE_BILL_HAS_CHANGED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        hasChanged = bundle.getBoolean("change");
                    }
                }
                if (intent.getAction().equals(CONFIRM_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY)) {
                    FinalBillFragment fragment = FinalBillFragment.newInstance();
                    fragment.setArguments(args);

                    String fragmentTag = fragment.getClass().getName();
                    boolean fragmentPopped = ((FragmentActivity) getContext()).getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

                    if (!fragmentPopped) { //fragment not in back stack, create it.
                        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        ft.replace(containerId, fragment);
                        ft.addToBackStack(fragmentTag);
                        ft.commit();
                    }
                }
                if (intent.getAction().equals(CANCEL_PRE_BILL_HAS_BEEN_SENT_SUCCESSFULLY)) {
                    getActivity().onBackPressed();
                }
                if (intent.getAction().equals(PRE_BILL_LIST_DID_NOT_RECEIVE)) {
                    Toast.makeText(getContext(), getString(R.string.error_pre_bill), Toast.LENGTH_LONG).show();
                    showProgress(false);
                    mImageRefresh.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
