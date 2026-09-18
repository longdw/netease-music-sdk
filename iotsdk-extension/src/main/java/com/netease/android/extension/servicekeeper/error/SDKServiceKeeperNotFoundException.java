package com.netease.android.extension.servicekeeper.error;

import androidx.annotation.RequiresApi;
import com.netease.android.extension.error.SDKRuntimeException;

public class SDKServiceKeeperNotFoundException extends SDKRuntimeException {
    public SDKServiceKeeperNotFoundException() {
    }

    public SDKServiceKeeperNotFoundException(String message) {
        super(message);
    }

    public SDKServiceKeeperNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKServiceKeeperNotFoundException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKServiceKeeperNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}