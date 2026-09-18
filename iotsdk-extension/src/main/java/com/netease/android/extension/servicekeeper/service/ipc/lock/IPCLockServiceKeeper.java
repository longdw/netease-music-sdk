package com.netease.android.extension.servicekeeper.service.ipc.lock;

import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.ipc.base.AbstractIPCServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.util.ELog;

public class IPCLockServiceKeeper extends AbstractIPCServiceKeeper<IPCLockServiceUniqueId, IIPCLockService> implements IIPCLockServiceKeeper {
    private static final String TAG = IPCLockServiceKeeper.class.getSimpleName();

    public IPCLockServiceKeeper(@NonNull IIPCClientBinder ipcClientBinder) {
        super(ipcClientBinder);
    }

    public void tryLock(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException {
        IIPCLockService service = (IIPCLockService)this.getServiceOrThrow(serviceUniqueId, "send");
        service.tryLock(target, autoReleaseMillis, callBack);
    }

    public void tryLockOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, int autoReleaseMillis, @NonNull NFuncB callBack) {
        IIPCLockService service = (IIPCLockService)this.getServiceOrNull(serviceUniqueId);

        try {
            if (null != service) {
                service.tryLock(target, autoReleaseMillis, callBack);
            }
        } catch (SDKIPCServerNotConnectedException e) {
            ELog.e("[" + TAG + "]tryLockOrFalse error: ", e);
        }

    }

    public void release(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) throws SDKServiceNotFoundException {
        IIPCLockService service = (IIPCLockService)this.getServiceOrThrow(serviceUniqueId, "send");
        service.release(target, callBack);
    }

    public void releaseOrIgnore(@NonNull IPCLockServiceUniqueId serviceUniqueId, @NonNull String target, @NonNull NFuncB callBack) {
        IIPCLockService service = (IIPCLockService)this.getServiceOrNull(serviceUniqueId);
        if (null != service) {
            service.release(target, callBack);
        }

    }
}