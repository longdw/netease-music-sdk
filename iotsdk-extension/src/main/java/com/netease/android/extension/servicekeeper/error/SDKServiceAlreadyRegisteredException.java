package com.netease.android.extension.servicekeeper.error;

import androidx.annotation.RequiresApi;

public class SDKServiceAlreadyRegisteredException extends SDKServiceKeeperException {
    public SDKServiceAlreadyRegisteredException() {
    }

    public SDKServiceAlreadyRegisteredException(String message) {
        super(message);
    }

    public SDKServiceAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKServiceAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKServiceAlreadyRegisteredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}