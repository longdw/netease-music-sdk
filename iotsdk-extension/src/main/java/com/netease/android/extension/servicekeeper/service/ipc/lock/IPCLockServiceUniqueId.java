package com.netease.android.extension.servicekeeper.service.ipc.lock;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.AbstractServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceUniqueId;

public class IPCLockServiceUniqueId extends AbstractServiceUniqueId<IPCLockServiceKeeper> implements IIPCServiceUniqueId<IPCLockServiceKeeper> {
    public IPCLockServiceUniqueId() {
    }

    public IPCLockServiceUniqueId(@NonNull String name) {
        super(name);
    }

    public IPCLockServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        super(name, expectServiceUniqueIdScope);
    }

    public ServiceUniqueIdType getUniqueIdType() {
        return ServiceUniqueIdType.IPC_LOCK_UNIQUE_ID;
    }
}