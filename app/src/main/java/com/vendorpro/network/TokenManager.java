package com.vendorpro.network;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenManager {

    private static final String PREF_NAME = "VendorProPrefs";

    private static final String KEY_JWT_TOKEN = "jwt_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";

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
        sharedPreferences.edit()
                .putString(KEY_JWT_TOKEN, token)
                .apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_JWT_TOKEN, null);
    }

    /* ================= REFRESH TOKEN (🔥 NEW) ================= */

    public void saveRefreshToken(String refreshToken) {
        sharedPreferences.edit()
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .apply();
    }

    public String getRefreshToken() {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null);
    }

    /* ================= USER INFO ================= */

    public void saveUserId(String userId) {
        sharedPreferences.edit()
                .putString(KEY_USER_ID, userId)
                .apply();
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    /* ================= LOGOUT / CLEAR ================= */

    public void clearAll() {
        sharedPreferences.edit().clear().apply();
    }

    // Backward compatibility (optional)
    public void clearToken() {
        sharedPreferences.edit().remove(KEY_JWT_TOKEN).apply();
    }
}
