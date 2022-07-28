package com.damavandit.apps.chair.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.OrderRecycleAdapter;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.DailyProductSubGroup;
import com.damavandit.apps.chair.dbModels.MeasurementUnit;
import com.damavandit.apps.chair.dbModels.ShoppingCartModel;
import com.damavandit.apps.chair.models.OrderModel;
import com.damavandit.apps.chair.other.Session;

import java.util.ArrayList;
import java.util.List;

import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_INCREASED;

public class OrderFragment extends Fragment {
    private ArrayList<OrderModel> orders;
    private RecyclerView orderRecycle;
    private Button submitOrder;
    private ImageView factoryImage;
    private TextView title;
    private Session session;
    private DatabaseHelper db;

    public static OrderFragment newInstance() {
        OrderFragment fragment = new OrderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new Session(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
        submitOrder = view.findViewById(R.id.button_submit_cart);
        submitOrder.setTypeface(face);
        title = view.findViewById(R.id.registered_text);
        title.setTypeface(face);
        orderRecycle = view.findViewById(R.id.product_recycle_cart);
        factoryImage = view.findViewById(R.id.factory_image_cart);
        orders = new ArrayList<OrderModel>();

//----------------------get argument from product adapter------------------------------
        final Bundle bundle = getArguments();


//-------------------------------real get arguments------------------------------------
        int alaki;
        DailyProductSubGroup dailyProductSubGroup = bundle.getParcelable("orderedProduct");
        final String explain = bundle.getString("productGroupName") + dailyProductSubGroup.getProductSubGroupName() + dailyProductSubGroup.getSize();
        Glide.with(getContext())
                .load(bundle.getString("manufacturerLogo"))
                .into(factoryImage);
        final String price = String.valueOf(dailyProductSubGroup.getPrice());
        final String deliverPlace = dailyProductSubGroup.getDeliveryPointName();
        final int dailyProductPriceListId = dailyProductSubGroup.getDailyProductPriceListId();

        db = new DatabaseHelper(getContext());
        int jjj = bundle.getInt("productGroupId");
        List<Integer> measurementIdList = db.getMeasurementGroupIdsByProductGroupId(bundle.getInt("productGroupId"));
        List<MeasurementUnit> measurementUnitList = new ArrayList<>();

        for (int i = 0; i < measurementIdList.size(); i++) {
            MeasurementUnit measurementUnit = new MeasurementUnit();
            measurementUnit.setMeasurementUnitId(measurementIdList.get(i));
            measurementIdList.get(i);
            measurementUnit.setMeasurementUnitName(db.getMeasurementById(measurementIdList.get(i)).getMeasurementUnitName());
            measurementUnitList.add(measurementUnit);
        }
        db.closeDB();

        final OrderModel order = new OrderModel();
        order.setExplain(explain);
        order.setPrice(price);
        order.setDeliverPlace(deliverPlace);
        orders.add(order);

        OrderRecycleAdapter orderAdapter = new OrderRecycleAdapter(getContext(), orders, measurementUnitList);
        orderRecycle.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));
        orderRecycle.setAdapter(orderAdapter);

//---------------make iranian date of submit order to send it for register fragment----
        final PersianDate pDate = new PersianDate();
        PersianDateFormat pFormat = new PersianDateFormat("Y/m/d");
        final String s = pFormat.format(pDate);


//---------------put argument for register adapter by submit button---------------------

        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (orders.get(0).getCount() == 0) {
                    Toast.makeText(getContext(), getString(R.string.enter_number), Toast.LENGTH_SHORT).show();
                } else {

                    int c = session.getNotifCount();
                    c++;
                    session.setNotificationCount(c);
                    Intent intent = new Intent();
                    intent.putExtra("notificationCount", session.getNotifCount());
                    intent.setAction(NOTIFICATION_COUNT_INCREASED);
                    getActivity().sendBroadcast(intent);

                    ShoppingCartModel basket = new ShoppingCartModel();
                    basket.setExplain(explain);
                    basket.setPrice(Integer.valueOf(price));
                    basket.setCount(orders.get(0).getCount());
                    basket.setUnitId(orders.get(0).getUnit());
                    basket.setBasketId(dailyProductPriceListId);
                    basket.setManufacturerLogo(bundle.getString("manufacturerLogo"));
                    basket.setFactoryName(bundle.getString("factoryName"));
                    basket.setDeliveryPoint(deliverPlace);

                    session.addInBasket(basket);
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.has_been_added_to_basket), Toast.LENGTH_SHORT).show();

                    getActivity().onBackPressed();
                }
            }
        });
    }
}
