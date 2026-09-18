package com.netease.android.extension.servicekeeper.service.ipc.lock;

import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFunc0;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;

public interface IIPCLockService extends IIPCServiceTick<IPCLockServiceUniqueId> {
    void tryLock(@NonNull String var1, int var2, @NonNull NFunc0 var3) throws SDKIPCServerNotConnectedException;

    void tryLock(@NonNull String var1, int var2, @NonNull NFuncB var3) throws SDKIPCServerNotConnectedException;

    void release(@NonNull String var1, @NonNull NFunc0 var2);

    void release(@NonNull String var1, @NonNull NFuncB var2);
}