package com.netease.android.extension.error;

import androidx.annotation.RequiresApi;

public class SDKStartFailException extends SDKRuntimeException {
    public SDKStartFailException() {
    }

    public SDKStartFailException(String message) {
        super(message);
    }

    public SDKStartFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKStartFailException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKStartFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
