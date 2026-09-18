package com.netease.android.extension.servicekeeper.service.observable.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.compose.ComposeNotifyServiceSubscriber;

public class ComposeNotifyObservableService extends ComposeObservableService<Void, ComposeNotifyServiceSubscriber> {
    public ComposeNotifyObservableService(@NonNull IServiceKeeperMaster serviceKeeperMaster, @NonNull ObservableServiceUniqueId<Void> serviceUniqueId) {
        super(serviceKeeperMaster, serviceUniqueId);
    }

    public void emit() {
        this.send(null);
    }
}