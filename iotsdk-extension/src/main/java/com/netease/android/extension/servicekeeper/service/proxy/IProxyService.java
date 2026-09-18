package com.netease.android.extension.servicekeeper.service.proxy;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.IServiceTick;

public interface IProxyService<Proxy> extends IServiceTick<ProxyServiceUniqueId<Proxy>> {
    @NonNull
    Proxy getProxy();
}