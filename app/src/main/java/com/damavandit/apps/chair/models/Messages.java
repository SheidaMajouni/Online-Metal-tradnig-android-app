package com.damavandit.apps.chair.models;

public class Messages {

    private int messageId;
    private String messageTitle;
    private int messageStatusId;
    private String messageStatus;
    private String messageDate;

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public int getMessageStatusId() {
        return messageStatusId;
    }

    public void setMessageStatusId(int messageStatusId) {
        this.messageStatusId = messageStatusId;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }
}
