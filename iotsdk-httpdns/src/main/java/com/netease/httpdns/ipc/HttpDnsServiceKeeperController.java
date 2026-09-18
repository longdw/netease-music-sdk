package com.netease.httpdns.ipc;

import android.content.Context;
import com.netease.android.extension.servicekeeper.controller.ServiceKeeperController;
import com.netease.android.extension.servicekeeper.service.ipc.listener.OnIPCConnectListener;

public class HttpDnsServiceKeeperController extends ServiceKeeperController {
    private OnIPCConnectListener listener;

    public HttpDnsServiceKeeperController(Context applicationContext, String scopeUniqueId) {
        super(applicationContext, scopeUniqueId);
    }

    public OnIPCConnectListener getOnIPCConnectListener() {
        return this.listener;
    }

    public void setOnIPCConnectListener(OnIPCConnectListener listener) {
        this.listener = listener;
    }

    public void onIPCAttached() {
        super.onIPCAttached();
        if (this.listener != null) {
            this.listener.onIPCAttached();
        }

    }

    public void onIPCConnected() {
        super.onIPCConnected();
        if (this.listener != null) {
            this.listener.onIPCConnected();
        }

    }

    public void onIPCDetached() {
        super.onIPCDetached();
        if (this.listener != null) {
            this.listener.onIPCDetached();
        }

    }

    public void onIPCDisconnected() {
        super.onIPCDisconnected();
        if (this.listener != null) {
            this.listener.onIPCDisconnected();
        }

    }
}