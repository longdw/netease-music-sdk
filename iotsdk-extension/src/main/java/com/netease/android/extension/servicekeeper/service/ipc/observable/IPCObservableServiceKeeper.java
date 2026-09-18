package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.ipc.base.AbstractIPCServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;

public class IPCObservableServiceKeeper extends AbstractIPCServiceKeeper<IPCObservableServiceUniqueId, IIPCObservableService> implements IIPCObservableServiceKeeper {
    public IPCObservableServiceKeeper(@NonNull IIPCClientBinder ipcClientBinder) {
        super(ipcClientBinder);
    }

    public <Emit extends Parcelable> boolean subscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        IIPCObservableService<Emit, IPCServiceSubscriber<Emit>> service = (IIPCObservableService)this.getServiceOrThrow(serviceUniqueId, "subscribe");
        return service.subscribe(subscriber);
    }

    public <Emit extends Parcelable> boolean subscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrNull(serviceUniqueId);
        return null != service && service.subscribe(subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribe(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) throws SDKServiceNotFoundException {
        IIPCObservableService<Emit, IPCServiceSubscriber<Emit>> service = (IIPCObservableService)this.getServiceOrThrow(serviceUniqueId, "unsubscribe");
        return service.unsubscribe(subscriber);
    }

    public <Emit extends Parcelable> boolean unsubscribeOrFalse(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCServiceSubscriber<Emit> subscriber) {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrNull(serviceUniqueId);
        return null != service && service.unsubscribe(subscriber);
    }

    public <Emit extends Parcelable> void send(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCPack<Emit> ipcPack) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrThrow(serviceUniqueId, "send");
        service.send(ipcPack);
    }

    public <Emit extends Parcelable> void send(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @Nullable Emit parcelable) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrThrow(serviceUniqueId, "send");
        service.send(parcelable);
    }

    public <Emit extends Parcelable> void sendOrIgnore(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @NonNull IPCPack<Emit> ipcPack) {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrNull(serviceUniqueId);
        if (null != service) {
            service.sendOrDispatch(ipcPack);
        }

    }

    public <Emit extends Parcelable> void sendOrIgnore(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId, @Nullable Emit parcelable) {
        IIPCObservableService service = (IIPCObservableService)this.getServiceOrNull(serviceUniqueId);
        if (null != service) {
            service.sendOrDispatch(parcelable);
        }

    }
}
