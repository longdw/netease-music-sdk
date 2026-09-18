package com.netease.android.extension.servicekeeper.error;

import androidx.annotation.RequiresApi;

public class SDKServiceNotFoundException extends SDKServiceKeeperException {
    public SDKServiceNotFoundException() {
    }

    public SDKServiceNotFoundException(String message) {
        super(message);
    }

    public SDKServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
            api = 24
    )
    public SDKServiceNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}