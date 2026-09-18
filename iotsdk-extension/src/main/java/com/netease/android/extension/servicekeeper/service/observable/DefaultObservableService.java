package com.netease.android.extension.servicekeeper.service.observable;

import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;

public class DefaultObservableService<Emit> extends ObservableService<Emit, ServiceSubscriber<Emit>> {
    public DefaultObservableService(ObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceUniqueId);
    }
}