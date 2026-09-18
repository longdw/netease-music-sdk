package com.netease.android.extension.servicekeeper.service.observable;

import com.netease.android.extension.servicekeeper.service.observable.subscriber.NotifyServiceSubscriber;

public class NotifyObservableService extends ObservableService<Void, NotifyServiceSubscriber> {
    public NotifyObservableService(ObservableServiceUniqueId<Void> serviceUniqueId) {
        super(serviceUniqueId);
    }

    public void emit() {
        this.send(null);
    }
}