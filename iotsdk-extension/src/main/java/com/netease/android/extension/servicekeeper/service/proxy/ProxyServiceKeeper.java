package com.netease.android.extension.servicekeeper.service.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.keeper.AbstractServiceKeeper;
import com.netease.android.extension.util.ELog;

public class ProxyServiceKeeper extends AbstractServiceKeeper<ProxyServiceUniqueId, IProxyService> implements IProxyServiceKeeper {
    private static final String TAG = ProxyServiceKeeper.class.getSimpleName();

    @NonNull
    public <T> T obtainProxy(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) throws SDKServiceNotFoundException {
        IProxyService proxyService = (IProxyService)this.getServiceOrThrow(serviceUniqueId, "obtainProxy");
        if (null == proxyService) {
            throw new SDKServiceNotFoundException("[" + TAG + "]obtainProxy, the service uniqueId " + serviceUniqueId + " is not found !");
        } else {
            return (T)proxyService.getProxy();
        }
    }

    @Nullable
    public <T> T obtainProxyOrNull(@NonNull ProxyServiceUniqueId<T> serviceUniqueId) {
        IProxyService proxyService = (IProxyService)this.getServiceOrNull(serviceUniqueId);
        if (null == proxyService) {
            ELog.e("[" + TAG + "]obtainProxyOrNull, the service uniqueId " + serviceUniqueId + " is not found !");
            return null;
        } else {
            try {
                return (T)proxyService.getProxy();
            } catch (Throwable var4) {
                ELog.e("[" + TAG + "]obtainProxyOrNull, the service uniqueId " + serviceUniqueId + " proxy cast error !");
                return null;
            }
        }
    }
}
