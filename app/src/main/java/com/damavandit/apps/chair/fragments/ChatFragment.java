package com.damavandit.apps.chair.fragments;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.activities.CreateMessageActivity;
import com.damavandit.apps.chair.activities.SentActivity;
import com.damavandit.apps.chair.database.DatabaseHelper;
import com.damavandit.apps.chair.dbModels.MessageModel;
import com.damavandit.apps.chair.other.Session;
import com.damavandit.apps.chair.services.AppMainService;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.damavandit.apps.chair.constants.Const.action.MESSAGE_SENT_SUCCESSFULLY;

public class ChatFragment extends Fragment implements ServiceConnection {

    private Boolean isFabOpen = false;
    private FloatingActionButton fab, mFabCreateMessage, fab2;
    private TextView text1, text2, mTextReceiveNothing;
    private boolean empty = true;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private View rootView;

    private ConstraintLayout constraintLayout;

    private TextInputEditText mTextTitle;
    private TextInputEditText mTextBody;
    private Button mButtonSend;

    private DatabaseHelper db;
    private Session session;
    private MessageModel messageRepliedTo;
    private boolean replied = false;
    private int id;

    private AppMainService mMainService;
    private SendMessageBroadcastReceiver mBroadcastReceiver;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().startService(new Intent(getContext(), AppMainService.class));
        mBroadcastReceiver = new SendMessageBroadcastReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = getActivity().getWindow().getDecorView().getRootView();

        mTextTitle = view.findViewById(R.id.title_create_message);
        mTextBody = view.findViewById(R.id.body_create_message);
        mButtonSend = view.findViewById(R.id.send_create_message);

        db = new DatabaseHelper(getContext());
        session = new Session(getContext());

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.getExtras() != null && intent.getStringExtra("fromDetail").equals("fromDetail")) {
                messageRepliedTo = intent.getParcelableExtra("messageReplyTo");
                mTextTitle.setText(messageRepliedTo.getmMessageTitle());
                mTextTitle.setEnabled(false);
                replied = true;
            }
        }

        mButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextTitle.getText().toString().equals("") || mTextBody.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getResources().getString(R.string.empty_message), Toast.LENGTH_SHORT).show();
                } else {
                    MessageModel message = new MessageModel();

                    if (!replied) {
                        message.setmMessageTitle(mTextTitle.getText().toString());
                        message.setmMessageText(mTextBody.getText().toString());
                        message.setmUserId(session.getUserId());
                    } else {
                        message.setmMessageTitle(mTextTitle.getText().toString());
                        message.setmMessageText(mTextBody.getText().toString());
                        message.setmUserId(session.getUserId());
                        message.setmInResponseTo(messageRepliedTo.getmMessageId());
                    }

                    if (mMainService != null) {
                        mMainService.sendMessage(message);
                    }
                }
            }
        });

        fab = view.findViewById(R.id.fab);
        mFabCreateMessage = view.findViewById(R.id.fab_create_message);
        fab2 = view.findViewById(R.id.fab_search);
        text1 = view.findViewById(R.id.text_fab1);
        text2 = view.findViewById(R.id.text_fab2);
        constraintLayout = view.findViewById(R.id.icons_container);
        fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabController();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SentActivity.class));
            }
        });

        mFabCreateMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CreateMessageActivity.class));
            }
        });

        Typeface face = Typeface.createFromAsset(getContext().getAssets(), "Font/Sans.ttf");
        setFont(view, face);
    }

    private void fabController() {
        if (isFabOpen) {

            fab.setImageResource(R.mipmap.ic_more);
            fab.startAnimation(rotate_backward);
            mFabCreateMessage.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            text1.startAnimation(fab_close);
            text2.startAnimation(fab_close);
            mFabCreateMessage.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
            constraintLayout.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.white));

        } else {

            fab.setImageResource(R.mipmap.ic_add);
            fab.startAnimation(rotate_forward);
            mFabCreateMessage.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            text1.startAnimation(fab_open);
            text2.startAnimation(fab_open);
            mFabCreateMessage.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
            constraintLayout.setBackgroundColor(getActivity().getApplicationContext().getResources().getColor(R.color.half_black));
        }
    }

    private void setFont(View view, Typeface face) {
        TextView happy_call = view.findViewById(R.id.happy_call);
        happy_call.setTypeface(face);
        TextView be_call = view.findViewById(R.id.be_call);
        be_call.setTypeface(face);
        TextView text_call = view.findViewById(R.id.text_call);
        text_call.setTypeface(face);
        TextView text_phone = view.findViewById(R.id.text_phone);
        text_phone.setTypeface(face);
        TextView text_email = view.findViewById(R.id.text_email);
        text_email.setTypeface(face);
        TextView email_address = view.findViewById(R.id.email_address);
        email_address.setTypeface(face);
        TextView text_address = view.findViewById(R.id.text_address);
        text_address.setTypeface(face);
        TextView address = view.findViewById(R.id.address);
        address.setTypeface(face);
        text1.setTypeface(face);
        text2.setTypeface(face);
        mTextTitle.setTypeface(face);
        mTextBody.setTypeface(face);
        mButtonSend.setTypeface(face);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unbindService(this);
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        Intent service = new Intent(getContext(), AppMainService.class);
        getActivity().bindService(service, this, BIND_AUTO_CREATE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(MESSAGE_SENT_SUCCESSFULLY);
        getActivity().registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        if (service != null) {
            AppMainService.MyBinder binder = (AppMainService.MyBinder) service;
            mMainService = binder.getService();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

        if (mMainService != null) {
            mMainService = null;
        }
    }

    public class SendMessageBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
                if (intent.getAction().equals(MESSAGE_SENT_SUCCESSFULLY)) {
                    Toast.makeText(getContext(), "پیام ارسال شد", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                } else {
                    Toast.makeText(getContext(), "پیام ارسال نشد", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}