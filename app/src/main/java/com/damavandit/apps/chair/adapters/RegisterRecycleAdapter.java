package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.fragments.PreBillFragment;
import com.damavandit.apps.chair.models.RegisterStatus;

import java.util.ArrayList;

import saman.zamani.persiandate.PersianDate;

public class RegisterRecycleAdapter extends RecyclerView.Adapter<RegisterRecycleAdapter.RegisterRecycleHolder> {

    private ArrayList<RegisterStatus> mRegisteredList;
    private Context mContext;
    private CountDownTimer mCountDownTimer;
    private ArrayList<String> mInfoList;
    private DatabaseHelper db;
    private String fromDrawer;

    public RegisterRecycleAdapter(Context mContext, ArrayList<RegisterStatus> registeredList, String fromDrawer) {
        this.mRegisteredList = registeredList;
        this.mContext = mContext;
        this.fromDrawer = fromDrawer;
    }

    @NonNull
    @Override
    public RegisterRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = new DatabaseHelper(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_row_register, parent, false);
        return new RegisterRecycleAdapter.RegisterRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RegisterRecycleHolder holder, final int position) {
        holder.rowRegister.setText(String.valueOf(position + 1));
        holder.dateRegister.setText(mRegisteredList.get(position).getTrackingCode());
        holder.statusRegisterButton.setText(db.getOrderStatusById(mRegisteredList.get(position).getStatusId()).getOrderStatusName());


        //persian date
        String gregorianDate = mRegisteredList.get(position).getDate().substring(0, 10);
        final PersianDate pDate = new PersianDate();
        int year = Integer.valueOf(gregorianDate.substring(0, 4));
        int month = Integer.valueOf(gregorianDate.substring(5, 7));
        int day = Integer.valueOf(gregorianDate.substring(8, 10));

        int[] persianDate = pDate.toJalali(year, month, day);
        final String showDate = (persianDate[0] + "/" + persianDate[1] + "/" + persianDate[2]);


        holder.minute.setText(showDate);
        if (mRegisteredList.get(position).getStatusId() == 3) {
            holder.statusRegisterButton.setEnabled(true);
            holder.statusRegisterButton.setBackground(mContext.getResources().getDrawable(R.drawable.submited));
        } else {
            holder.statusRegisterButton.setEnabled(false);
            if (mRegisteredList.get(position).getStatusId() == 1)
                holder.statusRegisterButton.setBackground(mContext.getResources().getDrawable(R.drawable.wait_yellow));
            else if (mRegisteredList.get(position).getStatusId() == 2)
                holder.statusRegisterButton.setBackground(mContext.getResources().getDrawable(R.drawable.wait_orange));
            else if (mRegisteredList.get(position).getStatusId() == 4)
                holder.statusRegisterButton.setBackground(mContext.getResources().getDrawable(R.drawable.not_submit));
        }
        holder.statusRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();

                args.putInt("orderId", mRegisteredList.get(position).getOrderId());
                args.putString("date", showDate);
                args.putString("code", mRegisteredList.get(position).getTrackingCode());
                int containerId;
                if (fromDrawer.equals("shopping")) {
                    containerId = R.id.basket_container;
                    args.putString("drawer", "shopping");
                } else {
                    containerId = R.id.container;
                    args.putString("drawer", "drawer");
                }

                PreBillFragment fragment = PreBillFragment.newInstance();
                fragment.setArguments(args);

                String fragmentTag = fragment.getClass().getName();
                boolean fragmentPopped = ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    FragmentTransaction ft = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    ft.replace(containerId, fragment);
                    ft.addToBackStack(fragmentTag);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRegisteredList.size();
    }

    public class RegisterRecycleHolder extends RecyclerView.ViewHolder {

        public TextView rowRegister;
        public TextView dateRegister;
        public TextView minute;
        public Button statusRegisterButton;
        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "Font/Sans.ttf");

        public RegisterRecycleHolder(View itemView) {
            super(itemView);
            rowRegister = itemView.findViewById(R.id.table_register_row);
            rowRegister.setTypeface(face);
            dateRegister = itemView.findViewById(R.id.table_date_register);
            dateRegister.setTypeface(face);
            statusRegisterButton = itemView.findViewById(R.id.button_status);
            statusRegisterButton.setTypeface(face);
            minute = itemView.findViewById(R.id.minute);
            minute.setTypeface(face);
        }
    }
}

