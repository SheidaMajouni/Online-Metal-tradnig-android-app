package com.damavandit.apps.chair.newAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.ProductGroupManufacturerModel2;

import java.util.ArrayList;

public class MainRecyclerAdapter2 extends RecyclerView.Adapter<MainRecyclerAdapter2.MainRecycleHolder2> {

    private ArrayList<ProductGroupManufacturerModel2> mProductGroupManufacturerModelList;
    private Context mContext;

    public MainRecyclerAdapter2(Context context, ArrayList<ProductGroupManufacturerModel2> items) {
        this.mContext = context;
        this.mProductGroupManufacturerModelList = items;
    }

    @NonNull
    @Override
    public MainRecycleHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.main_recycle_row, parent, false);
        return new MainRecyclerAdapter2.MainRecycleHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecycleHolder2 holder, int position) {
        holder.textTitle.setText(mProductGroupManufacturerModelList.get(position).getProductGroupName());

        InnerRecycleAdapter2 innerAdapter = new InnerRecycleAdapter2(mContext,
                mProductGroupManufacturerModelList.get(position).getManufacturers(),
                mProductGroupManufacturerModelList.get(position).getProductGroupId(),
                mProductGroupManufacturerModelList.get(position).getProductGroupName());

        holder.innerRecycle.setHasFixedSize(true);
        holder.innerRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.innerRecycle.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return mProductGroupManufacturerModelList.size();
    }

    public void setProductGroupManufacturerModelList(ArrayList<ProductGroupManufacturerModel2> mProductGroupManufacturerModelList) {
        this.mProductGroupManufacturerModelList = mProductGroupManufacturerModelList;
    }

    public class MainRecycleHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AdapterView.OnItemClickListener itemClickListener;
        public RecyclerView innerRecycle;
        public TextView textTitle;
        public ConstraintLayout layout;
        Typeface face= Typeface.createFromAsset(mContext.getAssets(),"Font/Sans.ttf");

        public MainRecycleHolder2(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            textTitle = itemView.findViewById(R.id.text_title);
            textTitle.setTypeface(face);
            innerRecycle = itemView.findViewById(R.id.inner_recycle);
            layout = itemView.findViewById(R.id.outer_recycle_layout);
        }

        @Override
        public void onClick(View v) {
            v.setOnClickListener(this);
        }
    }
}
