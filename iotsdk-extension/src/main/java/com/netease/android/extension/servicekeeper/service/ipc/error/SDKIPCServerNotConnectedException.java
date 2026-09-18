package com.netease.android.extension.servicekeeper.service.ipc.error;

import androidx.annotation.RequiresApi;
import com.netease.android.extension.error.SDKException;

public class SDKIPCServerNotConnectedException extends SDKException {
    public SDKIPCServerNotConnectedException() {
    }

    public SDKIPCServerNotConnectedException(String message) {
        super(message);
    }

    public SDKIPCServerNotConnectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKIPCServerNotConnectedException(Throwable cause) {
        super(cause);
    }

    @RequiresApi(
        api = 24
    )
    public SDKIPCServerNotConnectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}