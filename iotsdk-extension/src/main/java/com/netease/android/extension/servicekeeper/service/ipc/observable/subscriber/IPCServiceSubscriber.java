package com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber;

import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;

public interface IPCServiceSubscriber<Emit> {
    void onReceive(Emit var1, IPCRoute var2);
}