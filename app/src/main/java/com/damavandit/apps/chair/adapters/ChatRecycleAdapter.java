package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.activities.MessageDetailActivity;
import com.damavandit.apps.chair.dbModels.MessageModel;
import com.damavandit.apps.chair.interfaces.OnLoadMoreMessage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import saman.zamani.persiandate.PersianDate;

public class ChatRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MessageModel> mMessageList;
    private Context mContext;
    private OnLoadMoreMessage onLoadMoreMessage;
    private boolean isLoading = false;

    public ChatRecycleAdapter(Context mContext, ArrayList<MessageModel> messages, RecyclerView recyclerView) {
        this.mContext = mContext;
        this.mMessageList = messages;
        final LinearLayoutManager linearLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int total = linearLayout.getItemCount();
                int lastVisible = linearLayout.findLastVisibleItemPosition();
                if (lastVisible == total - 1 && isLoading == false) {
                    if (onLoadMoreMessage != null) {
                        onLoadMoreMessage.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setOnLoadMoreMessage(OnLoadMoreMessage onLoadMoreMessage) {
        this.onLoadMoreMessage = onLoadMoreMessage;
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessageList.get(position) != null)
            return 1;
        else
            return 2;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_chat, parent, false);
            return new ChatRecycleHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_loading, parent, false);
            return new MessageLoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ChatRecycleHolder) {
            ChatRecycleHolder chatRecycleHolder = (ChatRecycleHolder) holder;
            chatRecycleHolder.nameChatText.setText(mMessageList.get(position).getmMessageTitle());
            chatRecycleHolder.contentChatText.setText(mMessageList.get(position).getmMessageText());

            //persian date
            String gregorianDate = mMessageList.get(position).getmSentDate().substring(0, 10);
            final PersianDate pDate = new PersianDate();
            int year = Integer.valueOf(gregorianDate.substring(0, 4));
            int month = Integer.valueOf(gregorianDate.substring(5, 7));
            int day = Integer.valueOf(gregorianDate.substring(8, 10));

            int[] persianDate = pDate.toJalali(year, month, day);
            final String showDate = (persianDate[0] + "/" + persianDate[1] + "/" + persianDate[2]);

            chatRecycleHolder.dateMessage.setText(showDate);
            chatRecycleHolder.imageChat.setImageResource(R.drawable.ic_launcher_background);
            chatRecycleHolder.chatLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MessageDetailActivity.class);
                    intent.putExtra("messageList", mMessageList.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else if (holder instanceof MessageLoadingViewHolder) {
            MessageLoadingViewHolder loadingHolder = (MessageLoadingViewHolder) holder;
            loadingHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class ChatRecycleHolder extends RecyclerView.ViewHolder {

        public CircleImageView imageChat;
        public TextView nameChatText;
        public TextView contentChatText;
        public TextView dateMessage;
        public ConstraintLayout chatLayout;

        public ChatRecycleHolder(View itemView) {
            super(itemView);
            imageChat = itemView.findViewById(R.id.chat_image);
            nameChatText = itemView.findViewById(R.id.name_chat_text);
            contentChatText = itemView.findViewById(R.id.content_chat_text);
            dateMessage = itemView.findViewById(R.id.chat_date);
            chatLayout = itemView.findViewById(R.id.chat_layout);
        }
    }

    public class MessageLoadingViewHolder extends RecyclerView.ViewHolder {

        ContentLoadingProgressBar progressBar;

        public MessageLoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar_message);
        }
    }
}

