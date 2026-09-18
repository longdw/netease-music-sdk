package com.netease.android.extension.servicekeeper.service.ipc.server;

import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import com.netease.android.extension.ext.CollectionExt;
import com.netease.android.extension.ext.MapExt;
import com.netease.android.extension.func.NFunc0R;
import com.netease.android.extension.func.NFunc1R;
import com.netease.android.extension.servicekeeper.service.ipc.server.lock.IProcessLockController;
import com.netease.android.extension.servicekeeper.service.ipc.server.lock.ProcessLockController;
import com.netease.android.extension.servicekeeper.service.ipc.server.message.IMessageController;
import com.netease.android.extension.servicekeeper.service.ipc.server.message.MessageController;
import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.usage.NLazy;
import com.netease.android.extension.util.ELog;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class IPCServerBinder extends IPCServer.Stub {
    private static final String TAG = IPCServerBinder.class.getSimpleName();
    private final ConcurrentHashMap<String, Set<ClientBinderWrapper>> subscribers = new ConcurrentHashMap();
    private final NLazy<IMessageController> messageController = new NLazy(new NFunc0R<IMessageController>() {
        public IMessageController call() {
            return new MessageController();
        }
    });
    private final NLazy<IProcessLockController> processLockController = new NLazy(new NFunc0R<IProcessLockController>() {
        public IProcessLockController call() {
            return new ProcessLockController();
        }
    });

    public void attach(SKCSerial skcSerial, final ClientBinderWrapper clientBinderWrapper, @Nullable IPCFunc ipcFunc) throws RemoteException {
        String skcName = skcSerial.getSkcName();
        final Set<ClientBinderWrapper> clientBinderWrappers = (Set)MapExt.computeIfAbsent(this.subscribers, skcName, new NFunc1R<String, Set<ClientBinderWrapper>>() {
            public Set<ClientBinderWrapper> call(String param) {
                return new LinkedHashSet();
            }
        });
        if (!clientBinderWrappers.add(clientBinderWrapper)) {
            throw new SecurityException("[" + TAG + "]Duplicate skcSerial in multi binderWrapper !");
        } else {
            try {
                DeathRecipient deathRecipient = new DeathRecipient() {
                    public void binderDied() {
                        ELog.w("[" + IPCServerBinder.TAG + "]ClientBinder died, removed from subscribers! " + clientBinderWrapper);
                        clientBinderWrappers.remove(clientBinderWrapper);

                        try {
                            clientBinderWrapper.getBinder().unlinkToDeath(this, 0);
                        } catch (Throwable throwable) {
                            ELog.e("[" + IPCServerBinder.TAG + "]ClientBinder died, unlinkToDeath error: ", throwable);
                        }

                    }
                };
                clientBinderWrapper.getBinder().linkToDeath(deathRecipient, 0);
            } catch (Throwable throwable) {
                ELog.e("[" + TAG + "]attach, linkToDeath error: ", throwable);
            }

            if (ELog.showLog()) {
                ELog.i("[" + TAG + "]ipcServerBinder.attach, clientBinders(size: " + this.subscribers.size() + "): " + this.subscribers);
            }

            if (null != ipcFunc) {
                ipcFunc.onCall();
            }

        }
    }

    public void detach(SKCSerial skcSerial, ClientBinderWrapper clientBinderWrapper, @Nullable IPCFunc ipcFunc) throws RemoteException {
        String skcName = skcSerial.getSkcName();
        Set<ClientBinderWrapper> clientBinderWrappers = (Set)this.subscribers.get(skcName);
        if (null != clientBinderWrappers) {
            clientBinderWrappers.remove(clientBinderWrapper);
            if (CollectionExt.isEmpty(clientBinderWrappers)) {
                this.subscribers.remove(skcName);
            }
        }

        if (ELog.showLog()) {
            ELog.i("[" + TAG + "]ipcServerBinder.detach, clientBinders(size: " + this.subscribers.size() + "): " + this.subscribers);
        }

        if (null != ipcFunc) {
            ipcFunc.onCall();
        }

    }

    public void send(String uniqueIdType, String uniqueId, IPCPack ipcPack) throws RemoteException {
        ((IMessageController)this.messageController.get()).handleMessage(this.subscribers, uniqueIdType, uniqueId, ipcPack);
    }

    public void tryLock(IPCPack ipcPack, IPCFuncB ipcFunB) throws RemoteException {
        boolean locked = ((IProcessLockController)this.processLockController.get()).tryLock(ipcPack);
        if (null != ipcFunB) {
            ipcFunB.onCall(locked);
        }

    }

    public void release(IPCPack ipcPack, IPCFuncB ipcFunB) throws RemoteException {
        boolean released = ((IProcessLockController)this.processLockController.get()).release(ipcPack);
        if (null != ipcFunB) {
            ipcFunB.onCall(released);
        }

    }
}