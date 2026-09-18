package com.netease.android.extension.servicekeeper.keeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.SKLifecycle;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.IServiceTick;

public interface IServiceKeeper<ServiceUniqueId extends IServiceUniqueId, ServiceTick extends IServiceTick> extends SKLifecycle {
    void injectServiceKeeperController(@NonNull IServiceKeeperController var1);

    ServiceTick register(@NonNull ServiceTick var1) throws SDKServiceAlreadyRegisteredException;

    ServiceTick unregister(@NonNull ServiceTick var1);

    ServiceTick unregister(@NonNull ServiceUniqueId var1);

    ServiceTick obtainService(@NonNull ServiceUniqueId var1) throws SDKServiceNotFoundException;

    @Nullable
    ServiceTick obtainServiceOrNull(@NonNull ServiceUniqueId var1);

    @Nullable
    ServiceTick obtainServiceOrNull(@NonNull String var1);
}