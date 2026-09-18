package com.netease.android.extension.servicekeeper.service.observable.compose;

import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.servicestarter.AbstractServiceStarter;

public abstract class NotifyObservableServiceStarter extends AbstractServiceStarter<ObservableServiceUniqueId<Void>, ComposeNotifyObservableService> {
    public NotifyObservableServiceStarter(IServiceKeeperMaster serviceKeeperMaster, ObservableServiceUniqueId<Void> serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }
}