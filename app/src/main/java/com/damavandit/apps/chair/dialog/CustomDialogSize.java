package com.damavandit.apps.chair.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.SizeRecycleAdapter;

import java.util.ArrayList;

public class CustomDialogSize extends Dialog {

    private Context mContext;
    private RecyclerView sizeRecycle;
    private Button acceptSize;
    private ArrayList<String> sizeList;
    private double size;
    private double thickness,length,width,height;
    private String alloy;

    public CustomDialogSize(@NonNull Context context,
                            double length,
                            double width,
                            double height,
                            double thickness,
                            double size,
                            String alloy) {
        super(context);
        this.mContext = context;
        this.length = length;
        this.width = width;
        this.height = height;
        this.thickness = thickness;
        this.size = size;
        this.alloy = alloy;
    }

    public CustomDialogSize(@NonNull Context context) {
        super(context);
        this.mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_size);
        Drawable drawable = mContext.getDrawable(R.drawable.oval_gray);
        getWindow().setBackgroundDrawable(drawable);

        sizeRecycle = findViewById(R.id.size_recycle);
        acceptSize = findViewById(R.id.accept_size_unit);

        sizeList = new ArrayList<>();

        sizeList.add(String.valueOf(length));
        sizeList.add(String.valueOf(width));
        sizeList.add(String.valueOf(height));
        sizeList.add(String.valueOf(thickness));
        sizeList.add(String.valueOf(size));
        sizeList.add(alloy);

        SizeRecycleAdapter sizeAdapter = new SizeRecycleAdapter(getContext(), sizeList);
        sizeRecycle.setLayoutManager(new LinearLayoutManager
                (getContext(), LinearLayoutManager.VERTICAL, false));
        sizeRecycle.setAdapter(sizeAdapter);

        acceptSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}