package com.netease.android.extension.servicekeeper.service.ipc.lock;

import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;

public interface IIPCLockServiceKeeperExport {
    void tryLock(@NonNull IPCLockServiceUniqueId var1, @NonNull String var2, int var3, @NonNull NFuncB var4) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException;

    void tryLockOrIgnore(@NonNull IPCLockServiceUniqueId var1, @NonNull String var2, int var3, @NonNull NFuncB var4);

    void release(@NonNull IPCLockServiceUniqueId var1, @NonNull String var2, @NonNull NFuncB var3) throws SDKServiceNotFoundException;

    void releaseOrIgnore(@NonNull IPCLockServiceUniqueId var1, @NonNull String var2, @NonNull NFuncB var3);
}