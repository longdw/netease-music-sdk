package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.service.AbstractServiceTick;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObservableService<Emit, Subscriber extends ServiceSubscriber<Emit>> extends AbstractServiceTick<ObservableServiceUniqueId<Emit>> implements IObservableService<Emit, Subscriber> {
    protected final Set<Subscriber> subscribers = Collections.synchronizedSet(new CopyOnWriteArraySet());

    public ObservableService(ObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceUniqueId);
    }

    public boolean subscribe(@NonNull Subscriber subscriber) {
        return this.subscribers.add(subscriber);
    }

    public boolean unsubscribe(@NonNull Subscriber subscriber) {
        return this.subscribers.remove(subscriber);
    }

    public boolean send(@Nullable Emit emit) {
        for(Subscriber next : this.subscribers) {
            next.onReceive(emit);
        }

        return true;
    }
}