package com.netease.android.extension.servicekeeper.service.ipc.observable;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.netease.android.extension.servicekeeper.error.SDKServiceNotFoundException;
import com.netease.android.extension.servicekeeper.service.ipc.base.IIPCServiceKeeper;
import com.netease.android.extension.servicekeeper.service.ipc.error.SDKIPCServerNotConnectedException;
import com.netease.android.extension.servicekeeper.service.ipc.tx.IPCPack;

public interface IIPCObservableServiceKeeper extends IIPCObservableServiceKeeperExport, IIPCServiceKeeper<IPCObservableServiceUniqueId, IIPCObservableService> {
    <Emit extends Parcelable> void send(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCPack<Emit> var2) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException;

    <Emit extends Parcelable> void send(@NonNull IPCObservableServiceUniqueId<Emit> var1, @Nullable Emit var2) throws SDKServiceNotFoundException, SDKIPCServerNotConnectedException;

    <Emit extends Parcelable> void sendOrIgnore(@NonNull IPCObservableServiceUniqueId<Emit> var1, @NonNull IPCPack<Emit> var2);

    <Emit extends Parcelable> void sendOrIgnore(@NonNull IPCObservableServiceUniqueId<Emit> var1, @Nullable Emit var2);
}