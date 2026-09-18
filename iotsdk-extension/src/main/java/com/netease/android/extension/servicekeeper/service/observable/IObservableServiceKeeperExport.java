package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;

public interface IObservableServiceKeeperExport {
    <Emit> boolean subscribe(@NonNull ObservableServiceUniqueId<Emit> var1, @NonNull ServiceSubscriber<Emit> var2) throws SDKServiceNotFoundException;

    <Emit> boolean subscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> var1, @NonNull ServiceSubscriber<Emit> var2);

    <Emit> boolean unsubscribe(@NonNull ObservableServiceUniqueId<Emit> var1, @NonNull ServiceSubscriber<Emit> var2) throws SDKServiceNotFoundException;

    <Emit> boolean unsubscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> var1, @NonNull ServiceSubscriber<Emit> var2);
}