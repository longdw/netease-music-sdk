package com.netease.android.extension.servicekeeper.service.ipc.base;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;

public interface IIPCServiceTick<ServiceUniqueId extends IServiceUniqueId> extends IServiceTick<ServiceUniqueId> {
    void injectIpcClientBinder(@NonNull IIPCClientBinder var1);
}