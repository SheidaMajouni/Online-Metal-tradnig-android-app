package com.damavandit.apps.chair.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.adapters.ChatRecycleAdapter;
import com.damavandit.apps.chair.dbModels.MessageModel;
import com.damavandit.apps.chair.interfaces.OnLoadMoreMessage;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import java.util.ArrayList;

import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_LIST_DID_NOT_RECIEVE;
import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_LIST_RECEIVED;

public class SentActivity extends AppCompatActivity implements ServiceConnection {

    ArrayList<MessageModel> messageList, messageListTemp;
    private AppMainService mMainService;
    private MessageSentListBroadcastReceiver messageReceiver;
    private Session session;

    private ChatRecycleAdapter chatRecycleAdapter;
    private TextView mTextSendNothing;
    private View mProgressView;
    private ImageView mImageRefresh;
    private int pageNumber = 1;
    private int loadItemIndex;

    private RecyclerView sentRecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);

        startService(new Intent(this, AppMainService.class));
        messageReceiver = new MessageSentListBroadcastReceiver();

        session = new Session(this);

        messageList = new ArrayList<>();
        messageListTemp = new ArrayList<>();

        sentRecycle = findViewById(R.id.recycle_sent_messages);
        mTextSendNothing = findViewById(R.id.send_nothing);

        sentRecycle.setLayoutManager(new LinearLayoutManager
                (SentActivity.this, LinearLayoutManager.VERTICAL, false));
        chatRecycleAdapter = new ChatRecycleAdapter(SentActivity.this, messageList, sentRecycle);
        sentRecycle.setAdapter(chatRecycleAdapter);
        chatRecycleAdapter.setOnLoadMoreMessage(new OnLoadMoreMessage() {
            @Override
            public void onLoadMore() {
                if (mMainService != null) {
                    messageList.add(null);
                    loadItemIndex = messageList.size() - 1;
                    chatRecycleAdapter.notifyItemInserted(loadItemIndex);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            messageList.remove(loadItemIndex);
                            chatRecycleAdapter.notifyItemRemoved(loadItemIndex);
                            pageNumber++;
                            mMainService.getMessages(session.getUserId(), pageNumber);
                            chatRecycleAdapter.notifyDataSetChanged();
                            chatRecycleAdapter.setLoading(false);
                        }
                    }, 3000);
                }
            }
        });

//      -------------------------------progress bar--------------------------
        mProgressView = findViewById(R.id.store_progress);
        mImageRefresh = findViewById(R.id.refresh);
        mImageRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(true);
                mImageRefresh.setVisibility(View.GONE);
                mMainService.getMessages(session.getUserId(), pageNumber);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        sentRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
        sentRecycle.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                sentRecycle.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindService(this);
        unregisterReceiver(messageReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent service = new Intent(SentActivity.this, AppMainService.class);
        bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_LIST_RECEIVED);
        filter.addAction(MESSAGE_LIST_DID_NOT_RECIEVE);
        registerReceiver(messageReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
            showProgress(true);
            mMainService.getMessages(session.getUserId(), pageNumber);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class MessageSentListBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(MESSAGE_LIST_RECEIVED)) {
                    showProgress(false);
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        messageListTemp = bundle.getParcelableArrayList(
                                "messageList");

                        for (int i = 0; i < messageListTemp.size(); i++) {
                            if (messageListTemp.get(i).getmSender() == 1) {
                                messageList.add(messageListTemp.get(i));
                            }
                        }
                        if (messageList.size() == 0) {
                            mTextSendNothing.setVisibility(View.VISIBLE);
                        } else {
                            chatRecycleAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (intent.getAction().equals(MESSAGE_LIST_DID_NOT_RECIEVE)) {
                    showProgress(false);
                    mImageRefresh.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}

