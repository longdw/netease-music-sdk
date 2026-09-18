package com.netease.android.extension.error;

import androidx.annotation.RequiresApi;

public class SDKProcessorInterruptedException extends Exception {
    public SDKProcessorInterruptedException() {
    }

    public SDKProcessorInterruptedException(String message) {
        super(message);
    }

    public SDKProcessorInterruptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKProcessorInterruptedException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKProcessorInterruptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}