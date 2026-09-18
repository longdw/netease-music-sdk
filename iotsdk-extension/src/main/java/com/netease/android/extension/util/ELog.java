package com.netease.android.extension.util;

import android.util.Log;

public class ELog {
    private static boolean isDebug = false;
    private static String DEFAULT_TAG = "ExtensionLog";

    public static void setDefaultTag(String defaultTag) {
        DEFAULT_TAG = defaultTag;
    }

    public static void setDebug(boolean isDebug) {
        ELog.isDebug = isDebug;
    }

    public static boolean showLog() {
        return isDebug;
    }

    public static int i(String msg) {
        return Log.i(DEFAULT_TAG, msg);
    }

    public static int i(String tag, String msg) {
        return Log.i(DEFAULT_TAG, "[" + tag + "]" + msg);
    }

    public static int w(String msg) {
        return Log.w(DEFAULT_TAG, msg);
    }

    public static int w(String tag, String msg) {
        return Log.w(DEFAULT_TAG, "[" + tag + "]" + msg);
    }

    public static int w(String msg, Throwable throwable) {
        return Log.w(DEFAULT_TAG, msg, throwable);
    }

    public static int e(String msg) {
        return Log.e(DEFAULT_TAG, msg);
    }

    public static int e(String msg, Throwable throwable) {
        return Log.e(DEFAULT_TAG, msg, throwable);
    }

    public static int e(String tag, String msg, Throwable throwable) {
        return Log.e(DEFAULT_TAG, "[" + tag + "]" + msg, throwable);
    }
}
