package com.netease.android.extension.servicekeeper.service.observable.compose;

import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.compose.ComposeServiceSubscriber;
import com.netease.android.extension.servicekeeper.servicestarter.AbstractServiceStarter;

public abstract class ObservableServiceStarter<Emit> extends AbstractServiceStarter<ObservableServiceUniqueId<Emit>, ComposeObservableService<Emit, ComposeServiceSubscriber<Emit>>> {
    public ObservableServiceStarter(IServiceKeeperMaster serviceKeeperMaster, ObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }
}