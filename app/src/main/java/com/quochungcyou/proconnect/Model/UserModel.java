package com.quochungcyou.proconnect.Model;

public class UserModel {
    String name;
    String lastMessage;
    String profileImageUrl;
    boolean isOnline = false;

    public UserModel(String name, String lastMessage, String profileImageUrl, boolean isOnline) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
        this.isOnline = isOnline;
    }
    public UserModel(String name, String lastMessage, String profileImageUrl) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
