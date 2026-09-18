package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.service.IServiceTick;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;

public interface IObservableService<Emit, Subscriber extends ServiceSubscriber<Emit>> extends IServiceTick<ObservableServiceUniqueId<Emit>> {
    boolean subscribe(@NonNull Subscriber var1);

    boolean unsubscribe(@NonNull Subscriber var1);

    boolean send(@Nullable Emit var1);
}
