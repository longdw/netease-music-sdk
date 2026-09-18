package com.netease.android.extension.servicekeeper.service.starter;

import androidx.annotation.NonNull;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.service.AbstractServiceTick;
import com.netease.android.extension.servicekeeper.servicestarter.ServiceStarter;

public class StarterService<Starter extends ServiceStarter> extends AbstractServiceTick<StarterServiceUniqueId<Starter>> implements IStarterService<Starter> {
    private Starter starter;

    public StarterService(@NonNull StarterServiceUniqueId<Starter> serviceUniqueId, @NonNull Starter starter) {
        super(serviceUniqueId);
        this.starter = starter;
        ObjectExt.requireNonNull(starter);
    }

    @NonNull
    public Starter getStarter() {
        return this.starter;
    }
}