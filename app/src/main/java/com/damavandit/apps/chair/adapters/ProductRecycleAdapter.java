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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dialog.CustomDialogSize;
import com.damavandit.apps.chair.fragments.OrderFragment;
import com.damavandit.apps.chair.models.ProductTable;

import java.util.ArrayList;

public class ProductRecycleAdapter extends RecyclerView.Adapter<ProductRecycleAdapter.ProductRecycleHolder> {

    private ArrayList<ProductTable> products;
    private Context mContext;
    private String factoryName;
    private String groupName;

    public ProductRecycleAdapter(Context mContext, ArrayList<ProductTable> products
            , String factoryName
            , String groupName) {
        this.products = products;
        this.mContext = mContext;
        this.factoryName = factoryName;
        this.groupName = groupName;
    }

    @NonNull
    @Override
    public ProductRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_product, parent, false);
        return new ProductRecycleAdapter.ProductRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductRecycleHolder holder, int position) {
        holder.size.setText("14");
        holder.deliveryPoint.setText(products.get(position).getDeliverPlace());
        holder.price.setText(products.get(position).getPrice());

        final Bundle args = new Bundle();
        args.putString("deliverPlace", products.get(position).getDeliverPlace());
        args.putString("price", products.get(position).getPrice());
        args.putString("factoryName", factoryName);
        args.putString("groupName", groupName);
        args.putString("size", products.get(position).getSize());
        args.putString("subGroup", products.get(position).getSubGroup());

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
                        new CustomDialogSize(mContext);
                customDialogSize.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductRecycleHolder extends RecyclerView.ViewHolder {
        public TextView size;
        public TextView deliveryPoint;
        public TextView price;
        public Button commit;
        public ImageView imageProduct;

        public ProductRecycleHolder(View itemView) {
            super(itemView);
            size = itemView.findViewById(R.id.table_product_size);
            deliveryPoint = itemView.findViewById(R.id.table_delivery_product);
            price = itemView.findViewById(R.id.table_product_price);
            commit = itemView.findViewById(R.id.button_order_product);
            imageProduct = itemView.findViewById(R.id.image_product);
        }
    }
}

