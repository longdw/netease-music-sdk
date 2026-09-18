package com.netease.android.extension.servicekeeper.service.proxy.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyService;
import com.netease.android.extension.servicekeeper.service.proxy.ProxyServiceUniqueId;

public class ComposeProxyService<Proxy> extends ProxyService<Proxy> {
    public ComposeProxyService(@NonNull ProxyServiceUniqueId<Proxy> serviceUniqueId, @NonNull Proxy proxy) {
        super(serviceUniqueId, proxy);
    }
}