package com.netease.android.extension.servicekeeper.service.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;

public interface IProxyServiceKeeperExport {
    @NonNull
    <T> T obtainProxy(@NonNull ProxyServiceUniqueId<T> var1) throws SDKServiceNotFoundException;

    @Nullable
    <T> T obtainProxyOrNull(@NonNull ProxyServiceUniqueId<T> var1);
}