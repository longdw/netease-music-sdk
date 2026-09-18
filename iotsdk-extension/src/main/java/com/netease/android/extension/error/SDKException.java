package com.netease.android.extension.error;

import androidx.annotation.RequiresApi;

public class SDKException extends Exception {
    public SDKException() {
    }

    public SDKException(String message) {
        super(message);
    }

    public SDKException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}