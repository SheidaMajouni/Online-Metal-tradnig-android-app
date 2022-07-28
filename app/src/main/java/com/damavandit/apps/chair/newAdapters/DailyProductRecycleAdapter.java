package com.damavandit.apps.chair.newAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.DailyProductSubGroup;
import com.damavandit.apps.chair.dialog.CustomDialogSize;
import com.damavandit.apps.chair.fragments.OrderFragment;

import java.util.ArrayList;

public class DailyProductRecycleAdapter extends RecyclerView.Adapter<DailyProductRecycleAdapter.DailyProductRecycleHolder> {

    private Context mContext;
    private ArrayList<DailyProductSubGroup> mDailyProductList;
    private String mManufacturerLogo;
    private String mProductGroupName;
    private int mProductGroupId;

    public DailyProductRecycleAdapter(Context mContext, ArrayList<DailyProductSubGroup> mDailyProductList, String manufacturerLogo,
                                      String productGroupName, int productGroupId) {
        this.mContext = mContext;
        this.mDailyProductList = mDailyProductList;
        this.mManufacturerLogo = manufacturerLogo;
        this.mProductGroupName = productGroupName;
        this.mProductGroupId = productGroupId;

    }

    @NonNull
    @Override
    public DailyProductRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_product, parent, false);
        return new DailyProductRecycleAdapter.DailyProductRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyProductRecycleHolder holder, int position) {
        DailyProductSubGroup dailyProductSubGroup = mDailyProductList.get(position);

        holder.size.setText(" سایز: "+String.valueOf(dailyProductSubGroup.getDefaultDimensionValue()));
        holder.deliveryPoint.setText(" محل تحویل: " + (dailyProductSubGroup.getDeliveryPointName() != null ? dailyProductSubGroup.getDeliveryPointName() : ""));
        holder.price.setText(String.valueOf(dailyProductSubGroup.getPrice())+" تومان ");
        Glide.with(mContext)
                .load(dailyProductSubGroup.getProductGroupImage().replace("\\","/"))
                .into(holder.imageProduct);


        final double length = dailyProductSubGroup.getLenght();
        final double width = dailyProductSubGroup.getWidth();
        final double height = dailyProductSubGroup.getHeight();
        final double thickness = dailyProductSubGroup.getThickness();
        final double size = dailyProductSubGroup.getSize();
        final String alloy = dailyProductSubGroup.getAlloy();


        final Bundle args = new Bundle();
        args.putParcelable("orderedProduct", dailyProductSubGroup);
        args.putString("manufacturerLogo", mManufacturerLogo);
        args.putString("productGroupName", mProductGroupName);
        args.putInt("productGroupId", mProductGroupId);

        holder.commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment fragment = OrderFragment.newInstance();
                fragment.setArguments(args);

                String fragmentTag = fragment.getClass().getName();
                boolean fragmentPopped = ((FragmentActivity) v.getContext()).getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);

                if (!fragmentPopped) { //fragment not in back stack, create it.
                    FragmentTransaction ft = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, fragment);
                    ft.addToBackStack(fragmentTag);
                    ft.commit();
                }
            }
        });

        holder.size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogSize customDialogSize =
                        new CustomDialogSize(mContext, length, width, height, thickness, size,alloy);
                customDialogSize.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDailyProductList.size();
    }

    public void setDailyProductList(ArrayList<DailyProductSubGroup> mDailyProductList) {
        this.mDailyProductList = mDailyProductList;
    }

    public class DailyProductRecycleHolder extends RecyclerView.ViewHolder {
        public TextView size;
        public TextView deliveryPoint;
        public TextView price;
        public TextView commit;
        public ImageView imageProduct;

        public DailyProductRecycleHolder(View itemView) {
            super(itemView);
            Typeface face= Typeface.createFromAsset(mContext.getAssets(),"Font/Sans.ttf");
            size = itemView.findViewById(R.id.table_product_size);
            size.setTypeface(face);
            deliveryPoint = itemView.findViewById(R.id.table_delivery_product);
            deliveryPoint.setTypeface(face);
            price = itemView.findViewById(R.id.table_product_price);
            price.setTypeface(face);
            commit = itemView.findViewById(R.id.button_order_product);
            commit.setTypeface(face);
            imageProduct = itemView.findViewById(R.id.image_product);
        }
    }
}
