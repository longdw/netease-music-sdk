package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.service.ipc.base.AbstractIPCService;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.observable.subscriber.IPCServiceSubscriber;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.util.ELog;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class IPCObservableService<Emit extends Parcelable, Subscriber extends IPCServiceSubscriber<Emit>> extends AbstractIPCService<IPCObservableServiceUniqueId<Emit>> implements IIPCObservableService<Emit, Subscriber> {
    private static final String TAG = IPCObservableService.class.getSimpleName();
    protected final Set<Subscriber> subscribers = Collections.synchronizedSet(new CopyOnWriteArraySet());

    public IPCObservableService(@NonNull IPCObservableServiceUniqueId<Emit> serviceUniqueId) {
        super(serviceUniqueId);
    }

    public boolean subscribe(@NonNull Subscriber subscriber) {
        return this.subscribers.add(subscriber);
    }

    public boolean unsubscribe(@NonNull Subscriber subscriber) {
        return this.subscribers.remove(subscriber);
    }

    public boolean send(@Nullable Emit content) throws SDKIPCServerNotConnectedException {
        return this.sendInternal(new IPCPack(content), false);
    }

    public boolean sendOrDispatch(@Nullable Emit content) {
        try {
            return this.sendInternal(new IPCPack(content), true);
        } catch (SDKIPCServerNotConnectedException e) {
            ELog.e("[IPCObservableService]sendOrDispatch error: ", e);
            return false;
        }
    }

    public boolean send(@NonNull IPCPack<Emit> ipcPack) throws SDKIPCServerNotConnectedException {
        return this.sendInternal(ipcPack, false);
    }

    public boolean sendOrDispatch(@NonNull IPCPack<Emit> ipcPack) {
        try {
            return this.sendInternal(ipcPack, true);
        } catch (SDKIPCServerNotConnectedException e) {
            ELog.e("[IPCObservableService]sendOrDispatch error: ", e);
            return false;
        }
    }

    private boolean sendInternal(@NonNull IPCPack<Emit> ipcPack, boolean dispatchIfSendException) throws SDKIPCServerNotConnectedException {
        IPCServer ipcServer;
        if (null != this.ipcClientBinder && null != (ipcServer = this.ipcClientBinder.getIPCServerOrNull())) {
            if (!this.prepareIPCPack(ipcPack)) {
                ELog.w("[" + TAG + "]send fail. ipcPack: " + ipcPack);
                return false;
            } else {
                try {
                    ipcServer.send(((IPCObservableServiceUniqueId)this.serviceUniqueId).getUniqueIdType().name(), ((IPCObservableServiceUniqueId)this.serviceUniqueId).getName(), ipcPack);
                    return true;
                } catch (Exception e) {
                    ELog.e("[" + TAG + "]send fail[" + (dispatchIfSendException ? "dispatch" : "ignore") + "]. ipcPack: " + ipcPack, e);
                    if (dispatchIfSendException) {
                        this.dispatchMessage(ipcPack);
                    }

                    return false;
                }
            }
        } else {
            ELog.w("[" + TAG + "]send fail[" + (dispatchIfSendException ? "dispatch" : "ignore") + "]. ipcClientBinder or IPCServer is null(IPC Server not connected).");
            if (dispatchIfSendException) {
                this.dispatchMessage(ipcPack);
                return false;
            } else {
                throw new SDKIPCServerNotConnectedException("IPC Server is not connected !");
            }
        }
    }

    public void dispatchMessage(@NonNull IPCPack<Emit> ipcPack) {
        for(Subscriber next : this.subscribers) {
            next.onReceive(ipcPack.getContent(), ipcPack.getIpcRoute());
        }

    }
}