package com.example.umaknexus;

public class WishlistItems {
    String notifTitle;
    String notifDesc;

    public WishlistItems(String notifTitle, String notifDesc) {
        this.notifTitle = notifTitle;
        this.notifDesc = notifDesc;
    }

    public String getNotifTitle() {
        return notifTitle;
    }

    public void setNotifTitle(String notifTitle) {
        this.notifTitle = notifTitle;
    }

    public String getNotifDesc() {
        return notifDesc;
    }

    public void setNotifDesc(String notifDesc) {
        this.notifDesc = notifDesc;
    }
}
