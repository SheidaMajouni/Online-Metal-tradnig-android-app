package com.damavandit.apps.chair;

import android.annotation.SuppressLint;
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
import android.support.constraint.ConstraintLayout;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.activities.ShoppingCartActivity;
import com.damavandit.apps.chair.activities.SignInActivity;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.DeliveryPoint;
import com.damavandit.apps.chair.dbModels.MeasurementUnit;
import com.damavandit.apps.chair.dbModels.OrderStatus;
import com.damavandit.apps.chair.dbModels.ProductGroupMeasurementUnit;
import com.damavandit.apps.chair.dbModels.ProductGroupMeasurementUnit2;
import com.damavandit.apps.chair.fragments.AboutUsFragment;
import com.damavandit.apps.chair.fragments.BottomSheetFragment;
import com.damavandit.apps.chair.fragments.ChatFragment;
import com.damavandit.apps.chair.fragments.ContactFragment;
import com.damavandit.apps.chair.fragments.RegisterFragment;
import com.damavandit.apps.chair.fragments.ShoppingCartFragment;
import com.damavandit.apps.chair.fragments.StoreFragment;
import com.damavandit.apps.chair.fragments.WindowFragment;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.other.Setting;
import com.damavandit.apps.chair.services.AppMainService;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.damavandit.apps.chair.constants.Const.action.DELIVERY_POINT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.MEASUREMENT_UNIT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_DECREASED;
import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_INCREASED;
import static com.damavandit.apps.chair.constants.Const.action.ORDER_STATUS_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED;
import static com.damavandit.apps.chair.constants.Const.action.USER_INFO_STATE_DID_NOT_RECEIVE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServiceConnection {

    private AppMainService mMainService;
    private MainActivityReceiver mNotificationCountReceiver;

    private ArrayList<MeasurementUnit> measurementUnitList;
    private ArrayList<OrderStatus> orderStatusList;
    private ArrayList<DeliveryPoint> deliveryPointList;
    private ArrayList<ProductGroupMeasurementUnit2> productGroupManufacturerModelList;

    private Fragment selectedFragment;
    private StoreFragment storeFragment;
    private ChatFragment chatFragment;
    private ContactFragment contactFragment;
    private AboutUsFragment aboutUsFragment;
    private WindowFragment windowFragment;

    private BottomNavigationView mBottomNav;
    private ImageView mImageCart;
    private ImageView mImageCamera;
    private ImageView mImageNavigationDrawer;
    private TextView mTextTitle;
    private TextView mTextNotificationCount;
    private TextView mTextUserPhone;

    private Session session;
    private Setting setting;
    private ConstraintLayout constraintLayout;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = new Setting(MainActivity.this);

        setting.SetLocale("fa");
        setContentView(R.layout.activity_main);

        startService(new Intent(this, AppMainService.class));
        mNotificationCountReceiver = new MainActivityReceiver();

        db = new DatabaseHelper(this);

        session = new Session(this);
        constraintLayout = findViewById(R.id.main_layout);
        if (!session.loggedin()) {
            session.setLoggedin(false);
            finish();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
        }

        mTextNotificationCount = findViewById(R.id.notification_count_text);
        if (session.getNotifCount() == 0) {
            mTextNotificationCount.setVisibility(View.INVISIBLE);
        } else {
            mTextNotificationCount.setVisibility(View.VISIBLE);
            mTextNotificationCount.setText(String.valueOf(session.getNotifCount()));
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "Font/Sans.ttf");
        mTextTitle = findViewById(R.id.text_title_action_bar);
        mTextTitle.setTypeface(face);

        mImageCart = findViewById(R.id.image_cart);
        mImageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBasketActivity = new Intent(MainActivity.this, ShoppingCartActivity.class);
                startActivity(goBasketActivity);
            }
        });

        mImageCamera = findViewById(R.id.image_camera);
        mImageCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        mTextUserPhone = headerView.findViewById(R.id.user_number);
        mTextUserPhone.setText(" : " + session.getUserName());

        mImageNavigationDrawer = findViewById(R.id.image_nav_drawer);
        mImageNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(Gravity.START)) {
                    drawer.openDrawer(Gravity.START);
                } else {
                    drawer.closeDrawer(Gravity.START);
                }
            }
        });


        mBottomNav = findViewById(R.id.navigationView);
        BottomNavigationViewHelper.removeShiftMode(mBottomNav);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        if (chatFragment == null) {
                            chatFragment = ChatFragment.newInstance();
                        }
                        selectedFragment = chatFragment;
                        break;
                    case R.id.navigation_contact:
                        if (contactFragment == null) {
                            contactFragment = ContactFragment.newInstance();
                        }
                        selectedFragment = contactFragment;
                        break;
                    case R.id.navigation_bazaar:
                        if (storeFragment == null) {
                            storeFragment = StoreFragment.newInstance();
                        }
                        selectedFragment = storeFragment;
                        break;
                    case R.id.navigation_vitrin:
                        if (windowFragment == null) {
                            windowFragment = WindowFragment.newInstance();
                        }
                        selectedFragment = windowFragment;
                        break;
                    case R.id.navigation_more:
                        if (aboutUsFragment == null) {
                            aboutUsFragment = AboutUsFragment.newInstance();
                        }
                        selectedFragment = aboutUsFragment;
                        break;
                }

                String fragmentTag = selectedFragment.getClass().getName();
                boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, selectedFragment);
                    ft.addToBackStack(fragmentTag);
                    ft.commit();
                }
                return true;
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, StoreFragment.newInstance());
        mBottomNav.getMenu().findItem(R.id.navigation_bazaar).setChecked(true);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            StoreFragment fragment = StoreFragment.newInstance();

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }

        } else if (id == R.id.nav_cart) {

            ShoppingCartFragment fragment = ShoppingCartFragment.newInstance();
            Bundle arg = new Bundle();
            arg.putString("drawer", "drawer");
            fragment.setArguments(arg);

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }

        } else if (id == R.id.register_list) {

            RegisterFragment fragment = RegisterFragment.newInstance();
            Bundle arg = new Bundle();
            arg.putString("drawer", "drawer");
            fragment.setArguments(arg);

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }

        } else if (id == R.id.nav_log_out) {

            session.deleteAllSharedPreferences();
            session.setLoggedin(false);
            Snackbar snackbar = Snackbar
                    .make(constraintLayout, getString(R.string.exit), Snackbar.LENGTH_LONG);
            snackbar.show();
            startActivity(new Intent(MainActivity.this, SignInActivity.class));

        } else if (id == R.id.nav_contacts) {

            ContactFragment fragment = ContactFragment.newInstance();

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }

        } else if (id == R.id.nav_about) {

            AboutUsFragment fragment = AboutUsFragment.newInstance();

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }

        } else if (id == R.id.nav_special){
            WindowFragment fragment = WindowFragment.newInstance();

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }
        } else if (id == R.id.nav_call){
            ChatFragment fragment = ChatFragment.newInstance();

            String fragmentTag = fragment.getClass().getName();
            boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(fragmentTag);
                ft.commit();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);

                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                if (mMainService != null) {
                    mMainService.getAllConstantTables();
                    mMainService.getUserInfoState();
                }
            } else {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.check_net), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mNotificationCountReceiver);
        unbindService(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_COUNT_INCREASED);
        filter.addAction(NOTIFICATION_COUNT_DECREASED);
        filter.addAction(MEASUREMENT_UNIT_LIST_RECEIVED);
        filter.addAction(ORDER_STATUS_LIST_RECEIVED);
        filter.addAction(DELIVERY_POINT_LIST_RECEIVED);
        filter.addAction(PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED);
        filter.addAction(USER_INFO_STATE_DID_NOT_RECEIVE);

        registerReceiver(mNotificationCountReceiver, filter);

        if (session.getNotifCount() == 0) {
            mTextNotificationCount.setVisibility(View.INVISIBLE);
        } else {
            mTextNotificationCount.setVisibility(View.VISIBLE);
        }
        mTextNotificationCount.setText(String.valueOf(session.getNotifCount()));

        Intent service = new Intent(this, AppMainService.class);
        bindService(service, this, BIND_AUTO_CREATE);
    }

    public class MainActivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(NOTIFICATION_COUNT_INCREASED) || intent.getAction().equals(NOTIFICATION_COUNT_DECREASED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        if (bundle.getInt("notificationCount") == 0) {
                            mTextNotificationCount.setVisibility(View.INVISIBLE);
                        } else {
                            mTextNotificationCount.setVisibility(View.VISIBLE);
                        }
                        mTextNotificationCount.setText(String.valueOf(bundle.getInt("notificationCount")));
                    }
                } else if (intent.getAction().equals(MEASUREMENT_UNIT_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        measurementUnitList = bundle.getParcelableArrayList(
                                "measurementUnitList");
                        if (!(db.getMeasurementUnitCount() > 0)) {
                            for (MeasurementUnit measurementUnit : measurementUnitList) {
                                db.insertMeasurementUnit(measurementUnit);
                            }
                        }
                        db.closeDB();
                    }
                } else if (intent.getAction().equals(ORDER_STATUS_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        orderStatusList = bundle.getParcelableArrayList(
                                "orderStatusList");
                        if (!(db.getOrderStatusCount() > 0)) {
                            for (OrderStatus orderStatus : orderStatusList) {
                                db.insertOrderStatus(orderStatus);
                            }
                        }
                        db.closeDB();
                    }
                } else if (intent.getAction().equals(DELIVERY_POINT_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        deliveryPointList = bundle.getParcelableArrayList(
                                "deliveryPointList");
                        if (!(db.getDeliveryPointCount() > 0)) {
                            for (DeliveryPoint deliveryPoint : deliveryPointList) {
                                db.insertDeliveryPoint(deliveryPoint);
                            }
                        }
                        db.closeDB();
                    }
                } else if (intent.getAction().equals(PRODUCT_GROUP_MEASUREMENT_UNIT_LIST_RECEIVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        productGroupManufacturerModelList = bundle.getParcelableArrayList(
                                "productGroupMeasurementUnitList");

                        if (!(db.getProductGroupManufacturerUnitCount() > 0)) {
                            for (ProductGroupMeasurementUnit2 PGMU2 : productGroupManufacturerModelList) {
                                for (int i = 0; i < PGMU2.getMeasurementUnitId().size(); i++) {
                                    ProductGroupMeasurementUnit groupMeasurementUnit = new ProductGroupMeasurementUnit();
                                    groupMeasurementUnit.setProductGroupId(PGMU2.getProductGroupId());
                                    groupMeasurementUnit.setMeasurementGroupId(PGMU2.getMeasurementUnitId().get(i).getMeasurementUnitId());
                                    db.insertProductGroupManufacturerUnit(groupMeasurementUnit);
                                }
                            }
                            db.closeDB();
                        }
                    }
                } else if (intent.getAction().equals(USER_INFO_STATE_DID_NOT_RECEIVE)){
                    mMainService.getUserInfoState();
                }
            }
        }
    }

}
