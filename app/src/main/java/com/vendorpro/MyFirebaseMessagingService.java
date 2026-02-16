package com.vendorpro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.vendorpro.model.FcmTokenRequest;
import com.vendorpro.network.AuthService;
import com.vendorpro.network.RetrofitClient;
import com.vendorpro.network.TokenManager;
import com.vendorpro.ui.OrdersActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            sendNotification(
                    remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody()
            );
        } else if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            sendNotification(title, body);
        }
    }

    @Override
    public void onNewToken(String fcmToken) {
        Log.d(TAG, "Refreshed FCM token: " + fcmToken);
        sendRegistrationToServer(fcmToken);
    }

    // ================= SEND FCM TOKEN TO BACKEND =================
    private void sendRegistrationToServer(String fcmToken) {

        String jwtToken = TokenManager
                .getInstance(this)
                .getToken();

        if (jwtToken == null) {
            Log.w(TAG, "JWT token not found, skipping FCM update");
            return;
        }

        AuthService authService =
                RetrofitClient.getClient(this)
                        .create(AuthService.class);

        authService.updateFcmToken(
                "Bearer " + jwtToken,
                new FcmTokenRequest(fcmToken)
        ).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "FCM token updated successfully");
                } else {
                    Log.e(TAG, "Failed to update FCM token. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error updating FCM token", t);
            }
        });
    }

    // ================= SHOW NOTIFICATION =================
    private void sendNotification(String title, String messageBody) {

        Intent intent = new Intent(this, OrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "OrderCallbacks";
        Uri defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(
                            channelId,
                            "Orders",
                            NotificationManager.IMPORTANCE_DEFAULT
                    );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
