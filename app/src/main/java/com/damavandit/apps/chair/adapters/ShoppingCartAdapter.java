package com.damavandit.apps.chair.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.ShoppingCartModel;
import com.damavandit.apps.chair.other.Session;

import java.util.ArrayList;

import static com.damavandit.apps.chair.constants.Const.action.NOTIFICATION_COUNT_DECREASED;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.BasketRecycleHolder> {

    private ArrayList<ShoppingCartModel> basketList;
    private Context mContext;
    private Session session;
    private DatabaseHelper db;

    public ShoppingCartAdapter(Context mContext, ArrayList<ShoppingCartModel> orders) {
        this.basketList = orders;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BasketRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        session = new Session(mContext);
        db = new DatabaseHelper(mContext);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_recycle_basket, parent, false);
        return new ShoppingCartAdapter.BasketRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketRecycleHolder holder, final int position) {

        holder.deliver.setText(" تحویل در " + basketList.get(position).getDeliveryPoint());
        holder.explain.setText(basketList.get(position).getExplain());
        holder.price.setText(" به نرخ " + String.valueOf(basketList.get(position).getPrice()) + " تومان ");
        holder.row.setText(String.valueOf(position + 1));
        holder.count.setText(String.valueOf(basketList.get(position).getCount()));
        holder.unit.setText(db.getMeasurementById(basketList.get(position).getUnitId()).getMeasurementUnitName());
        db.closeDB();

        holder.deleteBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder cancel = new AlertDialog.Builder(mContext);
                cancel.setMessage(mContext.getResources().getString(R.string.shopping_cart_alert_message));
                cancel.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                cancel.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basketList = session.delete(position);

                        int count = session.getNotifCount();
                        count--;
                        session.setNotificationCount(count);
                        notifyDataSetChanged();
                        Intent intent = new Intent();
                        intent.putExtra("notificationCount", session.getNotifCount());
                        intent.setAction(NOTIFICATION_COUNT_DECREASED);
                        mContext.sendBroadcast(intent);
                    }
                });
                cancel.create();
                cancel.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public class BasketRecycleHolder extends RecyclerView.ViewHolder {

        public TextView explain;
        public TextView price;
        public EditText count;
        public TextView deliver;
        public TextView unit;
        //        public ImageView factoryImage;
        public TextView countBasket;
        public TextView row;
        public ImageView deleteBasket;

        public BasketRecycleHolder(View itemView) {
            super(itemView);
            Typeface face = Typeface.createFromAsset(mContext.getAssets(), "Font/Sans.ttf");
            explain = itemView.findViewById(R.id.table_row_explain_basket);
            explain.setTypeface(face);
            price = itemView.findViewById(R.id.table_row_price_basket);
            price.setTypeface(face);
            count = itemView.findViewById(R.id.table_row_count_basket);
            deliver = itemView.findViewById(R.id.deliver_text_basket);
            deliver.setTypeface(face);
            unit = itemView.findViewById(R.id.table_row_unit_basket);
            unit.setTypeface(face);
            row = itemView.findViewById(R.id.basket_row);
            row.setTypeface(face);
            deleteBasket = itemView.findViewById(R.id.delete_basket);
            countBasket = itemView.findViewById(R.id.count_basket);
            countBasket.setTypeface(face);
        }
    }
}

