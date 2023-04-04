package com.quochungcyou.proconnect.Model;

public class UserModel {
    String name;
    String lastMessage;
    String profileImageUrl;
    String useruid;
    boolean isOnline = false;


    public UserModel(String name, String lastMessage, String profileImageUrl, String useruid) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.profileImageUrl = profileImageUrl;
        this.useruid = useruid;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
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
