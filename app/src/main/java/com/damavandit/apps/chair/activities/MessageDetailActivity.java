package com.damavandit.apps.chair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.MessageDetailAdapter;
import com.damavandit.apps.chair.dbModels.MessageModel;

import java.util.ArrayList;

public class MessageDetailActivity extends AppCompatActivity {

    private RecyclerView mRecycleMessageDetail;
    private MessageModel message;
    private ArrayList<MessageModel> messageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        Intent intent = getIntent();
        if (intent != null) {
            message = intent.getParcelableExtra("messageList");
        }

        mRecycleMessageDetail = findViewById(R.id.recycle_message_detail);
        messageDetails = new ArrayList<>();
        messageDetails.add(message);
        MessageDetailAdapter messageDetailAdapter = new MessageDetailAdapter(this, messageDetails);
        mRecycleMessageDetail.setLayoutManager(new LinearLayoutManager
                (this, LinearLayoutManager.VERTICAL, false));
        mRecycleMessageDetail.setAdapter(messageDetailAdapter);
    }
}

