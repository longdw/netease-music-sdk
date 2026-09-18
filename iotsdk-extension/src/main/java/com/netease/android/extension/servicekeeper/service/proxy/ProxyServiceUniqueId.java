package com.netease.android.extension.servicekeeper.service.proxy;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.id.AbstractServiceUniqueId;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdScope;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;

public class ProxyServiceUniqueId<ProxyType> extends AbstractServiceUniqueId<IProxyServiceKeeper> {
    public ProxyServiceUniqueId() {
    }

    public ProxyServiceUniqueId(@NonNull String name) {
        super(name);
    }

    public ProxyServiceUniqueId(@NonNull String name, ServiceUniqueIdScope expectServiceUniqueIdScope) {
        super(name, expectServiceUniqueIdScope);
    }

    public ServiceUniqueIdType getUniqueIdType() {
        return ServiceUniqueIdType.PROXY_SERVICE_UNIQUE_ID;
    }
}