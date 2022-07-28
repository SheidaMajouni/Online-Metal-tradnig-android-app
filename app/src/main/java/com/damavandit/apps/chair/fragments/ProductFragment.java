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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.SpinnerAdapter;
import com.damavandit.apps.chair.dbModels.DailyProductSubGroup;
import com.damavandit.apps.chair.models.ProductTable;
import com.damavandit.apps.chair.newAdapters.DailyProductRecycleAdapter;
import com.damavandit.apps.chair.services.AppMainService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.THERE_IS_NO_PRODUCT;

public class ProductFragment extends Fragment implements ServiceConnection {

    private ArrayList<ProductTable> products;
    private RecyclerView productRecycle;
    private TextView title;
    private TextView productName;
    private TextView size;
    private ImageView factoryImage;
    private Spinner spinner;
    private View mProgressView;
    private ImageView mImageRefresh;

    private ArrayList<DailyProductSubGroup> dailyProductSubGroupList;
    private ArrayList<DailyProductSubGroup> selectedDailyProductSubGroupList;
    private DailyProductRecycleAdapter dailyAdapter;
    private List<String> subgroupProductList;

    private AppMainService mMainService;
    private DailyProductBroadcastReceiver mDailyProductReceiver;

    public static ProductFragment newInstance() {
        ProductFragment fragment = new ProductFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mDailyProductReceiver = new DailyProductBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.text_register_list_title);
        productName = view.findViewById(R.id.product_name);
        productRecycle = view.findViewById(R.id.product_recycle);
        factoryImage = view.findViewById(R.id.factory_image);
        products = new ArrayList<ProductTable>();
        dailyProductSubGroupList = new ArrayList<>();
        selectedDailyProductSubGroupList = new ArrayList<>();

//      -------------------------------progress bar--------------------------
        mProgressView = view.findViewById(R.id.product_progress);
        mImageRefresh = view.findViewById(R.id.refresh_product);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mImageRefresh.setVisibility(View.GONE);
                Bundle bundle = getArguments();
                int productGroupId = bundle.getInt("productGroupId");
                int manufacturerId = bundle.getInt("manufacturerId");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
                String date = sdf.format(new Date());
                mMainService.getDailyProductSubGroupView(productGroupId, manufacturerId, date);
            }
        });

//----------------------set real title and image and size from server data-------------------------

        Bundle bundle = getArguments();
        String manufacturerLogo = bundle.getString("manufacturerLogo");
        String manufacturerName = bundle.getString("manufacturerName");
        String productGroupName = bundle.getString("productGroupName");
        int productGroupId = bundle.getInt("productGroupId");
        String tempStr = "کارخانه " + manufacturerName;
        productName.setText("گروه " + productGroupName);
        title.setText(tempStr);
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
        productName.setTypeface(face);
        title.setTypeface(face);

        Glide.with(getContext())
                .load(manufacturerLogo)
                .into(factoryImage);

// ---------------------spinner content---------------------------------------------------
//-----------------------set spinner ------------------------------------------------------
        spinner = view.findViewById(R.id.product_spinner);
        subgroupProductList = new ArrayList<>();
//----------------------make table content-------------------------
        dailyAdapter = new DailyProductRecycleAdapter(getContext(), selectedDailyProductSubGroupList, manufacturerLogo, productGroupName, productGroupId);
        productRecycle.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        productRecycle.setAdapter(dailyAdapter);

        final List<String> tempSubgroupList = subgroupProductList;
//----------------------------------real spinner------------------------------------------


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> finalSubgroupProductList = new ArrayList<>();
                finalSubgroupProductList = subgroupProductList;
                if (finalSubgroupProductList.size() > 0) {
                    String subGroupStr = finalSubgroupProductList.get(position);
                    selectedDailyProductSubGroupList.clear();
                    for (int i = 0; i < dailyProductSubGroupList.size(); i++) {
                        if (dailyProductSubGroupList.get(i).getProductSubGroupName().equals(subGroupStr)) {
                            selectedDailyProductSubGroupList.add(dailyProductSubGroupList.get(i));
                        }
                    }
                    dailyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        productRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        productRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                productRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
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
        getActivity().unregisterReceiver(mDailyProductReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED);
        filter.addAction(THERE_IS_NO_PRODUCT);
        filter.addAction(DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED);
        getActivity().registerReceiver(mDailyProductReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        Bundle bundle = getArguments();
        int productGroupId = bundle.getInt("productGroupId");
        int manufacturerId = bundle.getInt("manufacturerId");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        String date = sdf.format(new Date());

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//        String date = sdf.format(new Date(118, 9, 21));

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
            showProgress(true);
            mMainService.getDailyProductSubGroupView(productGroupId, manufacturerId, date);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    private List<String> segregateSpinnerContent(ArrayList<DailyProductSubGroup> dailyProductSubGroupList) {
        List<String> listSubProduct = new ArrayList<String>();
        int count = 0;
        for (int i = 0; i < dailyProductSubGroupList.size(); i++) {
            String subGroup = dailyProductSubGroupList.get(i).getProductSubGroupName();
            if (listSubProduct.size() == 0) {
                listSubProduct.add(subGroup);
            } else {
                for (int j = 0; j < listSubProduct.size(); j++) {
                    if (!listSubProduct.get(j).equals(subGroup)) {
                        count++;
                    }
                }
                if (count == listSubProduct.size()) {
                    listSubProduct.add(subGroup);
                }
            }
        }

        return listSubProduct;
    }

    public class DailyProductBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(DAILY_PRODUCT_SUBGROUP_LIST_RECEIVED)) {
                    showProgress(false);
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        dailyProductSubGroupList = bundle.getParcelableArrayList(
                                "dailyProductSubGroupList");

                        subgroupProductList = segregateSpinnerContent(dailyProductSubGroupList);

                        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                                getContext(),
                                subgroupProductList);
                        spinner.setAdapter(spinnerAdapter);

//                        dailyAdapter.setDailyProductList(dailyProductSubGroupList);
                        dailyAdapter.setDailyProductList(selectedDailyProductSubGroupList);
                        dailyAdapter.notifyDataSetChanged();
                    }
                } else if (intent.getAction().equals(DAILY_PRODUCT_SUBGROUP_LIST_DID_NOT_RECEIVED)) {
                    showProgress(false);
                    Toast.makeText(getContext(), getString(R.string.error_get_daily), Toast.LENGTH_LONG).show();
                    mImageRefresh.setVisibility(View.VISIBLE);
                } else if (intent.getAction().equals(THERE_IS_NO_PRODUCT)) {
                    showProgress(false);
                    Toast.makeText(getContext(), getResources().getString(R.string.there_is_no_product), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
