package com.vendorpro.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TokenManager {

    private static final String TAG = "TOKEN_MANAGER";

    private static final String PREF_NAME = "VendorProPrefs";

    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_MESS_ID = "mess_id";   // 🔥 NEW

    private static TokenManager instance;
    private final SharedPreferences sharedPreferences;

    private TokenManager(Context context) {
        sharedPreferences =
                context.getApplicationContext()
                        .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized TokenManager getInstance(Context context) {
        if (instance == null) {
            instance = new TokenManager(context);
        }
        return instance;
    }

    /* ================= ACCESS TOKEN ================= */

    public void saveToken(String token) {

        Log.d(TAG, "Saving JWT Token");

        sharedPreferences.edit()
                .putString(KEY_JWT_TOKEN, token)
                .apply();
    }

    public String getToken() {

        String token = sharedPreferences.getString(KEY_JWT_TOKEN, null);

        Log.d(TAG, "Fetched JWT Token: " + token);

        return token;
    }

    /* ================= REFRESH TOKEN ================= */

    public void saveRefreshToken(String refreshToken) {

        Log.d(TAG, "Saving Refresh Token");

        sharedPreferences.edit()
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public String getRefreshToken() {

        String refreshToken = sharedPreferences.getString(KEY_REFRESH_TOKEN, null);

        Log.d(TAG, "Fetched Refresh Token: " + refreshToken);

        return refreshToken;
    }

    /* ================= USER INFO ================= */

    public void saveUserId(String userId) {

        Log.d(TAG, "Saving User ID: " + userId);

        sharedPreferences.edit()
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public String getUserId() {

        String userId = sharedPreferences.getString(KEY_USER_ID, null);

        Log.d(TAG, "Fetched User ID: " + userId);

        return userId;
    }

    /* ================= MESS INFO ================= */

    public void saveMessId(String messId) {

        Log.d(TAG, "Saving Mess ID: " + messId);

        sharedPreferences.edit()
                .putString(KEY_MESS_ID, messId)
                .apply();
    }

    public String getMessId() {

        String messId = sharedPreferences.getString(KEY_MESS_ID, null);

        Log.d(TAG, "Fetched Mess ID: " + messId);

        return messId;
    }

    /* ================= LOGOUT / CLEAR ================= */

    public void clearAll() {

        Log.d(TAG, "Clearing all stored tokens");

        sharedPreferences.edit().clear().apply();
    }

    // Optional (old compatibility)
    public void clearToken() {

        Log.d(TAG, "Clearing JWT Token");

        sharedPreferences.edit().remove(KEY_JWT_TOKEN).apply();
    }
}