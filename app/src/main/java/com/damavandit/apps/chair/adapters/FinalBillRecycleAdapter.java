package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.models.OrderModel;

import java.util.ArrayList;

public class FinalBillRecycleAdapter extends RecyclerView.Adapter<FinalBillRecycleAdapter.FinalBillHolder> {

    private ArrayList<OrderModel> orders;
    private Context mContext;

    public FinalBillRecycleAdapter(Context mContext, ArrayList<OrderModel> orders) {
        this.orders = orders;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public FinalBillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_row_final_bill, parent, false);
        return new FinalBillRecycleAdapter.FinalBillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalBillHolder holder, int position) {
        holder.row.setText(orders.get(position).getRow());
        holder.explain.setText(orders.get(position).getExplain());
        holder.price.setText(orders.get(position).getPrice());
        holder.deliver.setText(orders.get(position).getDeliverPlace());
        holder.count.setText(String.valueOf(orders.get(position).getCount()));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class FinalBillHolder extends RecyclerView.ViewHolder {

        public TextView count;
        public TextView row;
        public TextView explain;
        public TextView price;
        public TextView deliver;

        public FinalBillHolder(View itemView) {
            super(itemView);
            count = itemView.findViewById(R.id.count_final_bill_text);
            row = itemView.findViewById(R.id.row_final_bill);
            explain = itemView.findViewById(R.id.explain_final_bill_text);
            price = itemView.findViewById(R.id.price_final_bill_text);
            deliver = itemView.findViewById(R.id.deliver_final_bill_text);
        }
    }
}

