package com.damavandit.apps.chair.newAdapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.Manufacturer;
import com.damavandit.apps.chair.fragments.ProductFragment;

import java.util.ArrayList;

public class InnerRecycleAdapter2 extends RecyclerView.Adapter<InnerRecycleAdapter2.InnerRecycleHolder2> {

    private ArrayList<Manufacturer> mManufacturers;
    private Context mContext;
    private int mProductGroupId;
    private String mProductGroupName;

    public InnerRecycleAdapter2(Context mContext,ArrayList<Manufacturer> manufacturers, int productGroupId , String productGroupName) {
        this.mManufacturers = manufacturers;
        this.mContext = mContext;
        this.mProductGroupId = productGroupId;
        this.mProductGroupName = productGroupName;
    }

    @NonNull
    @Override
    public InnerRecycleHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_recycle_row, parent, false);
        return new InnerRecycleAdapter2.InnerRecycleHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRecycleHolder2 holder, final int position) {
        holder.textId.setText(mManufacturers.get(position).getManufacturerName());

        Glide.with(mContext)
                .load((mManufacturers.get(position).getManufacturerLogo()).replace("\\","/"))
                .into(holder.mImageView);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putInt("productGroupId", mProductGroupId);
                args.putInt("manufacturerId", mManufacturers.get(position).getManufacturerId());
                args.putString("manufacturerLogo",(mManufacturers.get(position).getManufacturerLogo()).replace("\\","/"));
                args.putString("productGroupName",mProductGroupName);
                args.putString("manufacturerName",mManufacturers.get(position).getManufacturerName());

                ProductFragment fragment = ProductFragment.newInstance();
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
    }

    @Override
    public int getItemCount() {
        return mManufacturers.size();
    }

    public class InnerRecycleHolder2 extends RecyclerView.ViewHolder {

        public TextView textId;
        public ImageView mImageView;

        public InnerRecycleHolder2(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.text_recycle);
            mImageView = itemView.findViewById(R.id.image_recycle);
        }
    }
}