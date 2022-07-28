package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.damavandit.apps.chair.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context mContect;
    private List<String> list;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<String> list) {
        this.mContect = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return getCount() > 0 ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_spinner_item, null);
        TextView products = convertView.findViewById(R.id.text1);
        products.setText(list.get(position));
        return convertView;
    }
}

