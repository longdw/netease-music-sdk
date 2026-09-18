package com.netease.android.extension.servicekeeper.service.ipc.server.lock;

import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;

public interface IProcessLockController {
    boolean tryLock(IPCPack var1);

    boolean release(IPCPack var1);
}