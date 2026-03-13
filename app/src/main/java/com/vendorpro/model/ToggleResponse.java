package com.vendorpro.model;

import com.google.gson.annotations.SerializedName;

public class ToggleResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("isOpen")
    private boolean isOpen;

    @SerializedName("message")
    private String message;

    /* ======================
       GETTERS
    ====================== */

    public boolean isSuccess() {
        return success;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public String getMessage() {
        return message;
    }

}