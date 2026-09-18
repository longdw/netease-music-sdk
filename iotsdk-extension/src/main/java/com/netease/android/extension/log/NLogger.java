package com.netease.android.extension.log;

import android.util.Log;
import com.netease.android.extension.util.ELog;

public class NLogger {
    private boolean isDebug = false;
    private String defaultTag = "NLogger";

    public NLogger() {
    }

    public NLogger(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    public void setDebug(boolean debug) {
        this.isDebug = debug;
        if (this.isDebug) {
            ELog.setDebug(true);
        }

    }

    public void setDefaultTag(String defaultTag) {
        this.defaultTag = defaultTag;
    }

    public boolean showLog() {
        return this.isDebug;
    }

    public int i(String msg) {
        return Log.i(this.defaultTag, msg);
    }

    public int i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    public int w(String msg) {
        return Log.w(this.defaultTag, msg);
    }

    public int w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    public int w(String msg, Throwable throwable) {
        return Log.w(this.defaultTag, msg, throwable);
    }

    public int e(String msg) {
        return Log.e(this.defaultTag, msg);
    }

    public int e(String msg, Throwable throwable) {
        return Log.e(this.defaultTag, msg, throwable);
    }

    public int e(String tag, String msg, Throwable throwable) {
        return Log.e(tag, msg, throwable);
    }
}