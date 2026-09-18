package com.netease.android.extension.error;

import androidx.annotation.RequiresApi;

public class SDKRuntimeException extends RuntimeException {
    public SDKRuntimeException() {
    }

    public SDKRuntimeException(String message) {
        super(message);
    }

    public SDKRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKRuntimeException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}