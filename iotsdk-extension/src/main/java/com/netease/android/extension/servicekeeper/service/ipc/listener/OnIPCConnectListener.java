package com.netease.android.extension.servicekeeper.service.ipc.listener;

public interface OnIPCConnectListener {
    void onIPCConnected();

    void onIPCAttached();

    void onIPCDetached();

    void onIPCDisconnected();
}