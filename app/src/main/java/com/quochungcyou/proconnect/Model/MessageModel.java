package com.quochungcyou.proconnect.Model;

import java.io.Serializable;

public class MessageModel implements Serializable {
    Long time;
    String message;
    String sender;
    String receiver;
    String senderavatar, receiveravatar, sendername, targetname;

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public MessageModel(Long time, String message, String sender, String receiver, String senderavatar, String receiveravatar, String sendername, String targetname) {
        this.time = time;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.senderavatar = senderavatar;
        this.receiveravatar = receiveravatar;
        this.sendername = sendername;
        this.targetname = targetname;
    }

    public String getTargetname() {
        return targetname;
    }

    public void setTargetname(String targetname) {
        this.targetname = targetname;
    }



    public String getSenderavatar() {
        return senderavatar;
    }

    public void setSenderavatar(String senderavatar) {
        this.senderavatar = senderavatar;
    }

    public String getReceiveravatar() {
        return receiveravatar;
    }

    public void setReceiveravatar(String receiveravatar) {
        this.receiveravatar = receiveravatar;
    }

    public MessageModel() {

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
