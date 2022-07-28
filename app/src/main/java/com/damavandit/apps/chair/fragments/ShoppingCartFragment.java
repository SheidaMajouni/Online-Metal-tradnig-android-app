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
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.activities.UserInfoActivity;
import com.damavandit.apps.chair.adapters.ShoppingCartAdapter;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.Order;
import com.damavandit.apps.chair.dbModels.OrderDetail;
import com.damavandit.apps.chair.dbModels.ShoppingCartModel;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import saman.zamani.persiandate.PersianDate;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_DECREASED;
import static com.damavandit.apps.chair.constants.Const.action.ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY;
import static com.damavandit.apps.chair.constants.Const.action.REGISTERE_FOR_SHOPPING;

public class ShoppingCartFragment extends Fragment implements ServiceConnection {

    private ArrayList<ShoppingCartModel> mBasketList;
    private Order sendBasketContents;
    private RecyclerView mRecycle;
    private Button mSubmitButton;
    private TextView mTextEmptyCart;
    private TextView mTextBasket;
    private Session session;
    private Bundle args;
    private AppMainService mMainService;
    private SendBasketBroadcastReceiver mBroadcastReceiver;
    private DatabaseHelper db;
    private boolean hasSomething = false;

    private View mProgressView;

    public static ShoppingCartFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
        db = new DatabaseHelper(getContext());
        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mBroadcastReceiver = new SendBasketBroadcastReceiver();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_cart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgressView = view.findViewById(R.id.shopping_progress);

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");

        mRecycle = view.findViewById(R.id.basket_recycle);
        mSubmitButton = view.findViewById(R.id.submit_basket);
        mSubmitButton.setTypeface(face);
        mTextEmptyCart = view.findViewById(R.id.empty_shopping_cart_text);
        mTextEmptyCart.setTypeface(face);
        mTextBasket = view.findViewById(R.id.text_basket);
        mTextBasket.setTypeface(face);
        mBasketList = new ArrayList<>();

        if (session.getModelList() != null && session.getModelList().size() == 0) {
            mTextEmptyCart.setVisibility(View.VISIBLE);
            mSubmitButton.setText(getResources().getString(R.string.exit_pure));
            hasSomething = false;
//            mSubmitButton.setEnabled(false);
        } else {
            mBasketList = session.getModelList();
            if (mBasketList != null && mBasketList.size() != 0) {
                mTextEmptyCart.setVisibility(View.INVISIBLE);
                mSubmitButton.setEnabled(true);
                hasSomething = true;
                final ShoppingCartAdapter shoppingCartAdapter = new ShoppingCartAdapter(getContext(), mBasketList);
                mRecycle.setLayoutManager(new LinearLayoutManager
                        (getContext(), LinearLayoutManager.VERTICAL, false));
                mRecycle.setAdapter(shoppingCartAdapter);
            } else {
                mTextEmptyCart.setVisibility(View.VISIBLE);
                mSubmitButton.setText(getResources().getString(R.string.exit_pure));
                mSubmitButton.setEnabled(true);
                hasSomething = false;
            }
        }
        if (session.getModelList() == null) {
            mTextEmptyCart.setVisibility(View.VISIBLE);
            mSubmitButton.setText(getResources().getString(R.string.exit_pure));
            mSubmitButton.setEnabled(true);
            hasSomething = false;
        }

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!hasSomething) {
                    getActivity().onBackPressed();
                } else {
                    //------------------------------making persian date---------------------------------
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time =&gt; " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String gregorianDate = df.format(c.getTime());

                    //persian date
                    final PersianDate pDate = new PersianDate();
                    int year = Integer.valueOf(gregorianDate.substring(0, 4));
                    int month = Integer.valueOf(gregorianDate.substring(5, 7));
                    int day = Integer.valueOf(gregorianDate.substring(8, 10));

                    int[] persianDate = pDate.toJalali(year, month, day);
                    String showDate = (persianDate[2] + "/" + persianDate[1] + "/" + persianDate[0]);

//--------------------------milady date---------------------------
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    String date = sdf.format(new Date());

                    sendBasketContents = new Order();
                    ArrayList<OrderDetail> orderDetails = new ArrayList<>();
                    for (int i = 0; i < mBasketList.size(); i++) {
                        OrderDetail basketContent = new OrderDetail();
                        basketContent.setDailyProductPriceListId(mBasketList.get(i).getBasketId());
                        basketContent.setQuantity(mBasketList.get(i).getCount());
                        basketContent.setMeasurementUnitId(mBasketList.get(i).getUnitId());
                        orderDetails.add(basketContent);
                    }
                    sendBasketContents.setDate(date);
                    sendBasketContents.setUserId(session.getUserId());
                    sendBasketContents.setOrderStatusId(1);
                    sendBasketContents.setOrderDetails(orderDetails);

                    args = new Bundle();
                    args.putString("drawer", "shopping");
                    db.closeDB();

                    if (mMainService != null && sendBasketContents != null) {
                        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            if (mMainService != null) {
                                if (session.getUserInfoComplete()) {
                                    mMainService.sendOrderedBasket(sendBasketContents);
                                    showProgress(true);
                                } else {
                                    startActivity(new Intent(getContext(), UserInfoActivity.class));
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
        filter.addAction(ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY);
        filter.addAction(NOTIFICATION_COUNT_DECREASED);
        getActivity().registerReceiver(mBroadcastReceiver, filter);
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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        mRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public class SendBasketBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(ORDERED_BASKET_HAS_BEEN_SENT_SUCCESSFULLY)) {
                    showProgress(false);
                    session.clearShoppingCart();
                    RegisterFragment fragment = RegisterFragment.newInstance();
                    fragment.setArguments(args);

                    String fragmentTag = fragment.getClass().getName();
                    boolean fragmentPopped = ((FragmentActivity) getContext()).getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

                    if (!fragmentPopped) { //fragment not in back stack, create it.
                        FragmentTransaction ft = ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.basket_container, fragment);
                        ft.addToBackStack(fragmentTag);
                        ft.commit();
                    }

                } else if (intent.getAction().equals(NOTIFICATION_COUNT_DECREASED)) {
                    if (session.getModelList().size() == 0) {
                        mTextEmptyCart.setVisibility(View.VISIBLE);
                        mSubmitButton.setEnabled(false);
                    }
                } else if (intent.getAction().equals(REGISTERE_FOR_SHOPPING)) {
                    if (mMainService != null && sendBasketContents != null) {
                        mMainService.sendOrderedBasket(sendBasketContents);
                    }
                } else {
                    showProgress(false);
                    Toast.makeText(getContext(), getResources().getString(R.string.reject_shopping_cart), Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
