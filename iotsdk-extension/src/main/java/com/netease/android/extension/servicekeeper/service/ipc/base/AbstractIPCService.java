package com.netease.android.extension.servicekeeper.service.ipc.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.service.AbstractServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.client.IIPCClientBinder;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCRoute;
import com.netease.android.extension.servicekeeper.service.ipc.tx.SKCSerial;

public abstract class AbstractIPCService<ServiceUniqueId extends IIPCServiceUniqueId> extends AbstractServiceTick<ServiceUniqueId> implements IIPCServiceTick<ServiceUniqueId> {
    protected IIPCClientBinder ipcClientBinder;

    public AbstractIPCService(@NonNull ServiceUniqueId serviceUniqueId) {
        super(serviceUniqueId);
    }

    public void injectIpcClientBinder(@NonNull IIPCClientBinder ipcClientBinder) {
        this.ipcClientBinder = ipcClientBinder;
    }

    protected boolean prepareIPCPack(@Nullable IPCPack ipcPack) {
        if (null == this.ipcClientBinder) {
            return false;
        } else if (null == ipcPack) {
            return true;
        } else {
            IPCRoute ipcRoute = ipcPack.getIpcRoute();
            SKCSerial fromSKCSerial = ipcRoute.getFromSKCSerial();
            if (null == fromSKCSerial) {
                ipcRoute.setFromSKCSerial(this.ipcClientBinder.getSKCSerial());
            }

            return true;
        }
    }
}