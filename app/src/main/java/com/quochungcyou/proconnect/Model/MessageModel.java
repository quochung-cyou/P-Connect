package com.quochungcyou.proconnect.Model;

public class MessageModel {
    Long time;
    String message;
    String sender;
    String receiver;

    public MessageModel(long time, String message, String sender, String receiver) {
        this.time = time;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
