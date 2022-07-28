package com.damavandit.apps.chair.models;

public class MessageDetail {

    private int messageDetailId;
    private int messageId;
    private String contentQuestion;
    private String contentAnswer;
    private int questionNumber;
    private int answerNumber;

    public int getMessageDetailId() {
        return messageDetailId;
    }

    public void setMessageDetailId(int messageDetailId) {
        this.messageDetailId = messageDetailId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getContentQuestion() {
        return contentQuestion;
    }

    public void setContentQuestion(String contentQuestion) {
        this.contentQuestion = contentQuestion;
    }

    public String getContentAnswer() {
        return contentAnswer;
    }

    public void setContentAnswer(String contentAnswer) {
        this.contentAnswer = contentAnswer;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }
}
