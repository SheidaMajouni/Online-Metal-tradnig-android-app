package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damavandit.apps.chair.R;

import java.util.ArrayList;

public class SizeRecycleAdapter extends RecyclerView.Adapter<SizeRecycleAdapter.SizeRecycleHolder> {

    private ArrayList<String> sizes;
    private Context mContext;

    public SizeRecycleAdapter(Context mContext, ArrayList<String> sizes) {
        this.sizes = sizes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SizeRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_size, parent, false);

        return new SizeRecycleAdapter.SizeRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeRecycleHolder holder, int position) {
        holder.length.setText(sizes.get(position));
    }

    @Override
    public int getItemCount() {
        return sizes.size();
    }

    public class SizeRecycleHolder extends RecyclerView.ViewHolder {

        public TextView length;

        public SizeRecycleHolder(View itemView) {
            super(itemView);
            length = itemView.findViewById(R.id.length_size);
        }
    }
}

