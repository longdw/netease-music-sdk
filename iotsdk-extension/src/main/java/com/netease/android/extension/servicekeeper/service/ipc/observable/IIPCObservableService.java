package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.service.ipc.base.message.IIPCMessageServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;

public interface IIPCObservableService<Emit extends Parcelable, Subscriber extends IPCServiceSubscriber<Emit>> extends IIPCMessageServiceTick<Emit, IPCObservableServiceUniqueId<Emit>> {
    boolean subscribe(@NonNull Subscriber var1);

    boolean unsubscribe(@NonNull Subscriber var1);
}