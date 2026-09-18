package com.netease.android.extension.servicekeeper.service.ipc.base;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.keeper.AbstractServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;

public abstract class AbstractIPCServiceKeeper<ServiceUniqueId extends IIPCServiceUniqueId, ServiceTick extends IIPCServiceTick> extends AbstractServiceKeeper<ServiceUniqueId, ServiceTick> implements IIPCServiceKeeper<ServiceUniqueId, ServiceTick> {
    @NonNull
    protected IIPCClientBinder ipcClientBinder;

    public AbstractIPCServiceKeeper(@NonNull IIPCClientBinder ipcClientBinder) {
        this.ipcClientBinder = ipcClientBinder;
    }

    public void initialize() {
        super.initialize();
        this.ipcClientBinder.bindServiceKeeper(this);
    }

    public ServiceTick register(@NonNull ServiceTick serviceTick) throws SDKServiceAlreadyRegisteredException {
        ServiceTick service = (ServiceTick)(super.register(serviceTick));
        service.injectIpcClientBinder(this.ipcClientBinder);
        return service;
    }

    public void destroy() {
        super.destroy();
        this.ipcClientBinder.unbindServiceKeeper(this);
    }
}