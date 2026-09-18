package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;

public interface IIPCObservableServiceKeeperExport {
    <Emit extends Parcelable> boolean subscribe(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCServiceSubscriber<Emit> var2) throws SDKServiceNotFoundException;

    <Emit extends Parcelable> boolean subscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCServiceSubscriber<Emit> var2);

    <Emit extends Parcelable> boolean unsubscribe(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCServiceSubscriber<Emit> var2) throws SDKServiceNotFoundException;

    <Emit extends Parcelable> boolean unsubscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCServiceSubscriber<Emit> var2);
}