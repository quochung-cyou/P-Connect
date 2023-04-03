package com.quochungcyou.proconnect.Model;

public class NotificationModel {
    String useruid;
    String fullname;
    String avatarurl;

    public NotificationModel(String useruid, String fullname, String avatarurl) {
        this.useruid = useruid;
        this.fullname = fullname;
        this.avatarurl = avatarurl;
    }

    public String getAvatarurl() {
        return avatarurl;
    }

    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
