package com.netease.android.extension.servicekeeper.service.starter;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.servicestarter.ServiceStarter;

public interface IStarterService<Starter extends ServiceStarter> extends IServiceTick<StarterServiceUniqueId<Starter>> {
    @NonNull
    Starter getStarter();
}