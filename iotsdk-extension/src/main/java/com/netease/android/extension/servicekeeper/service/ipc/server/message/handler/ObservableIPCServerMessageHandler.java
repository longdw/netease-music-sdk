package com.netease.android.extension.servicekeeper.service.ipc.server.message.handler;

import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import com.netease.android.extension.servicekeeper.id.ServiceUniqueIdType;
import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient.Stub;
import com.netease.android.extension.util.ELog;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ObservableIPCServerMessageHandler implements IIPCServerMessageHandler {
    private static final String TAG = ObservableIPCServerMessageHandler.class.getSimpleName();

    public void handle(Set<ClientBinderWrapper> clientBinderWrappers, ServiceUniqueIdType serviceUniqueIdType, String uniqueId, IPCPack ipcPack) {
        Iterator<ClientBinderWrapper> iterator = (new CopyOnWriteArraySet(clientBinderWrappers)).iterator();

        while(iterator.hasNext()) {
            ClientBinderWrapper clientBinder = (ClientBinderWrapper)iterator.next();
            IBinder binder = clientBinder.getBinder();

            try {
                Stub.asInterface(binder).onReceive(uniqueId, ipcPack);
            } catch (DeadObjectException e) {
                ELog.e("[" + TAG + "]Dead client bind proxy object error, remove it !", e);
                iterator.remove();
            } catch (RemoteException e) {
                ELog.e("[" + TAG + "]error: ", e);
            }
        }

    }
}