package com.netease.android.extension.servicekeeper.servicestarter;

import com.netease.android.extension.servicekeeper.error.SDKServiceAlreadyRegisteredException;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.IServiceTick;

public interface ServiceStarter<ServiceUniqueId extends IServiceUniqueId, ServiceTick extends IServiceTick<ServiceUniqueId>> {
    void initialize() throws SDKServiceAlreadyRegisteredException;

    void start();

    ServiceTick createService(ServiceUniqueId var1);

    void onBeforeCreateService();

    void onAfterCreateService(ServiceTick var1);

    void onBeforeRegisterService(ServiceTick var1);

    void onAfterRegisterService(ServiceTick var1);

    void destroy();
}