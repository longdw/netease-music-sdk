package com.netease.android.extension.servicekeeper.service.starter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.keeper.AbstractServiceKeeper;

public class StarterServiceKeeper extends AbstractServiceKeeper<StarterServiceUniqueId, IStarterService> implements IStarterServiceKeeper {
    @NonNull
    public <T> T obtainStarter(String serviceUniqueIdName) throws SDKServiceNotFoundException {
        IStarterService starterService = (IStarterService)this.getServiceOrThrow(serviceUniqueIdName, "obtainStarter");
        return (T)starterService.getStarter();
    }

    @Nullable
    public <T> T obtainStarterOrNull(String serviceUniqueIdName) {
        IStarterService starterService = (IStarterService)this.getServiceOrNull(serviceUniqueIdName);
        return (T)(null == starterService ? null : starterService.getStarter());
    }
}