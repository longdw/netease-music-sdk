package com.netease.android.extension.servicekeeper.service.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.ext.CollectionExt;
import com.netease.android.extension.ext.ObjectExt;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.base.message.IIPCMessageServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.listener.OnIPCConnectListener;
import com.netease.android.extension.servicekeeper.service.ipc.server.IPCCommunicationAndroidService;
import com.netease.android.extension.servicekeeper.service.ipc.tx.ClientBinderWrapper;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCClient;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFunc;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer.Stub;
import com.netease.android.extension.util.ELog;
import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

public class IPCClientBinder implements IIPCClientBinder {
    private static final String TAG = IPCClientBinder.class.getSimpleName();
    private Context applicationContext;
    private boolean connected;
    private boolean attached;
    @Nullable
    private ClientBinderWrapper clientBinderWrapper;
    @Nullable
    private IPCServer ipcServer;
    @NonNull
    private SKCSerial skcSerial;
    @Nullable
    private Set<WeakReference<OnIPCConnectListener>> onIPCConnectedListeners;
    private Set<IIPCServiceKeeper> bindServiceKeepers = new LinkedHashSet();
    private final IBinder binder = new IPCClient.Stub() {
        public void onReceive(String uniqueId, IPCPack ipcPack) throws RemoteException {
            ELog.i("[IPCClientBinder]clientBinder, onReceive, type: " + uniqueId + ", p: " + ipcPack + ", pid: " + Process.myPid());
            if (!CollectionExt.isEmpty(IPCClientBinder.this.bindServiceKeepers)) {
                boolean found = false;

                for(IIPCServiceKeeper serverKeeper : IPCClientBinder.this.bindServiceKeepers) {
                    IIPCServiceTick service = serverKeeper.obtainServiceOrNull(uniqueId);
                    if (service instanceof IIPCMessageServiceTick) {
                        found = true;
                        ((IIPCMessageServiceTick)service).dispatchMessage(ipcPack);
                    }
                }

                if (!found) {
                    ELog.e("[IPCClientBinder]binder.onReceive, the service uniqueId[" + uniqueId + "] not found !, bindServiceKeepers.size: " + IPCClientBinder.this.bindServiceKeepers.size());
                }

            }
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            IPCClientBinder.this.connected = true;
            IPCClientBinder.this.attached = false;
            IPCClientBinder.this.ipcServer = Stub.asInterface(service);
            ELog.i("[IPCClientBinder]serviceConnection.onServiceConnected, ipcServer: " + IPCClientBinder.this.ipcServer);
            if (null == IPCClientBinder.this.clientBinderWrapper) {
                throw new IllegalArgumentException("[" + IPCClientBinder.TAG + "]serviceConnection.onServiceConnected, binderWrapper is null !");
            } else {
                IPCClientBinder.this.onServiceConnected(name);

                try {
                    if (null != IPCClientBinder.this.ipcServer) {
                        IPCClientBinder.this.ipcServer.attach(IPCClientBinder.this.skcSerial, IPCClientBinder.this.clientBinderWrapper, new IPCFunc.Stub() {
                            public void onCall() throws RemoteException {
                                IPCClientBinder.this.onAttach();
                            }
                        });
                    }
                } catch (RemoteException e) {
                    ELog.e("[" + IPCClientBinder.TAG + "]serviceConnection.onServiceConnected error: ", e);
                }

            }
        }

        public void onServiceDisconnected(ComponentName name) {
            ELog.i("[IPCClientBinder]serviceConnection, onServiceDisconnected, name: " + name);
            IPCClientBinder.this.connected = false;
            IPCClientBinder.this.attached = false;
            if (null == IPCClientBinder.this.clientBinderWrapper) {
                ELog.e("[" + IPCClientBinder.TAG + "]serviceConnection.onServiceDisconnected, binderWrapper is null !");
            } else {
                try {
                    IPCClientBinder.this.detach();
                } catch (Throwable e) {
                    ELog.e("[" + IPCClientBinder.TAG + "]serviceConnection.onServiceDisconnected detach error: ", e);
                }

                IPCClientBinder.this.onServiceDisconnectedException();
            }
        }
    };

    public IPCClientBinder(@NonNull Context applicationContext, @NonNull SKCSerial skcSerial) {
        this.applicationContext = applicationContext;
        this.skcSerial = skcSerial;
    }

    public void initialize() {
        this.connect();
    }

    private void connect() {
        this.clientBinderWrapper = new ClientBinderWrapper(this.skcSerial, this.binder);
        IPCCommunicationAndroidService.bindServerService(this.applicationContext, this.serviceConnection, 1);
    }

    public void bindServiceKeeper(IIPCServiceKeeper serviceKeeper) {
        this.bindServiceKeepers.add(serviceKeeper);
    }

    public void unbindServiceKeeper(IIPCServiceKeeper serviceKeeper) {
        this.bindServiceKeepers.remove(serviceKeeper);
    }

    private void detach() throws RemoteException {
        if (null != this.ipcServer && null != this.clientBinderWrapper) {
            ELog.i("[IPCClientBinder]detach...");
            this.connected = false;

            try {
                this.ipcServer.detach(this.skcSerial, this.clientBinderWrapper, new IPCFunc.Stub() {
                    public void onCall() throws RemoteException {
                        IPCClientBinder.this.onDetach();
                    }
                });
            } catch (Throwable throwable) {
                ELog.e("[IPCClientBinder]detach, ipcServer.detach error: ", throwable);
            } finally {
                this.ipcServer = null;
                this.clientBinderWrapper = null;
            }
        }

    }

    private void onAttach() {
        this.attached = true;
        if (!CollectionExt.isEmpty(this.onIPCConnectedListeners)) {
            for(WeakReference<OnIPCConnectListener> onIPCConnectedListenerWeakRef : this.onIPCConnectedListeners) {
                OnIPCConnectListener onIPCConnectListener;
                if (null != onIPCConnectedListenerWeakRef && null != (onIPCConnectListener = (OnIPCConnectListener)onIPCConnectedListenerWeakRef.get())) {
                    onIPCConnectListener.onIPCAttached();
                }
            }
        }

    }

    private void onDetach() {
        this.attached = true;
        if (!CollectionExt.isEmpty(this.onIPCConnectedListeners)) {
            for(WeakReference<OnIPCConnectListener> onIPCConnectedListenerWeakRef : this.onIPCConnectedListeners) {
                OnIPCConnectListener onIPCConnectListener;
                if (null != onIPCConnectedListenerWeakRef && null != (onIPCConnectListener = (OnIPCConnectListener)onIPCConnectedListenerWeakRef.get())) {
                    onIPCConnectListener.onIPCDetached();
                }
            }
        }

    }

    protected void onServiceConnected(ComponentName name) {
        if (!CollectionExt.isEmpty(this.onIPCConnectedListeners)) {
            for(WeakReference<OnIPCConnectListener> onIPCConnectedListenerWeakRef : this.onIPCConnectedListeners) {
                OnIPCConnectListener onIPCConnectListener;
                if (null != onIPCConnectedListenerWeakRef && null != (onIPCConnectListener = (OnIPCConnectListener)onIPCConnectedListenerWeakRef.get())) {
                    onIPCConnectListener.onIPCConnected();
                }
            }
        }

    }

    protected void onServiceDisconnectedException() {
        ELog.w("[IPCClientBinder]onServiceDisconnectedException, reconnect()...");
        this.onServiceDisconnected();
        this.connect();
    }

    protected void onServiceDisconnected() {
        if (ELog.showLog()) {
            ELog.i("[IPCClientBinder]onServiceDisconnected...");
        }

        if (!CollectionExt.isEmpty(this.onIPCConnectedListeners)) {
            for(WeakReference<OnIPCConnectListener> onIPCConnectedListenerWeakRef : this.onIPCConnectedListeners) {
                OnIPCConnectListener onIPCConnectListener;
                if (null != onIPCConnectedListenerWeakRef && null != (onIPCConnectListener = (OnIPCConnectListener)onIPCConnectedListenerWeakRef.get())) {
                    onIPCConnectListener.onIPCDisconnected();
                }
            }
        }

    }

    public void destroy() {
        try {
            this.detach();
            IPCCommunicationAndroidService.unbindServerService(this.applicationContext, this.serviceConnection);
            this.connected = false;
            this.onServiceDisconnected();
        } catch (Throwable e) {
            ELog.e("[IIPCClientBinder]destroy, detach error, e: ", e);
        }

        this.bindServiceKeepers.clear();
    }

    @Nullable
    public IPCServer getIPCServerOrNull() {
        return this.ipcServer;
    }

    @NonNull
    public SKCSerial getSKCSerial() {
        return this.skcSerial;
    }

    public void addOnIPCConnectedListener(OnIPCConnectListener onIPCConnectListener) {
        if (null == this.onIPCConnectedListeners) {
            this.onIPCConnectedListeners = new LinkedHashSet();
        }

        for(WeakReference<OnIPCConnectListener> onIPCConnectedListenerRef : this.onIPCConnectedListeners) {
            if (ObjectExt.equals(onIPCConnectedListenerRef.get(), onIPCConnectListener)) {
                return;
            }
        }

        this.onIPCConnectedListeners.add(new WeakReference(onIPCConnectListener));
    }
}
