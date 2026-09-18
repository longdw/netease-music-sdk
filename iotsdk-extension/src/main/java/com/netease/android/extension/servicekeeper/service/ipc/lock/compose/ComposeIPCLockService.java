package com.netease.android.extension.servicekeeper.service.ipc.lock.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockService;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IPCLockServiceUniqueId;

public class ComposeIPCLockService extends IPCLockService {
    public ComposeIPCLockService(@NonNull IPCLockServiceUniqueId serviceUniqueId) {
        super(serviceUniqueId);
    }
}
