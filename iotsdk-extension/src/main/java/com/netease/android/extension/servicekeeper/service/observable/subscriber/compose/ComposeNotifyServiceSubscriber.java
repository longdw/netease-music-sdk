package com.netease.android.extension.servicekeeper.service.observable.subscriber.compose;

import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;

public abstract class ComposeNotifyServiceSubscriber extends ComposeServiceSubscriber<Void> {
    public ComposeNotifyServiceSubscriber(IServiceKeeperMaster serviceKeeperMaster) {
        super(serviceKeeperMaster);
    }

    public final void onReceive(Void aVoid) {
        this.onReceive();
    }

    public abstract void onReceive();
}