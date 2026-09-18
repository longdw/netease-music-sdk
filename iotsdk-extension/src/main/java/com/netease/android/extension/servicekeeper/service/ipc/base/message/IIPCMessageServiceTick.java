package com.netease.android.extension.servicekeeper.service.ipc.base.message;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.id.IServiceUniqueId;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceTick;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;

public interface IIPCMessageServiceTick<Emit extends Parcelable, ServiceUniqueId extends IServiceUniqueId> extends IIPCServiceTick<ServiceUniqueId> {
    boolean send(@Nullable Emit var1) throws SDKIPCServerNotConnectedException;

    boolean sendOrDispatch(@Nullable Emit var1);

    boolean send(@NonNull IPCPack<Emit> var1) throws SDKIPCServerNotConnectedException;

    boolean sendOrDispatch(@NonNull IPCPack<Emit> var1);

    void dispatchMessage(@NonNull IPCPack<Emit> var1);
}