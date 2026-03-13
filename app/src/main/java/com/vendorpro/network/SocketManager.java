package com.vendorpro.network;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/* ============================================================
   SOCKET MANAGER (Singleton)
   Handles realtime order updates
============================================================ */

public class SocketManager {

    private static final String TAG = "SocketManager";

    /* ============================================================
       SERVER URL
       Emulator → 10.0.2.2
       Physical device → your local IP
       Production → your domain
    ============================================================ */

    private static final String SOCKET_URL = "http://10.0.2.2:4000";

    private static Socket socket;

    /* ============================================================
       GET SOCKET INSTANCE
    ============================================================ */

    public static synchronized Socket getSocket() {

        if (socket == null) {

            try {

                IO.Options options = new IO.Options();

                options.reconnection = true;
                options.reconnectionAttempts = Integer.MAX_VALUE;
                options.reconnectionDelay = 2000;
                options.timeout = 20000;
                options.forceNew = false;
                options.transports = new String[]{"websocket"};

                socket = IO.socket(SOCKET_URL, options);

                setupConnectionLogs();

            } catch (URISyntaxException e) {

                Log.e(TAG, "Socket URL error", e);

            }

        }

        return socket;
    }

    /* ============================================================
       CONNECTION LOGS
============================================================ */

    private static void setupConnectionLogs() {

        if (socket == null) return;

        socket.on(Socket.EVENT_CONNECT, args ->
                Log.d(TAG, "✅ Socket connected"));

        socket.on(Socket.EVENT_DISCONNECT, args ->
                Log.d(TAG, "❌ Socket disconnected"));

        socket.on(Socket.EVENT_CONNECT_ERROR, args ->
                Log.e(TAG, "⚠️ Socket connection error"));



    }

    /* ============================================================
       CONNECT SOCKET
============================================================ */

    public static void connect() {

        if (socket == null) {
            getSocket();
        }

        if (socket != null && !socket.connected()) {

            socket.connect();

            Log.d(TAG, "Socket connecting...");

        }

    }

    /* ============================================================
       DISCONNECT SOCKET
============================================================ */

    public static void disconnect() {

        if (socket != null && socket.connected()) {

            socket.disconnect();

            Log.d(TAG, "Socket disconnected");

        }

    }

    /* ============================================================
       JOIN OWNER ROOM (Vendor)
============================================================ */

    public static void joinOwner(String ownerId) {

        if (socket == null) return;

        if (!socket.connected()) {
            connect();
        }

        try {

            socket.emit("join_owner", ownerId);

            Log.d(TAG, "👨‍🍳 Joined owner room: " + ownerId);

        } catch (Exception e) {

            Log.e(TAG, "Join owner error", e);

        }

    }

    /* ============================================================
       JOIN USER ROOM
============================================================ */

    public static void joinUser(String userId) {

        if (socket == null) return;

        socket.emit("join_user", userId);

        Log.d(TAG, "👤 Joined user room: " + userId);

    }

    /* ============================================================
       EMIT EVENT
============================================================ */

    public static void emit(String event, Object data) {

        if (socket != null && socket.connected()) {

            socket.emit(event, data);

            Log.d(TAG, "Emit event: " + event);

        }

    }

    /* ============================================================
       LISTEN EVENT
============================================================ */

    public static void on(String event, io.socket.emitter.Emitter.Listener listener) {

        if (socket != null) {

            socket.off(event); // prevent duplicate listeners
            socket.on(event, listener);

        }

    }

    /* ============================================================
       REMOVE LISTENER
============================================================ */

    public static void off(String event) {

        if (socket != null) {

            socket.off(event);

        }

    }

    /* ============================================================
       CLEAR ALL LISTENERS
============================================================ */

    public static void clearAllListeners() {

        if (socket != null) {

            socket.off();

        }

    }

}