package com.netease.httpdns.log;

import android.util.Log;
import com.netease.httpdns.util.S;

public class DNSLog {
    private static final String TAG = "HttpDns";
    private static boolean DEBUG = false;

    public static void setDEBUG(boolean debug) {
        DEBUG = debug;
        S.LOG.setDebug(debug);
    }

    public static void i(String message) {
        if (DEBUG) {
            Log.i("HttpDns", message);
        }

    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e("HttpDns", message);
        }

    }

    public static void d(String message) {
        if (DEBUG) {
            Log.d("HttpDns", message);
        }

    }

    public static void v(String message) {
        if (DEBUG) {
            Log.v("HttpDns", message);
        }

    }
}