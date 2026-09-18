package com.netease.android.extension.servicekeeper.keeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.controller.IServiceKeeperController;
import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.util.ELog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractServiceKeeper<ServiceUniqueId extends IServiceUniqueId, ServiceTick extends IServiceTick> implements IServiceKeeper<ServiceUniqueId, ServiceTick> {
    protected ConcurrentHashMap<ServiceUniqueId, ServiceTick> services = new ConcurrentHashMap();
    protected IServiceKeeperController serviceKeeperController;

    public void injectServiceKeeperController(@NonNull IServiceKeeperController serviceKeeperController) {
        this.serviceKeeperController = serviceKeeperController;
    }

    public void initialize() {
    }

    @NonNull
    protected final ServiceTick getServiceOrThrow(ServiceUniqueId serviceUniqueId, String errorTag) throws SDKServiceNotFoundException {
        ServiceTick serviceTick = this.getServiceOrNullInternal(serviceUniqueId);
        if (null == serviceTick) {
            throw new SDKServiceNotFoundException("[" + this.getClass().getSimpleName() + "]" + errorTag + " error: can not found serviceTick uniqueId " + serviceUniqueId + "!");
        } else {
            return serviceTick;
        }
    }

    @Nullable
    protected final ServiceTick getServiceOrNull(ServiceUniqueId serviceUniqueId) {
        return (ServiceTick)this.getServiceOrNullInternal(serviceUniqueId);
    }

    @Nullable
    protected ServiceTick getServiceOrNullInternal(ServiceUniqueId serviceUniqueId) {
        return (ServiceTick)(this.services.get(serviceUniqueId));
    }

    @NonNull
    protected ServiceTick getServiceOrThrow(@NonNull String serviceUniqueIdName, String errorTag) throws SDKServiceNotFoundException {
        ServiceTick serviceTick = this.getServiceOrNull(serviceUniqueIdName);
        if (null == serviceTick) {
            throw new SDKServiceNotFoundException("[" + this.getClass().getSimpleName() + "]" + errorTag + " error: can not found serviceTick uniqueIdName " + serviceUniqueIdName + "!");
        } else {
            return serviceTick;
        }
    }

    @Nullable
    protected ServiceTick getServiceOrNull(@NonNull String serviceUniqueIdName) {
        for(Map.Entry<ServiceUniqueId, ServiceTick> entry : this.services.entrySet()) {
            if (ObjectExt.isEquals(((IServiceUniqueId)entry.getKey()).getName(), serviceUniqueIdName)) {
                return (ServiceTick)(entry.getValue());
            }
        }

        return null;
    }

    public ServiceTick register(@NonNull ServiceTick serviceTick) throws SDKServiceAlreadyRegisteredException {
        ServiceUniqueId uniqueId = (ServiceUniqueId) serviceTick.getUniqueId();
        ServiceTick result = (ServiceTick)(this.services.putIfAbsent(uniqueId, serviceTick));
        if (null != result) {
            throw new SDKServiceAlreadyRegisteredException("[" + this.getClass().getSimpleName() + "]Error: Duplicate services uniqueId named " + uniqueId + " to register !");
        } else {
            if (ELog.showLog()) {
                ELog.i("[" + this.getClass().getSimpleName() + "]---> register, service uniqueId: " + uniqueId + ", service: " + serviceTick.getClass().getSimpleName());
            }

            serviceTick.onRegister();
            return serviceTick;
        }
    }

    public ServiceTick unregister(@NonNull ServiceTick serviceTick) {
        return (ServiceTick)this.unregister((ServiceTick) serviceTick.getUniqueId());
    }

    public ServiceTick unregister(@NonNull ServiceUniqueId serviceUniqueId) {
        ServiceTick removed = (ServiceTick)(this.services.remove(serviceUniqueId));
        if (null != removed) {
            removed.onUnregister();
            if (ELog.showLog()) {
                ELog.i("[ServiceKeeper]---> unregister, service uniqueId: " + serviceUniqueId + ", service: " + removed.getClass().getSimpleName());
            }
        }

        return removed;
    }

    public ServiceTick obtainService(@NonNull ServiceUniqueId serviceUniqueId) throws SDKServiceNotFoundException {
        return (ServiceTick)this.getServiceOrThrow(serviceUniqueId, "obtainService");
    }

    @Nullable
    public ServiceTick obtainServiceOrNull(@NonNull ServiceUniqueId serviceUniqueId) {
        return (ServiceTick)this.getServiceOrNull(serviceUniqueId);
    }

    @Nullable
    public ServiceTick obtainServiceOrNull(@NonNull String serviceUniqueId) {
        return (ServiceTick)this.getServiceOrNull(serviceUniqueId);
    }

    public void destroy() {
        this.services.clear();
    }
}