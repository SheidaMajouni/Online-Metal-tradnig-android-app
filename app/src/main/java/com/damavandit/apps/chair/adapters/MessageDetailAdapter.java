package com.damavandit.apps.chair.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damavandit.apps.chair.R;
import com.damavandit.apps.chair.dbModels.MessageModel;

import java.util.ArrayList;

import saman.zamani.persiandate.PersianDate;

public class MessageDetailAdapter extends RecyclerView.Adapter<MessageDetailAdapter.MessageDetailRecycleHolder> {

    private ArrayList<MessageModel> mMessageDetails;
    private Context mContext;

    public MessageDetailAdapter(Context context, ArrayList<MessageModel> messageDetails) {
        mContext = context;
        mMessageDetails = messageDetails;
    }

    @NonNull
    @Override
    public MessageDetailRecycleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_message_detail, parent, false);
        return new MessageDetailAdapter.MessageDetailRecycleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageDetailRecycleHolder holder, int position) {
        holder.questionText.setText(mMessageDetails.get(position).getmMessageTitle());
        holder.answerText.setText(mMessageDetails.get(position).getmMessageText());

        //persian date
        String gregorianDate = mMessageDetails.get(position).getmSentDate().substring(0, 10);
        final PersianDate pDate = new PersianDate();
        int year = Integer.valueOf(gregorianDate.substring(0, 4));
        int month = Integer.valueOf(gregorianDate.substring(5, 7));
        int day = Integer.valueOf(gregorianDate.substring(8, 10));

        int[] persianDate = pDate.toJalali(year, month, day);
        final String showDate = (persianDate[0] + "/" + persianDate[1] + "/" + persianDate[2]);

        holder.date.setText(showDate);
    }

    @Override
    public int getItemCount() {
        return mMessageDetails.size();
    }

    public class MessageDetailRecycleHolder extends RecyclerView.ViewHolder {

        public TextView questionText;
        public TextView answerText;
        public TextView date;

        public MessageDetailRecycleHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.question_text);
            answerText = itemView.findViewById(R.id.answer_text);
            date = itemView.findViewById(R.id.text_date);
        }
    }
}

