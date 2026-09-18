package com.netease.android.extension.servicekeeper.service.ipc.observable.compose;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.master.IServiceKeeperMaster;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IIPCObservableService;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableService;
import com.netease.android.extension.servicekeeper.service.ipc.observable.IPCObservableServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.compose.ComposeIPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.util.SKMUtil;

public class ComposeIPCObservableService<Emit extends Parcelable, Subscriber extends ComposeIPCServiceSubscriber<Emit>> extends IPCObservableService<Emit, Subscriber> implements IIPCObservableService<Emit, Subscriber> {
    @NonNull
    protected IServiceKeeperMaster serviceKeeperMaster;

    public ComposeIPCObservableService(@NonNull IServiceKeeperMaster serviceKeeperMaster, @NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId) {
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