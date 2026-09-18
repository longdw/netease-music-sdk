package com.netease.android.extension.servicekeeper.service.ipc.observable.compose;

import android.os.Parcelable;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.compose.ComposeIPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.servicestarter.AbstractServiceStarter;

public abstract class IPCObservableServiceStarter<Emit extends Parcelable> extends AbstractServiceStarter<IPCObservableServiceUniqueId<Emit>, ComposeIPCObservableService<Emit, ComposeIPCServiceSubscriber<Emit>>> {
    public IPCObservableServiceStarter(IServiceKeeperMaster serviceKeeperMaster, IPCObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }
}