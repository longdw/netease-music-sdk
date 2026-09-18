package com.netease.android.extension.servicekeeper.service.ipc.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;

public interface IIPCServiceKeeper<ServiceUniqueId extends IIPCServiceUniqueId, ServiceTick extends IIPCServiceTick> extends IServiceKeeper<ServiceUniqueId, ServiceTick> {
    ServiceTick obtainService(@NonNull ServiceUniqueId var1) throws SDKServiceNotFoundException;

    @Nullable
    ServiceTick obtainServiceOrNull(@NonNull ServiceUniqueId var1);

    @Nullable
    ServiceTick obtainServiceOrNull(@NonNull String var1);
}