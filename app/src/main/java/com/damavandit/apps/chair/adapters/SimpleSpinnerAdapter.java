package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.damavandit.apps.chair.R;

import java.util.List;

public class SimpleSpinnerAdapter extends BaseAdapter {

    private Context mContect;
    private List<String> list;
    private LayoutInflater inflater;

    public SimpleSpinnerAdapter(Context mContect, List<String> list) {
        this.mContect = mContect;
        this.list = list;
        inflater = (LayoutInflater.from(mContect));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_simple_spinner_item, null);
        TextView products = convertView.findViewById(R.id.text_item_spinner);
        products.setText(list.get(position));
        return convertView;
    }
}

