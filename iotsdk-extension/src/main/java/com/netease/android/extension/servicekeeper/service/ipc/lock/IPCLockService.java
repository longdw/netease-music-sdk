package com.netease.android.extension.servicekeeper.service.ipc.lock;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.netease.android.extension.func.NFunc0;
import com.netease.android.extension.func.NFuncB;
import com.netease.android.extension.servicekeeper.service.ipc.base.AbstractIPCService;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.parceltype.StringParcel;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCFuncB;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCServer;
import com.netease.android.extension.util.ELog;

public class IPCLockService extends AbstractIPCService<IPCLockServiceUniqueId> implements IIPCLockService {
    private static final String TAG = IPCLockService.class.getSimpleName();

    public IPCLockService(@NonNull IPCLockServiceUniqueId serviceUniqueId) {
        super(serviceUniqueId);
    }

    public void tryLock(@NonNull String target, int autoReleaseMillis, @NonNull final NFunc0 lockedCallBack) throws SDKIPCServerNotConnectedException {
        this.tryLock(target, autoReleaseMillis, new NFuncB() {
            public void call(boolean value) {
                if (value) {
                    lockedCallBack.call();
                }

            }
        });
    }

    public void tryLock(@NonNull String target, int autoReleaseMillis, @NonNull final NFuncB callBack) throws SDKIPCServerNotConnectedException {
        IPCServer ipcServer;
        if (null != this.ipcClientBinder && null != (ipcServer = this.ipcClientBinder.getIPCServerOrNull())) {
            IPCPack<StringParcel> ipcPack = new IPCPack(new StringParcel(target));
            if (!this.prepareIPCPack(ipcPack)) {
                ELog.w("[" + TAG + "]tryLock fail. ipcPack: " + ipcPack);
                callBack.call(false);
            } else {
                IPCRoute ipcRoute = ipcPack.getIpcRoute();
                Bundle paramExtra = new Bundle();
                paramExtra.putInt("LOCK__TTL", autoReleaseMillis);
                paramExtra.putString("LOCK__SERVICE_UNIQUE_ID", ((IPCLockServiceUniqueId)this.serviceUniqueId).getName());
                ipcRoute.setParamExtra(paramExtra);

                try {
                    ipcServer.tryLock(ipcPack, new IPCFuncB.Stub() {
                        public void onCall(boolean value) throws RemoteException {
                            callBack.call(value);
                        }
                    });
                } catch (Throwable throwable) {
                    ELog.e("[" + TAG + "]tryLock fail. ipcPack: " + ipcPack, throwable);
                    callBack.call(true);
                }

            }
        } else {
            throw new SDKIPCServerNotConnectedException("IPC Server not connected !");
        }
    }

    public void release(@NonNull String target, @NonNull final NFunc0 releasedCallBack) {
        this.release(target, new NFuncB() {
            public void call(boolean value) {
                if (value) {
                    releasedCallBack.call();
                }

            }
        });
    }

    public void release(@NonNull String target, @NonNull final NFuncB callBack) {
        IPCPack<StringParcel> ipcPack = new IPCPack(new StringParcel(target));
        IPCServer ipcServer;
        if (null != this.ipcClientBinder && null != (ipcServer = this.ipcClientBinder.getIPCServerOrNull())) {
            if (!this.prepareIPCPack(ipcPack)) {
                ELog.w("[" + TAG + "]release fail. ipcPack: " + ipcPack);
                callBack.call(false);
            } else {
                IPCRoute ipcRoute = ipcPack.getIpcRoute();
                Bundle paramExtra = new Bundle();
                paramExtra.putString("LOCK__SERVICE_UNIQUE_ID", ((IPCLockServiceUniqueId)this.serviceUniqueId).getName());
                ipcRoute.setParamExtra(paramExtra);

                try {
                    ipcServer.release(ipcPack, new IPCFuncB.Stub() {
                        public void onCall(boolean value) throws RemoteException {
                            callBack.call(value);
                        }
                    });
                } catch (Throwable throwable) {
                    ELog.e("[" + TAG + "]release fail. ipcPack: " + ipcPack, throwable);
                    callBack.call(false);
                }

            }
        } else {
            callBack.call(false);
        }
    }
}