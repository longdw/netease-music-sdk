package com.netease.android.extension.servicekeeper.service.ipc.lock.compose;

import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceUniqueId;
import com.netease.android.extension.servicekeeper.servicestarter.AbstractServiceStarter;

public abstract class IPCLockServiceStarter extends AbstractServiceStarter<IPCLockServiceUniqueId, ComposeIPCLockService> {
    public IPCLockServiceStarter(IServiceKeeperMaster serviceKeeperMaster, IPCLockServiceUniqueId serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }
}
