package com.miniproject.pickupalarm.Models;

public class TokenModel {
    private String fcmToken;

    public TokenModel() {
    }

    public TokenModel(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
