package com.netease.android.extension.servicekeeper.service.ipc.observable;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.AbstractServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceUniqueId;

public class IPCObservableServiceUniqueId<T> extends AbstractServiceUniqueId<IIPCObservableServiceKeeper> implements IIPCServiceUniqueId<IIPCObservableServiceKeeper> {
    public IPCObservableServiceUniqueId() {
    }

    public IPCObservableServiceUniqueId(@NonNull String name) {
        super(name);
    }

    public IPCObservableServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        super(name, expectServiceUniqueIdScope);
    }

    public ServiceUniqueIdType getUniqueIdType() {
        return ServiceUniqueIdType.IPC_OBSERVABLE_SERVICE_UNIQUE_ID;
    }
}