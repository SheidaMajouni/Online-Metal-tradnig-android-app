package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.models.OrderModel;

import java.util.ArrayList;

import static com.damavandit.apps.chair.constants.Const.action.PRE_BILL_HAS_CHANGED;

public class PreBillRecycleAdapter extends RecyclerView.Adapter<PreBillRecycleAdapter.PreBillHolder> {

    private ArrayList<OrderModel> orders;
    private Context mContext;
    public static boolean hasChenged;
    private DatabaseHelper db;

    public PreBillRecycleAdapter(Context mContext, ArrayList<OrderModel> orders, boolean hasChenged) {
        this.orders = orders;
        this.mContext = mContext;
        this.hasChenged = hasChenged;
    }

    @NonNull
    @Override
    public PreBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new DatabaseHelper(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_row_pre_bill, parent, false);
        return new PreBillRecycleAdapter.PreBillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PreBillHolder holder, int position) {

        final int holderPosition = position;
        holder.row.setText(orders.get(position).getRow());
        holder.explain.setText(orders.get(position).getExplain());
        holder.price.setText(orders.get(position).getPrice());
        holder.deliver.setText(orders.get(position).getDeliverPlace());

        int total = Integer.valueOf(orders.get(position).getPrice())
                * Integer.valueOf(orders.get(position).getCount());
        holder.totalPrice.setText(String.valueOf(total));

        holder.unit.setText(db.getMeasurementById(orders.get(position).getUnit()).getMeasurementUnitName());

        if (hasChenged) {
            holder.count.setTextColor(mContext.getResources().getColor(R.color.white_pressed));
            holder.count.setEnabled(true);
        } else {
            holder.count.setTextColor(mContext.getResources().getColor(R.color.icon));
            holder.count.setEnabled(false);
        }
        holder.count.setText(String.valueOf(orders.get(position).getCount()));
        holder.count.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                Intent intent = new Intent();
                intent.setAction(PRE_BILL_HAS_CHANGED);
                intent.putExtra("change", true);
                mContext.sendBroadcast(intent);

                if (!holder.count.getText().toString().equals("")) {
                    orders.get(holderPosition).setCount(Integer.parseInt(holder.count.getText().toString()));
                } else {
                    orders.get(holderPosition).setCount(0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class PreBillHolder extends RecyclerView.ViewHolder {

        public EditText count;
        public TextView row;
        public TextView explain;
        public TextView price;
        public TextView deliver;
        public TextView totalPrice;
        public TextView unit;

        public PreBillHolder(View itemView) {
            super(itemView);

            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "Font/Sans.ttf");
            count = itemView.findViewById(R.id.count_pre_bill_text);
            count.setTypeface(face);
            row = itemView.findViewById(R.id.row_pre_bill);
            row.setTypeface(face);
            explain = itemView.findViewById(R.id.explain_pre_bill_text);
            explain.setTypeface(face);
            price = itemView.findViewById(R.id.price_pre_bill_text);
            price.setTypeface(face);
            deliver = itemView.findViewById(R.id.deliver_pre_bill_text);
            deliver.setTypeface(face);
            totalPrice = itemView.findViewById(R.id.total_price_pre_bill_text);
            totalPrice.setTypeface(face);
            unit = itemView.findViewById(R.id.unit_pre_bill_text);
            unit.setTypeface(face);
        }
    }
}

