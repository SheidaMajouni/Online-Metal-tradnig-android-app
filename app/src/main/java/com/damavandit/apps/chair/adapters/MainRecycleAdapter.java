package com.damavandit.apps.chair.adapters;

import android.content.Context;
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
import com.damavandit.apps.chair.models.SectionItems;

import java.util.ArrayList;

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.MainRecycleHolder> {

    private ArrayList<SectionItems> mItems;
    private Context mContext;

    public MainRecycleAdapter(Context context, ArrayList<SectionItems> items) {
        this.mContext = context;
        this.mItems = items;
    }

    @NonNull
    @Override
    public MainRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.main_recycle_row, parent, false);
        return new MainRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecycleHolder holder, final int position) {
        holder.textTitle.setText(mItems.get(position).getHeadTitle());

        final InnerRecycleAdapter innerAdapter = new InnerRecycleAdapter(mContext,
                mItems.get(position).getItemsInSection(), mItems.get(position).getHeadTitle());
        holder.innerRecycle.setHasFixedSize(true);
        holder.innerRecycle.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.innerRecycle.setAdapter(innerAdapter);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class MainRecycleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AdapterView.OnItemClickListener itemClickListener;
        public RecyclerView innerRecycle;
        public TextView textTitle;
        public ConstraintLayout layout;

        public MainRecycleHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            textTitle = itemView.findViewById(R.id.text_title);
            innerRecycle = itemView.findViewById(R.id.inner_recycle);
            layout = itemView.findViewById(R.id.outer_recycle_layout);
        }

        @Override
        public void onClick(View v) {
            v.setOnClickListener(this);
        }
    }
}

