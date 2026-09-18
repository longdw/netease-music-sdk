package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;

public class IPCDefaultObservableService<Emit extends Parcelable> extends IPCObservableService<Emit, IPCServiceSubscriber<Emit>> {
    public IPCDefaultObservableService(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceUniqueId);
    }
}