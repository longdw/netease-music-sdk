package com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IIPCObservableService;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;

public interface IComposeIPCServiceSubscriber<Emit> extends IPCServiceSubscriber<Emit> {
    void onCancel(boolean var1);

    void onBindObservableService(IIPCObservableService var1);

    void onUnbindObservableService(IIPCObservableService var1);

    @NonNull
    IServiceKeeperMaster getOwnerSKM();
}