package com.netease.android.extension.servicekeeper.service.observable.compose;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.observable.IObservableService;
import com.netease.android.extension.servicekeeper.service.observable.ObservableService;
import com.netease.android.extension.servicekeeper.service.observable.ObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.compose.ComposeServiceSubscriber;
import com.netease.android.extension.servicekeeper.util.SKMUtil;

public class ComposeObservableService<Emit, Subscriber extends ComposeServiceSubscriber<Emit>> extends ObservableService<Emit, Subscriber> implements IObservableService<Emit, Subscriber> {
    @NonNull
    protected IServiceKeeperMaster serviceKeeperMaster;

    public ComposeObservableService(@NonNull IServiceKeeperMaster serviceKeeperMaster, @NonNull ObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceUniqueId);
        this.serviceKeeperMaster = serviceKeeperMaster;
    }

    public boolean subscribe(@NonNull Subscriber subscriber) {
        boolean subscribed = super.subscribe(subscriber);
        if (subscribed) {
            subscriber.onBindObservableService(this);
        }

        return subscribed;
    }

    public boolean unsubscribe(@NonNull Subscriber subscriber) {
        boolean unsubscribed = super.unsubscribe(subscriber);
        if (unsubscribed) {
            subscriber.onUnbindObservableService(this);
        }

        return unsubscribed;
    }

    public void onUnregister() {
        super.onUnregister();

        for(Subscriber subscriber : this.subscribers) {
            boolean cancelBySelf = SKMUtil.isSameLocalSKC(this.serviceKeeperMaster, subscriber.getOwnerSKM());
            subscriber.onCancel(cancelBySelf);
        }

    }
}