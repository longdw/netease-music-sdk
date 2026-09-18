package com.netease.android.extension.servicekeeper.service.proxy;

import androidx.annotation.NonNull;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.service.AbstractServiceTick;

public class ProxyService<Proxy> extends AbstractServiceTick<ProxyServiceUniqueId<Proxy>> implements IProxyService<Proxy> {
    private Proxy proxy;

    public ProxyService(@NonNull ProxyServiceUniqueId<Proxy> serviceUniqueId, @NonNull Proxy proxy) {
        super(serviceUniqueId);
        this.proxy = proxy;
        ObjectExt.requireNonNull(proxy);
    }

    @NonNull
    public Proxy getProxy() {
        return this.proxy;
    }
}
