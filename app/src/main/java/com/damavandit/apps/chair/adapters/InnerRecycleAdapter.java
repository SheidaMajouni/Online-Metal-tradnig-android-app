package com.damavandit.apps.chair.adapters;

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

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.fragments.ProductFragment;
import com.damavandit.apps.chair.models.Items;

import java.util.ArrayList;

public class InnerRecycleAdapter extends RecyclerView.Adapter<InnerRecycleAdapter.InnerRecycleHolder> {

    private ArrayList<Items> items;
    private Context mContext;
    private int mPosition;
    private String group;

    public void setmPosition(int position) {
        this.mPosition = position;
    }

    public InnerRecycleAdapter(Context context, ArrayList<Items> items, String group) {
        this.items = items;
        this.mContext = context;
        this.group = group;
    }

    @NonNull
    @Override
    public InnerRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inner_recycle_row, parent, false);
        return new InnerRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerRecycleHolder holder, final int position) {
        holder.textId.setText(items.get(position).getTitle());
        holder.mImageView.setImageResource(items.get(position).getImage());
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setmPosition(position);

                Bundle args = new Bundle();
                args.putString("group", group);
                args.putString("factory", items.get(position).getTitle());

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
        return items.size();
    }

    public class InnerRecycleHolder extends RecyclerView.ViewHolder {

        public TextView textId;
        public ImageView mImageView;

        public InnerRecycleHolder(View itemView) {
            super(itemView);
            textId = itemView.findViewById(R.id.text_recycle);
            mImageView = itemView.findViewById(R.id.image_recycle);
        }
    }
}

