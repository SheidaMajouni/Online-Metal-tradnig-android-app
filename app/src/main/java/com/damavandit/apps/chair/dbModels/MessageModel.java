package com.damavandit.apps.chair.dbModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageModel implements Parcelable {

    @SerializedName("MessageId")
    @Expose
    private String mMessageId;
    @SerializedName("UserId")
    @Expose
    private String mUserId;
    @SerializedName("MessageTitle")
    @Expose
    private String mMessageTitle;
    @SerializedName("MessageText")
    @Expose
    private String mMessageText;
    @SerializedName("Sender")
    @Expose
    private int mSender;
    @SerializedName("SentDate")
    @Expose
    private String mSentDate;
    @SerializedName("InResponseTo")
    @Expose
    private String mInResponseTo;

    public MessageModel(){

    }

    protected MessageModel(Parcel in) {
        mMessageId = in.readString();
        mUserId = in.readString();
        mMessageTitle = in.readString();
        mMessageText = in.readString();
        mSender = in.readInt();
        mSentDate = in.readString();
        mInResponseTo = in.readString();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel in) {
            return new MessageModel(in);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };

    public String getmMessageId() {
        return mMessageId;
    }

    public void setmMessageId(String mMessageId) {
        this.mMessageId = mMessageId;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmMessageTitle() {
        return mMessageTitle;
    }

    public void setmMessageTitle(String mMessageTitle) {
        this.mMessageTitle = mMessageTitle;
    }

    public String getmMessageText() {
        return mMessageText;
    }

    public void setmMessageText(String mMessageText) {
        this.mMessageText = mMessageText;
    }

    public int getmSender() {
        return mSender;
    }

    public void setmSender(int mSender) {
        this.mSender = mSender;
    }

    public String getmSentDate() {
        return mSentDate;
    }

    public void setmSentDate(String mSentDate) {
        this.mSentDate = mSentDate;
    }

    public String getmInResponseTo() {
        return mInResponseTo;
    }

    public void setmInResponseTo(String mInResponseTo) {
        this.mInResponseTo = mInResponseTo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMessageId);
        dest.writeString(mUserId);
        dest.writeString(mMessageTitle);
        dest.writeString(mMessageText);
        dest.writeInt(mSender);
        dest.writeString(mSentDate);
        dest.writeString(mInResponseTo);
    }
}
