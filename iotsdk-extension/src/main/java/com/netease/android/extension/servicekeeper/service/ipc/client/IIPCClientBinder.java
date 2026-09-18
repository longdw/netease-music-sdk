package com.netease.android.extension.servicekeeper.service.ipc.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.SKLifecycle;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;

public interface IIPCClientBinder extends SKLifecycle {
    void bindServiceKeeper(IIPCServiceKeeper var1);

    void unbindServiceKeeper(IIPCServiceKeeper var1);

    @Nullable
    IPCServer getIPCServerOrNull();

    @NonNull
    SKCSerial getSKCSerial();
}