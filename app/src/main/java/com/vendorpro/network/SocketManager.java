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
       CHANGE THIS TO YOUR BACKEND URL
    ============================================================ */

    private static final String SOCKET_URL = "http://10.0.2.2:4000";

    private static Socket socket;

    /* ============================================================
       GET SOCKET INSTANCE
    ============================================================ */

    public static Socket getSocket() {

        if (socket == null) {

            try {

                IO.Options options = new IO.Options();

                options.reconnection = true;
                options.reconnectionAttempts = Integer.MAX_VALUE;
                options.reconnectionDelay = 1000;
                options.timeout = 20000;

                socket = IO.socket(SOCKET_URL, options);

            } catch (URISyntaxException e) {

                Log.e(TAG, "Socket URL error", e);

            }

        }

        return socket;
    }

    /* ============================================================
       CONNECT SOCKET
    ============================================================ */

    public static void connect() {

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
       EMIT EVENT
    ============================================================ */

    public static void emit(String event, Object data) {

        if (socket != null) {

            socket.emit(event, data);

        }
    }

    /* ============================================================
       LISTEN EVENT
    ============================================================ */

    public static void on(String event, io.socket.emitter.Emitter.Listener listener) {

        if (socket != null) {

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

}