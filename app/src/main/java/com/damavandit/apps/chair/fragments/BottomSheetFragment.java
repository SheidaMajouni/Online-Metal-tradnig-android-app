package com.damavandit.apps.chair.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.activities.CameraActivity;
import com.damavandit.apps.chair.activities.UploadFileActivity;
import com.damavandit.apps.chair.activities.UploadVoiceActivity;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    private LinearLayout mLinearSendPic;
    private LinearLayout mLinearSendVoice;
    private LinearLayout mLinearSendFile;
    private LinearLayout mLinearSendTable;

    private TextView mTextPic;
    private TextView mTextVoice;
    private TextView mTextFile;
    private TextView mTextTable;
    private TextView mTextQuickOrder;

    private Context mContext;

    public BottomSheetFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextPic = view.findViewById(R.id.text_pic);
        mTextVoice = view.findViewById(R.id.text_voice);
        mTextFile = view.findViewById(R.id.text_file);
        mTextTable = view.findViewById(R.id.text_table);
        mTextQuickOrder = view.findViewById(R.id.text_quick_order);

        Typeface face = Typeface.createFromAsset(mContext.getAssets(), "Font/Sans.ttf");

        mTextPic.setTypeface(face);
        mTextVoice.setTypeface(face);
        mTextFile.setTypeface(face);
        mTextTable.setTypeface(face);
        mTextQuickOrder.setTypeface(face);

        mLinearSendPic = view.findViewById(R.id.linear_camera);
        mLinearSendVoice = view.findViewById(R.id.linear_voice);
        mLinearSendFile = view.findViewById(R.id.linear_file);
        mLinearSendTable = view.findViewById(R.id.linear_quick_table);

        mLinearSendPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CameraActivity.class));
            }
        });

        mLinearSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadVoiceActivity.class));
            }
        });

        mLinearSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadFileActivity.class));
            }
        });

        mLinearSendTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "send table", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
