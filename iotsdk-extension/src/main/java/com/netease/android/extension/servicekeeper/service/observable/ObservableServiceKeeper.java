package com.netease.android.extension.servicekeeper.service.observable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceKeeperException;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.keeper.AbstractServiceKeeper;
import com.netease.android.extension.servicekeeper.service.observable.subscriber.ServiceSubscriber;
import com.netease.android.extension.util.ELog;

public class ObservableServiceKeeper extends AbstractServiceKeeper<ObservableServiceUniqueId, IObservableService> implements IObservableServiceKeeper {
    private static final String TAG = ObservableServiceKeeper.class.getSimpleName();

    public <Emit> boolean subscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        IObservableService observableService = (IObservableService)this.getServiceOrThrow(serviceUniqueId, "subscribe");
        boolean subscribed = observableService.subscribe(serviceSubscriber);
        if (subscribed && ELog.showLog()) {
            ELog.i("[" + TAG + "]subscribe, service uniqueId: " + serviceUniqueId + ", service: " + observableService.getClass().getSimpleName());
        }

        return subscribed;
    }

    public <Emit> boolean subscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IObservableService observableService = (IObservableService)this.getServiceOrNull(serviceUniqueId);
        if (null == observableService) {
            return false;
        } else {
            boolean subscribed = observableService.subscribe(serviceSubscriber);
            if (subscribed && ELog.showLog()) {
                ELog.i("[" + TAG + "]subscribeOrFalse, service uniqueId: " + serviceUniqueId + ", service: " + observableService.getClass().getSimpleName());
            }

            return subscribed;
        }
    }

    public <Emit> boolean unsubscribe(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) throws SDKServiceNotFoundException {
        IObservableService observableService = (IObservableService)this.getServiceOrThrow(serviceUniqueId, "unsubscribe");
        boolean unsubscribed = observableService.unsubscribe(serviceSubscriber);
        if (unsubscribed && ELog.showLog()) {
            ELog.i("[" + TAG + "]unsubscribe, service uniqueId: " + serviceUniqueId + ", service: " + observableService.getClass().getSimpleName());
        }

        return unsubscribed;
    }

    public <Emit> boolean unsubscribeOrFalse(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull ServiceSubscriber<Emit> serviceSubscriber) {
        IObservableService observableService = (IObservableService)this.getServiceOrNull(serviceUniqueId);
        if (null == observableService) {
            return false;
        } else {
            boolean subscribed = observableService.unsubscribe(serviceSubscriber);
            if (subscribed && ELog.showLog()) {
                ELog.i("[" + TAG + "]unsubscribeOrFalse, service uniqueId: " + serviceUniqueId + ", service: " + observableService.getClass().getSimpleName());
            }

            return subscribed;
        }
    }

    public <Emit> boolean send(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @Nullable Emit emit) throws SDKServiceKeeperException {
        IObservableService service = (IObservableService)this.getServiceOrThrow(serviceUniqueId, "send");
        return service.send(emit);
    }

    public <Emit> boolean sendOrIgnore(@NonNull ObservableServiceUniqueId<Emit> serviceUniqueId, @Nullable Emit emit) {
        IObservableService service = (IObservableService)this.getServiceOrNull(serviceUniqueId);
        return null != service && service.send(emit);
    }
}
