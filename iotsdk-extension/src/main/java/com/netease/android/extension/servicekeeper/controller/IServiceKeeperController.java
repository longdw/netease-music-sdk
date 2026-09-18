package com.netease.android.extension.servicekeeper.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.SKLifecycle;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.keeper.IServiceKeeper;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.lock.IIPCLockServiceKeeperExport;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IIPCObservableServiceKeeperExport;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.servicekeeper.service.observable.IObservableServiceKeeperExport;
import com.netease.android.extension.servicekeeper.service.proxy.IProxyServiceKeeperExport;

public interface IServiceKeeperController extends SKLifecycle, IProxyServiceKeeperExport, IObservableServiceKeeperExport, IIPCObservableServiceKeeperExport, IIPCLockServiceKeeperExport {
    void initialize();

    @NonNull
    SKCSerial getSKCSerial();

    @Nullable
    IServiceTick obtainServiceOrNull(@NonNull IServiceUniqueId var1);

    boolean isServiceExist(@NonNull IServiceUniqueId var1);

    <ServiceTick extends IServiceTick> ServiceTick register(@NonNull ServiceTick var1) throws SDKServiceAlreadyRegisteredException;

    <ServiceTick extends IServiceTick> ServiceTick unregister(@NonNull ServiceTick var1);

    /** @deprecated */
    @Deprecated
    IServiceTick unregister(@NonNull IServiceUniqueId var1);

    void destroy();

    @NonNull
    <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull IServiceUniqueId<ServiceKeeper> var1);

    @NonNull
    <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeper(@NonNull ServiceUniqueIdType var1);

    @Nullable
    <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull IServiceUniqueId<ServiceKeeper> var1);

    @Nullable
    <ServiceKeeper extends IServiceKeeper> ServiceKeeper getServiceKeeperOrNull(@NonNull ServiceUniqueIdType var1);
}
