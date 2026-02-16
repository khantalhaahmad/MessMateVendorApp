package com.vendorpro.model;

public class LoginResponse {

    private boolean success;
    private String message;

    private String token;
    private String refreshToken;

    private User user;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    // 🔥 REQUIRED FOR REFRESH TOKEN FLOW
    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUser() {
        return user;
    }
}
