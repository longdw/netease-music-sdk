package com.netease.android.extension.servicekeeper.error;

import androidx.annotation.RequiresApi;
import com.netease.android.extension.error.SDKException;

public class SDKServiceKeeperException extends SDKException {
    public SDKServiceKeeperException() {
    }

    public SDKServiceKeeperException(String message) {
        super(message);
    }

    public SDKServiceKeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKServiceKeeperException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKServiceKeeperException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}