package com.damavandit.apps.chair.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.MeasurementUnit;
import com.damavandit.apps.chair.models.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderRecycleAdapter extends RecyclerView.Adapter<OrderRecycleAdapter.OrderRecycleHolder> {

    private ArrayList<OrderModel> orders;
    private Context mContext;
    private String count;
    private ArrayList<MeasurementUnit> measurementUnitList;

    public OrderRecycleAdapter(Context context, ArrayList<OrderModel> orders, List<MeasurementUnit> list) {
        this.orders = orders;
        this.mContext = context;
        this.measurementUnitList = (ArrayList<MeasurementUnit>) list;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @NonNull
    @Override
    public OrderRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parent.getWindowToken(), 0);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_row_cart, parent, false);
        return new OrderRecycleAdapter.OrderRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final OrderRecycleHolder holder, final int position) {

        final int holderPosition = position;
        holder.explain.setText(orders.get(position).getExplain());
        holder.price.setText(" به نرخ " + orders.get(position).getPrice() + " تومان ");
        holder.delivery.setText(" تحویل در " + orders.get(position).getDeliverPlace());
        holder.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!holder.count.getText().toString().equals("")) {
                    orders.get(holderPosition).setCount(Integer.parseInt(holder.count.getText().toString()));
                } else {
                    orders.get(holderPosition).setCount(0);
                }
            }
        });

        ArrayList<String> measurementUnitNameList = new ArrayList<>();
        for (int i = 0; i < measurementUnitList.size(); i++) {
            measurementUnitNameList.add(measurementUnitList.get(i).getMeasurementUnitName());
        }
        SimpleSpinnerAdapter spinnerAdapter = new SimpleSpinnerAdapter(
                mContext,
                measurementUnitNameList);
        holder.unitSpinner.setAdapter(spinnerAdapter);
        holder.unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orders.get(holderPosition).setUnit(measurementUnitList.get(position).getMeasurementUnitId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderRecycleHolder extends RecyclerView.ViewHolder {

        public TextView explain;
        public TextView price;
        public EditText count;
        public TextView delivery;
        public TextView howMuch;
        public Spinner unitSpinner;

        public OrderRecycleHolder(View itemView) {
            super(itemView);
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "Font/Sans.ttf");
            explain = itemView.findViewById(R.id.table_row_explain_cart);
            explain.setTypeface(face);
            price = itemView.findViewById(R.id.table_row_price_cart);
            price.setTypeface(face);
            count = itemView.findViewById(R.id.table_row_count_cart);
            delivery = itemView.findViewById(R.id.delivery_text_order);
            delivery.setTypeface(face);
            unitSpinner = itemView.findViewById(R.id.unit_spinner);
            howMuch = itemView.findViewById(R.id.how_much_text);
            howMuch.setTypeface(face);
        }
    }
}

